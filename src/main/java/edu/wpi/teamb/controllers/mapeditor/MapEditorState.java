package edu.wpi.teamb.controllers.mapeditor;

public interface MapEditorState {
    public void handleMouseClick(int x, int y);
    public void handleMouseDrag(int x, int y);
    public void handleMouseRelease(int x, int y);
    public void handleMouseHover(int x, int y);
    public void printStatus();
}
