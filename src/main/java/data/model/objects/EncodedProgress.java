package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;

import java.util.UUID;

public class EncodedProgress extends DatabaseObject {
    private Integer totalFrames = -1;
    private Integer passPhase = -1;
    private Integer pass1Progress = -1;
    private String pass1FileName = "";
    private Integer pass2Progress = -1;
    private String pass2FileName = "";

    public EncodedProgress() {
        super();
    }

    public EncodedProgress(UUID uuid, Integer totalFrames, Integer passPhase, Integer pass1Progress, String pass1FileName, Integer pass2Progress, String pass2FileName) {
        super(uuid);
        this.totalFrames = totalFrames;
        this.passPhase = passPhase;
        this.pass1Progress = pass1Progress;
        this.pass1FileName = pass1FileName;
        this.pass2Progress = pass2Progress;
        this.pass2FileName = pass2FileName;
    }

    @JSONMappable("passPhase")
    public Integer getPassPhase() {
        return passPhase;
    }

    public void setPassPhase(Integer passPhase) {
        this.passPhase = passPhase;
    }

    @JSONMappable("pass1Progress")
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

    @JSONMappable("pass2Progress")
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

    @JSONMappable("totalFrames")
    public Integer getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(Integer totalFrames) {
        this.totalFrames = totalFrames;
    }
}

