package Object;

import java.io.Serializable;

public class CodeResult implements Serializable{
    private String formattedSrc;
    private String execResult;

    public CodeResult() {
        this.formattedSrc = "";
        this.execResult = "";
    }
    
    public CodeResult(String formattedSrc, String execResult) {
        this.formattedSrc = formattedSrc;
        this.execResult = execResult;
    }

    public String getFormattedSrc() {
        return formattedSrc;
    }

    public void setFormattedSrc(String formattedSrc) {
        this.formattedSrc = formattedSrc;
    }

    public String getExecResult() {
        return execResult;
    }

    public void setExecResult(String execResult) {
        this.execResult = execResult;
    }
}
