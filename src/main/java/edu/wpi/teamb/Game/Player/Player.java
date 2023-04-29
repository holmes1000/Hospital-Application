package edu.wpi.teamb.Game.Player;

import edu.wpi.teamb.Game.PatientThings.PatientNeed;

public class Player {
    PatientNeed slectedNeed;
    int score;


    public Player( int score, int timeLeft) {
        this.slectedNeed = PatientNeed.BROKEN_LIMB;
        this.score = score;
    }

    public void setNeed(PatientNeed need) {
        this.slectedNeed = need;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }



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

    public PatientNeed submitNeed() {
        return slectedNeed;
    }
}
