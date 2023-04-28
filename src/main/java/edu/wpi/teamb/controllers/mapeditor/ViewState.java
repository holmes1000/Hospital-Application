package edu.wpi.teamb.controllers.mapeditor;

public class ViewState implements MapEditorState{
    @Override
    public void printStatus() {
        System.out.println("Map Editor is in View State");
    }
}
