package edu.wpi.teamb.entities;

public enum DefaultStart {
    DEFAULT_START (new EDefaultPathfindingStart());

    DefaultStart(EDefaultPathfindingStart eDefaultPathfindingStart) {

    }

    public String getStart() {
        return DEFAULT_START.getStart();
    }

    public void setStart(String longname) {
        DEFAULT_START.setStart(longname);
    }
}
