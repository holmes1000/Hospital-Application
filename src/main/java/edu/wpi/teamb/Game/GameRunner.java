package edu.wpi.teamb.Game;

import com.sun.javafx.application.LauncherImpl;

;

public class GameRunner  {

    /**
     * Runs the game in a new window
     * @param args
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(Gapp.class, null, args);
    }



}
