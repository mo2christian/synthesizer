package com.mo2christian.translate.text2speech;

/**
 * Represente les variables d'environnement du systeme.
 */
public class EnvVariables {

    public static final String REGION = "REGION";

    public static final String BUCKET_NAME = "BUCKET";

    public static final String URL_DURATION = "URL_DURATION";

    public static final String OUTPUT_FILE = "OUTPUTFILE";

    public static final String value(String name){
        String value = System.getenv(name);
        if (value == null){
            throw new IllegalArgumentException("Unkown property " + name);
        }
        return value;
    }

    public static final String value(String name, String defaultValue){
        String value = System.getenv(name);
        if (value == null){
            value = defaultValue;
        }
        return value;
    }

    public static final int intValue(String name){
        String value = value(name);
        return Integer.parseInt(value);
    }

    public static final int intValue(String name, int defaultValue){
        String value = value(name, Integer.toString(defaultValue));
        return Integer.parseInt(value);
    }

}
