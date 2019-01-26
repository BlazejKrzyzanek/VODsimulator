package model.cinematography;

import java.io.Serializable;

/*
TODO dokumentacja
TODO Odtwarzanie
 */
public class Trailer implements Serializable {
    private String sourceURL;

    public Trailer(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public void Play(){
    }
}
