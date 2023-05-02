package edu.wpi.teamb.entities;

public class DefaultStart {


    private static final DefaultStart INSTANCE = new DefaultStart();

    private DefaultStart(){
        this.default_start = "";
        this.default_end = "";
        this.true_default_start = "15 Lobby Entrance Floor 2";
    }

    private String default_start = "";
    private  String default_end = "";

    private String true_default_start = "";

    public static DefaultStart getInstance(){
        return INSTANCE;
    }

    public String getDefault_start() {
        return default_start;
    }

    public void setDefault_start(String default_start) {
        this.default_start = default_start;
    }

    public String getDefault_end() {
        return default_end;
    }

    public void setDefault_end(String default_end) {
        this.default_end = default_end;
    }

    public String getTrue_default_start() {return true_default_start;}

    public void setTrue_default_start(String true_default_start) {this.true_default_start = true_default_start;}
}
