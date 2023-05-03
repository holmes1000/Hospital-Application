package edu.wpi.teamb.Game;

import com.sun.javafx.application.LauncherImpl;

;

public class GameRunner  {

    /**
     * Runs the game in a new window
     * DO NOT run if the main app is running. it won't work
     * @param args
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(Gapp.class, null, args);
    }



}
