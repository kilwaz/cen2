package data.model.objects;

import data.model.DatabaseObject;

import java.util.UUID;

public class EncodedProgress extends DatabaseObject {
    private Integer passPhase = -1;
    private Integer pass1Progress = -1;
    private String pass1FileName = "";
    private Integer pass2Progress = -1;
    private String pass2FileName = "";

    public EncodedProgress() {
        super();
    }

    public EncodedProgress(UUID uuid, Integer passPhase, Integer pass1Progress, String pass1FileName, Integer pass2Progress, String pass2FileName) {
        super(uuid);
        this.passPhase = passPhase;
        this.pass1Progress = pass1Progress;
        this.pass1FileName = pass1FileName;
        this.pass2Progress = pass2Progress;
        this.pass2FileName = pass2FileName;
    }

    public Integer getPassPhase() {
        return passPhase;
    }

    public void setPassPhase(Integer passPhase) {
        this.passPhase = passPhase;
    }

    public Integer getPass1Progress() {
        return pass1Progress;
    }

    public void setPass1Progress(Integer pass1Progress) {
        this.pass1Progress = pass1Progress;
    }

    public String getPass1FileName() {
        return pass1FileName;
    }

    public void setPass1FileName(String pass1FileName) {
        this.pass1FileName = pass1FileName;
    }

    public Integer getPass2Progress() {
        return pass2Progress;
    }

    public void setPass2Progress(Integer pass2Progress) {
        this.pass2Progress = pass2Progress;
    }

    public String getPass2FileName() {
        return pass2FileName;
    }

    public void setPass2FileName(String pass2FileName) {
        this.pass2FileName = pass2FileName;
    }
}

