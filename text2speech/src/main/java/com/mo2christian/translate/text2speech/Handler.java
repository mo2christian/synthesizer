package com.mo2christian.translate.text2speech;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.mo2christian.translate.text2speech.EnvVariables.*;

/**
 * Convertie un test en audio.
 *
 */
public class Handler implements RequestHandler<Request, Response> {

    private static final Logger logger = LogManager.getLogger(Handler.class);

    private final AmazonPolly polly;

    private final AmazonS3 s3;

    public Handler() {
        AWSCredentialsProvider credentials = new EnvironmentVariableCredentialsProvider();
        polly = AmazonPollyClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(value(REGION))
                .build();
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(value(REGION))
                .build();
    }

    public Response handleRequest(Request request, Context context) {
        logger.info("Traduction du texte {} en {}", request.getText(), request.getLanguage() + "-" + request.getGender());
        String outputFile = value(OUTPUT_FILE, "/tmp/output.mp3");
        synthesizeSpeech(outputFile, request.getText(), getVoice(request.getLanguage(), request.getGender()));
        URL url = putObject(UUID.randomUUID().toString(), outputFile);
        Response response = new Response();
        response.setText(request.getText());
        response.setLink(url.toString());
        logger.info("Traduction terminée - fichier {}", response.getLink());
        return response;
    }

    /**
     * Convertie le texte en audio mp3 et l'enregistre dans le fichier.
     * @param outputFileName
     * @param text
     * @param voiceId
     */
    private void synthesizeSpeech(String outputFileName, String text, VoiceId voiceId) {
        logger.info("Conversion du texte");
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withOutputFormat(OutputFormat.Mp3)
                .withVoiceId(voiceId)
                .withText(text);

        try (FileOutputStream outputStream = new FileOutputStream(new File(outputFileName))) {
            SynthesizeSpeechResult synthesizeSpeechResult = polly.synthesizeSpeech(synthesizeSpeechRequest);
            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            try (InputStream in = synthesizeSpeechResult.getAudioStream()){
                while ((readBytes = in.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readBytes);
                }
            }
        } catch (Exception e) {
            logger.error("Error while translating ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne une voix
     * @param lang
     * @param gender
     * @return
     */
    private VoiceId getVoice(String lang, Character gender){
        return Voice.get(lang, gender);
    }

    /**
     * Enregistre l'object dans S3 et retourne l'url signé.
     * @param key
     * @param path
     * @return
     */
    private URL putObject(String key, String path){
        logger.info("Sauvegarde de l'audio dans S3");
        PutObjectRequest request = new PutObjectRequest(value(BUCKET_NAME), key, new File(path));
        PutObjectResult result = s3.putObject(request);
        return s3.generatePresignedUrl(request.getBucketName(), request.getKey(), Date.from(Instant.now().plus(intValue(URL_DURATION, 15), ChronoUnit.MINUTES)));
    }

}
