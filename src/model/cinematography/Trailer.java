package model.cinematography;

/*
TODO dokumentacja
TODO Odtwarzanie
 */
public class Trailer {
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
