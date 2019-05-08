package com.mo2christian.translate.text2speech;

/**
 * Main class pour les tests en local
 */
public class Application {

    private Handler handler;

    public Application() {
        handler = new Handler();
    }

    public Response exec(Request request){
        return handler.handleRequest(request, null);
    }

    public static final void main(String args[]){
        Request request = new Request("Let's transcribe this text", "en", 'M');
        new Application().exec(request);
    }
}
