package edu.wpi.teamb.controllers.mapeditor;

public class MapEditorContext implements MapEditorState {
    private MapEditorState state;
    public void setState(MapEditorState state) {
        this.state = state;
    }

    public MapEditorState getState() {
        return state;
    }

    @Override
    public void handleMouseClick(int x, int y) {
        state.handleMouseClick(x, y);
    }

    @Override
    public void handleMouseDrag(int x, int y) {
        state.handleMouseDrag(x, y);
    }

    @Override
    public void handleMouseRelease(int x, int y) {
        state.handleMouseRelease(x, y);
    }

    @Override
    public void handleMouseHover(int x, int y) {
        state.handleMouseHover(x, y);
    }

    @Override
    public void printStatus() {
        state.printStatus();
    }
}
