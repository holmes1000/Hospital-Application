package edu.wpi.teamb.Game.Player;

public class TimeController {
    int timeLeft;
    int maxTime;

    public TimeController(int maxTime) {
        this.maxTime = maxTime;
        timeLeft = maxTime;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    private void subtractTime(int time) {
        timeLeft -= time;
    }
    private void addTime(int time) {
        if(timeLeft + time > maxTime)
            timeLeft = maxTime;
        else if(timeLeft + time < 0)
            timeLeft = 0;
        else
            timeLeft += time;
    }
}
