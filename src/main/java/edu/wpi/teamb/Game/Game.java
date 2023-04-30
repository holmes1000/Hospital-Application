package edu.wpi.teamb.Game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import edu.wpi.teamb.Game.PatientThings.patient;
import edu.wpi.teamb.Game.Player.Player;
import edu.wpi.teamb.Game.Player.TimeController;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Game {
    /* Game objects */
    static Player player;
    public static Queue<patient> customerQ;
    public static Stack<patient> customerS;
    static TimeController timeController;
    // game state
    double timePassed;
    double gameSpeed;
    difficultyLevels curDif;
    static boolean running = false;
    static Canvas canvas;
    static GraphicsContext gc;

 

    private static Game game;

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

    /**
     * contructor is prviate so we can't make multiple games at once
     */
    private Game() {
        int timeLeft = 50;
        player = new Player(0, timeLeft);
        timeController = TimeController.newTimeController(50);
        gameSpeed = 1;
        customerQ = new LinkedList<>();
        customerS = new Stack<>();
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
                update(dt/10);

                // calls paint component
                repaint(dt/10);
                dt--;
                drawcount++;

            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawcount);
                drawcount = 0;
                timer = 0;
                // degub info
                System.out.println("update: " + timer);
            }
            if(timeController.getTimeLeft()<=0)
            {
                stop();
            }

            // checkLose();
        }
        System.out.println("-------------------END-------------------");

    }

    /**
     * stops the game
     */
    public static void stop() {
        running = false;
        //destroy this instance of the game
        game = null;
    }
    public static void kill()
    {
        //uninit all objects
        customerQ=null;
        customerS= null;
        TimeController.kill();
        player = null;
        canvas = null;
        gc = null;

        //destroy this instance of the game
        game = null;
    
    }

    /**
     * updates all objects
     * 
     * @param delta time since last frame
     */
    private void update(double delta) {
        timeController.update(delta);
        player.update(delta);
        for (patient patient : customerQ) {
            patient.update(delta);

        }

    }

    /**
     * Repaints all the objects
     * 
     * @param delta time since last frame;
     */
    private void repaint(double delta) {
        //gc.clearRect(0, 0, canvas.getLayoutX(), canvas.getLayoutY());
        timeController.show(gc);
        player.show(gc);
        for (patient patient : customerS) {
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
        if (canvas == null)
        {
            canvas = Gapp.getMainCanvas();
            gc = canvas.getGraphicsContext2D();
        }

    }

}
