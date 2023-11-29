package Object;

import java.io.Serializable;

public class Code implements Serializable{
    private String source;
    private String language;
    private String input;


    public Code() {
        this.source = "";
        this.language = "";
        this.input = "";
    }

    public Code(String source, String language, String input) {
        this.source = source;
        this.language = language;
        this.input = input;
    }

    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
