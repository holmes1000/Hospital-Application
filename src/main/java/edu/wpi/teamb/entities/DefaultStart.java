package edu.wpi.teamb.entities;

public class DefaultStart {


    private static final DefaultStart INSTANCE = new DefaultStart();

    private DefaultStart(){
        this.default_start = "";
    }

    private String default_start = "Neuroscience Waiting Room";

    public static DefaultStart getInstance(){
        return INSTANCE;
    }

    public String getDefault_start() {
        return default_start;
    }

    public void setDefault_start(String default_start) {
        this.default_start = default_start;
    }
}
