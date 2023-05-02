package edu.wpi.teamb.Game;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import edu.wpi.teamb.Game.Contollers.GameScnController;
import edu.wpi.teamb.Game.PatientThings.patient;
import edu.wpi.teamb.Game.Player.Player;
import edu.wpi.teamb.Game.Player.TimeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class Game {
    /* Game objects */
    static Player player;
    public static LinkedList<patient> customerQ;
    public static LinkedList<patient> customerS;
    public static ArrayList<patient> customersDone;
    public static TimeController timeController;
    // game state
    double timePassed;
    double gameSpeed;
    static difficultyLevels curDif;
    static boolean running = false;
    static Canvas canvas;
    static GraphicsContext gc;

    private static Game game;

    public enum difficultyLevels {
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

    /**
     * contructor is prviate so we can't make multiple games at once
     */
    private Game() {
        int timeLeft = 50;
        player = new Player(0, timeLeft);
        timeController = TimeController.newTimeController(20);
        gameSpeed = 1;
        customerQ = new LinkedList<>();
        customerS = new LinkedList<>();
        customersDone = new ArrayList<>();
        curDif = difficultyLevels.EASY;
        running = true;

    }

    /**
     * creates a new game if one does not exist
     * 
     * @return the ne game
     */
    public static Game newGame() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    public static difficultyLevels getCurDif() {
        return curDif;
    }

    public void start(Thread mfxThread) {
        final int TARGET_FPS = 60;

        double drawInterval = 1000000000 / TARGET_FPS;
        double dt = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawcount = 0;

        initCanvas(mfxThread);

        /* while thread exists, update then repaint */
        while (running/* !Thread.interrupted() */) // while thread exists
        {

            currentTime = System.nanoTime();

            dt += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (dt >= 1) {
                // calls update
                update(dt / 60);

                // calls paint component
                repaint(dt / 60);


                dt--;
                drawcount++;

            }
            if (timer >= 1000000000) {
                if((int)(Math.random()*3)==1)
                changeDif();
                
                //print position of every patiend in cQ and cS and cDone
                for (patient patient : customerQ) {
                    System.out.print(patient.getPosition()+" ");
                }System.out.println("\t:"+customerQ.size());
                for (patient patient : customerS) {
                    System.out.print(patient.getPosition()+" ");
                }System.out.println("\t:"+customerS.size());
                for (patient patient : customersDone) {
                    System.out.print(patient.getPosition()+ " ");
                }System.out.println("\t:"+customersDone.size());
                
                System.out.println("FPS: " + drawcount);
                drawcount = 0;
                timer = 0;
                // degub info
            }
            if (timeController.getTimeLeft() <= 0) {
                stop();
            }

            // checkLose();
        }
        repaint(0);

        System.out.println("-------------------END-------------------");
        finalPrint(gc);
    }

    private void finalPrint(GraphicsContext gc)
    {
        //paint to the canvas a black box with rounded corners and a white border
        gc.setFill(Colors.black);
        gc.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 20, 20);
        gc.setStroke(Colors.white);
        gc.setLineWidth(5);
        gc.strokeRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 20, 20);
        //Text in the Middle of the screen saying GAME OVER
        gc.setFill(Colors.white);
        gc.setFont(new Font("Arial", 50));
        gc.fillText("GAME OVER", canvas.getWidth()/2-150, canvas.getHeight()/2);
        //Text in the Middle of the screen saying Time Lasted: + seconds
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Time Lasted: "+NumberFormat.getInstance().format(timeController.gettotalTime()/60)+" seconds", canvas.getWidth()/2-150, canvas.getHeight()/2+50);
        //text under that saying now many poeple served (score)
        gc.fillText("People Served: "+player.getScore()+" people", canvas.getWidth()/2-150, canvas.getHeight()/2+100);
    }
    /**
     * stops the game
     */
    public static void stop() {
        running = false;
        // destroy this instance of the game
        game = null;
    }

    public static void kill() {
        // uninit all objects
        customerQ = null;
        customerS = null;
        TimeController.kill();
        player = null;
        canvas = null;
        gc = null;

        // destroy this instance of the game
        game = null;

    }

    /**
     * updates all objects
     * 
     * @param delta time since last frame
     */
    private void update(double delta) {
        QueueStackSync();
        rePosition();
        timeController.update(delta);
        player.update(delta);
        for (int i = 0; i < customerQ.size(); i++) {
            patient a = customerQ.get(i);
            a.update(delta);

        }

        // updates all in cD and when past screen, gets deleted
        for (int i = 0; i < customersDone.size(); i++) {
            customersDone.get(i).update(delta);
            if (customersDone.get(i).getX() > canvas.getWidth() + 30) {
                customersDone.remove(customersDone.get(i));
                i--;
            }
        }
    

    }

    public void QueueStackSync() {
        customerS = new LinkedList<>();
        for (int i = 0; i < customerQ.size(); i++) {

            customerS.add(0, customerQ.get(i));
        }
    }

    public static void rePosition() {
        for (int i = 0; i < customerQ.size(); i++) {
            customerQ.get(i).setPosition(i + 1);
        }
    }

    /**
     * Repaints all the objects
     * 
     * @param delta time since last frame;
     */
    private void repaint(double delta) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        timeController.show(gc);
        player.show(gc);

        // loop through sutomerS and customer done to show them
        for (patient patient : customerS) {
            patient.show(gc);
        }
        for (patient patient : customersDone) {
            // patient.dequeueing();
            patient.show(gc);

        }
    }

    public static Player getPlayer() {
        return player;
    }

    /**
     * Checks if there is not time left
     * if there is not, then the game ends
     * 
     * @return boolean if they lose or not
     */
    public boolean checkLose() {
        if (timeController.getTimeLeft() <= 0) {
            Thread.currentThread().interrupt();
            return true;
        }
        return false;
    }

    /**
     * randomly changes the difficulty overtime
     */
    public void changeDif() {
        // radomly chose a dificulty level
        adjustDifficulty(difficultyLevels.int2dif((int) (Math.random() * 3)));
    }

    /**
     * helper method to change the difficulty
     * 
     * @param d difficulty the chage to
     */
    private void adjustDifficulty(difficultyLevels d) {
        curDif = d;
    }

    public void initCanvas(Thread jfxThread) {
        if (canvas == null) {
            canvas = Gapp.getMainCanvas();
            gc = canvas.getGraphicsContext2D();
        }

    }

}
