package com.mo2christian.translate.text2speech;

import com.amazonaws.services.polly.model.VoiceId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Liste des voix à utiliser
 */
public class Voice {

    private static final Character MALE = 'M';

    private static final Character FEMALE = 'F';

    private static final List<String> LANGUAGES = new LinkedList<>();

    private static final Map<String, Map<Character, VoiceId>> voices = new HashMap<>();

    static {
        Map<Character, VoiceId> fr = new HashMap<>();
        fr.put(MALE, VoiceId.Mathieu);
        fr.put(FEMALE, VoiceId.Celine);
        LANGUAGES.add("fr");
        voices.put("fr", fr);

        Map<Character, VoiceId> en = new HashMap<>();
        en.put(MALE, VoiceId.Matthew);
        en.put(FEMALE, VoiceId.Ivy);
        LANGUAGES.add("en");
        voices.put("en", en);

    }

    /**
     * Retourne la voix correspondant.
     * @param language
     * @param gender
     * @return
     */
    public static VoiceId get(String language, Character gender){
        checkVoice(language, gender);
        return voices.get(language).get(gender);
    }

    /**
     * Verifie la voix demandée est disponible
     * @param language
     * @param gender
     */
    public static void checkVoice(String language, Character gender){
        if (!MALE.equals(gender) && !FEMALE.equals(gender))
            throw new IllegalArgumentException("Invalid value gender : " + gender);
        if (!LANGUAGES.contains(language))
            throw new IllegalArgumentException("Invalid language : " + language);
    }

}
