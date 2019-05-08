package com.mo2christian.translate.text2speech;

import java.io.Serializable;

/**
 * Object de retour de la fonction lambda
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private String link;

    private String text;

    public Response() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
