package edu.wpi.teamb.Game.Player;

import edu.wpi.teamb.Game.Colors;
import edu.wpi.teamb.Game.Game;
import edu.wpi.teamb.Game.PatientThings.patient;
import javafx.scene.canvas.GraphicsContext;

public class TimeController {
    static TimeController tC = null;
    double timeLeft;
    double maxTime;
    double totalTime;

    double patientTime = 3;
    double timeTilNewPatient;

    final double submitCooldownTime =0.5;
    double submitCooldown;

    /* render stuff */
    int x = 60, y = 10, width = 530, height = 25, border = 5;

    /**
     * Constructor is private so we can't make multiple time controllers
     * 
     * @param maxTime the max time for the game
     */

    private TimeController(double maxTime) {
        this.maxTime = maxTime;
        timeLeft = maxTime;
        timeTilNewPatient = 0;
        submitCooldown = submitCooldownTime;
        totalTime=0;
    }

    /**
     * Creates a new time controller if one does not exist
     * 
     * @param maxtime the max time for the game
     * @return the time controller
     */
    public static TimeController newTimeController(double maxtime) {
        if (tC == null) {
            tC = new TimeController(maxtime);
        }
        return tC;

    }

    public static void kill() {
        tC = null;

    }

    /**
     * Sets the time left in the game
     */
    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * returns the time left
     * 
     * @return the time left
     */
    public double getTimeLeft() {
        return timeLeft;
    }

    /**
     * returns the time until the next patient is spawned
     * 
     * @return the time until the next patient is spawned
     */
    public double getNewPatientTime() {
        return timeTilNewPatient;

    }

    /**
     * The time until the next patient is spawned
     * 
     * @param t the time until the next patient is spawned
     */
    public void setNewPatientTime(double t) {
        patientTime = t;
    }

    /**
     * subtracts time from the time left
     * 
     * @param time time to subtract
     */
    public void subtractTime(double time) {
        
        timeLeft -= time;
        if(timeLeft<0)
        {
            timeLeft=0;
        }
    }

    /**
     * adds time to the time left
     * 
     * @param time time to add
     */
    public void addTime(double time) {
        if (timeLeft + time > maxTime)
            timeLeft = maxTime;
        else if (timeLeft + time < 0)
            timeLeft = 0;
        else
            timeLeft += time;
    }

    public double gettotalTime()
    {
        return totalTime;
    }
    /**
     * updates the time
     * 
     * @param time time since last update
     */

    public void update(double time) {
        totalTime++;
        subtractTime(time);
        if (timeTilNewPatient > 0) {
            timeTilNewPatient -= time;
        } else if(Game.customerQ.size()<=10){

            timeTilNewPatient = patientTime/(Math.random()*(3+(Game.getCurDif().ordinal()))+1);
            System.out.println(Game.getCurDif());
            patient p = patient.genRandPat(Game.customerQ.size()+1);
            Game.customerQ.add(p);
            Game.customerS.add(0,p);

        }
        handleColldown(time);

    }

    private void handleColldown(double time)
    {
        
        if(Game.getPlayer().isSubmitting()&&submitCooldown>0)
        {
            submitCooldown-=time;
        }else
        {
            submitCooldown=submitCooldownTime;
            Game.getPlayer().resetSubmitting();
        }
    }

    /**
     * shows the time
     */
    public void show(GraphicsContext gc) {

        gc.stroke();
        gc.fill();
        gc.setFill(Colors.white);
        gc.fillRect(x, y, width, height);
        if (timeLeft / maxTime > 0.25)
            gc.setFill(Colors.green);
        else
            gc.setFill(Colors.red);
        gc.fill();
        gc.fillRect(x + border, y + border, (timeLeft / maxTime) * (width - border * 2), height - border * 2);
        // black bark if the places the above rectanlge no longer fills
        gc.setFill(Colors.black);
        gc.fill();
        gc.fillRect(x + border + (timeLeft / maxTime) * (width - border * 2), y + border,
        width - border * 2 - (timeLeft / maxTime) * (width - border * 2), height -
        border * 2);
    }

}
