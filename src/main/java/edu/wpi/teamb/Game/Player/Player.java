package edu.wpi.teamb.Game.Player;

import edu.wpi.teamb.Game.Game;
import edu.wpi.teamb.Game.Gapp;
import edu.wpi.teamb.Game.PatientThings.PatientNeed;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Player {
    PatientNeed slectedNeed;
    int score;
    /*Render */
    int x=120,y=250,w=75,h=75,border=5;
    private boolean submitting= false;



    public Player( int score, int timeLeft) {
        this.slectedNeed = PatientNeed.BROKEN_LIMB;
        this.score = score;
    }

    /**
     * sets the current need
     * @param need the need to set the current need to
     */
    public void setNeed(PatientNeed need) {
        this.slectedNeed = need;
    }

    public boolean isSubmitting() {
        return submitting;
    }
    public void resetSubmitting() {
        submitting = false;
    }


    /**
     * sets the score to a certain value
     * @param score the value to set the score to
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the current score
     * @return the current score
     */
    public int getScore() {
        return score;
    }



    /**
     * Adds the given amount to the score
     * @param score the amount to add to the score
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Changes the selected need by the given amount
     * @param change if negative lowers the need, if positive raises the need
     */
    public void changeSelectedNeed(int change) {
        //move patient need by 1 left if negative, right if positive
        //if move past end of enum, do not move
        int index = slectedNeed.ordinal();
        index += change;
        if(index < 0)
            index = 0;
        else if(index > PatientNeed.values().length - 1)
            index = PatientNeed.values().length - 1;
        slectedNeed = PatientNeed.values()[index];

    }

        /**
     * Returns the currently selected need
     * @return the currently selected need
     */
    public PatientNeed getSubmitNeed()
    {

        return slectedNeed;
    
    }

    public void submitNeed() {
        if(Game.timeController.submitCooldown!=Game.timeController.submitCooldownTime)
        {
            return;
        }
        submitting = true;
        if (Game.customerQ.peek() == null) {
            return;
        }
        if(slectedNeed==Game.customerQ.peek().getNeed())
        {
            Game.customersDone.add(Game.customerQ.removeFirst());
            Game.customerS.removeLast().fufilled();
            score++;
        }else{
            Game.customersDone.add(Game.customerQ.removeFirst());
            Game.customerS.removeLast().unFufilled();
        }
        Game.rePosition();

    }

    private int needtoInt()
    {
        return slectedNeed.ordinal();
    
    }

    public void update(double delta)
    {

    }

    public void show(GraphicsContext gc)
    {

        //orange rectange that move correponding to selected need
        gc.setFill(Paint.valueOf("orange"));
        if(submitting)
        {
            gc.setFill(Paint.valueOf("yellow"));
        }
        gc.clearRect(x, y, w*4+100, h);
        gc.fillRect(x+w*needtoInt()+25*needtoInt()-border, y-border, w+border*2, h+border*2);
//    TODO: draw images in stops corresponding to need
        gc.drawImage(Gapp.deskImages[0], x, y, w, h);
        gc.drawImage(Gapp.deskImages[1], x+w+25, y, w, h);
        gc.drawImage(Gapp.deskImages[2], x+w*2+50, y, w, h);
        gc.drawImage(Gapp.deskImages[3], x+w*3+75, y, w, h);
    

        
    }
}
