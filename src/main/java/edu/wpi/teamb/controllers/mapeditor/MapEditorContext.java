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
    public void printStatus() {
        state.printStatus();
    }
}
