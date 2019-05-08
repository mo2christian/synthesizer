package com.mo2christian.translate.text2speech;

import java.io.Serializable;

/**
 * Object encapsulant la requete à la fonction lambda.
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;

    private String language;

    private Character gender;

    public Request() {
    }

    public Request(String text, String language, Character gender) {
        this.text = text;
        this.language = language;
        this.gender = gender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }
}
