package edu.wpi.teamb.Game;

import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.teamb.Game.PatientThings.patient;
import edu.wpi.teamb.Game.Player.Player;
import edu.wpi.teamb.Game.Player.TimeController;

public class Game {
    Player player;
    Queue<patient> customerQ;
    double timePassed;
    TimeController timeController;
    double gameSpeed;

    public Game()
    {
        int timeLeft = 50;
        player = new Player(0, timeLeft);
        timeController = new TimeController(50);
        gameSpeed = 1;
        customerQ = new LinkedList<>();
    }

    enum difficultyLevels {
        EASY, MED, HARD;

        public static difficultyLevels int2dif(int d) {

            switch (d) {
                case 0:
                    return EASY;

                case 1:
                    return MED;

                default:
                    return HARD;

            }


        }
    }

    difficultyLevels curDif;

    public void update() {
        

    }

    public void show() {

    }

    public void lose() {

    }

    public void changeDif() {
        // radomly chose a dificulty level
        adjustDifficulty(difficultyLevels.int2dif((int) (Math.random() * 3)));
    }

    private void adjustDifficulty(difficultyLevels d) {
        curDif = d;
    }

}
