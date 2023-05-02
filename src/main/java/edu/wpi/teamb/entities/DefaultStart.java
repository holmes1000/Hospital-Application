package edu.wpi.teamb.entities;

public class DefaultStart {


    private static final DefaultStart INSTANCE = new DefaultStart();

    private DefaultStart(){
        this.default_start = "";
        this.default_end = "";
    }

    private String default_start = "";
    private  String default_end = "";

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
}
