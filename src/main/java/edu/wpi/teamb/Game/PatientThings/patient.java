package edu.wpi.teamb.Game.PatientThings;

import java.util.Random;

import edu.wpi.teamb.Game.Game;
import edu.wpi.teamb.Game.Gapp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class patient {

    /* patient state */
    int position;
    PatientNeed need;
    boolean happy = true;
    boolean dequeing = false;
    double patients;
    double initPat;

    /* render */
    int x = 250, y = 140, w = 75, h = 75, positionOffest;
    private int needBorder = 13;
    Image personimage;
    Image patientImage;

    public patient(PatientNeed need, double patients, int position) {
        this.need = need;
        setPatientImage(need);
        this.patients = patients;
        this.initPat = patients;
        this.position = position;
        personimage = Gapp.personImages[0];
        positionOffest = 75;

    }

    public static patient genRandPat(int position) {
        return new patient(RandomNeed(), randPatients(), position);
    }

    private static PatientNeed RandomNeed() {
        Random rand = new Random();
        int n = rand.nextInt(4);
        switch (n) {
            case 0:
                return PatientNeed.HEART;
            case 1:
                return PatientNeed.BROKEN_LIMB;
            case 2:
                return PatientNeed.HUNGRY;
            case 3:
                return PatientNeed.NON_SEVERE;
            default:
                return PatientNeed.NON_SEVERE;
        }

    }

    /**
     * genrates a random amount of time the patient will wait between 5 and 10 minus one for the curent difficulty
     * 
     * @return the amount of time the patient will wait
     */
    private static double randPatients() {
        return Math.random() * 5 + 5-Game.getCurDif().ordinal();

    }

    public int getX() {
        return x;
    }

    public void setX(int l) {
        x = l;
    }

    public boolean isDequeing() {
        return dequeing;
    }

    private void setPatientImage(PatientNeed pn) {
        switch (pn) {
            case HEART -> patientImage = Gapp.patientImages[0];
            case BROKEN_LIMB -> patientImage = Gapp.patientImages[1];
            case HUNGRY -> patientImage = Gapp.patientImages[2];
            case NON_SEVERE -> patientImage = Gapp.patientImages[3];
        }
    }

    // used if their need is submitted corectly
    public void fufilled() {
        position = 0;
        Game.timeController.addTime(patients);
        Game.getPlayer().addScore(1);
        happy = true;
        dequeing = true;
    }

    // used if their need is not submitted corectly
    public void unFufilled() {
        position = 0;
        Game.timeController.subtractTime(patients);
        happy = false;
        dequeing = true;

    }

    public PatientNeed getNeed() {
        return need;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int i) {
        position = i;
    }

    public void updatePosition() {
        position--;
    }

    public Image getPatientImage() {
        return patientImage;
    }

    public double getPatients() {
        return patients;
    }

    public void setPatients(int patients) {
        this.patients = patients;
    }

    public void decresePaditents(double amount) {
        patients -= amount * 2 / position;
    }

    public void update(double time) {
        if (!dequeing) {
            decresePaditents(time);
            if (happy && patients < 3) {
                personimage = Gapp.personImages[1];
                happy = false;
            }
            if (shouldDequeue()) {
                dequeing = true;
                if (Game.customerS.contains(this)) {
                    Game.customerQ.remove(this);
                    Game.customerS.remove(this);
                    Game.customersDone.add(this);
                    position = 0;
                    //Game.rePosition();
                    Game.timeController.subtractTime(initPat/2);
                }
            }
        } else {
            dequeueing();
        }

    }

    public void show(GraphicsContext gc) {

        gc.drawImage(personimage, x - w / 2 * (position - 1), y, w, h);
        if (position == 1) {
            gc.setFill(Paint.valueOf("orange"));
            gc.fillOval(x + positionOffest, y - positionOffest, 100, 100);
            gc.drawImage(patientImage, x + positionOffest + needBorder, y - positionOffest + needBorder, 75, 75);
        }
        gc.setFont(new Font(30));
       gc.fillText(position + "", x - w / 2 * (position - 1), y, h);

    }

    /**
     * checks if the patient will deque becuase they ran out of time
     * 
     * @return
     */
    private boolean shouldDequeue() {

        return patients <= 0;
    }

    // moves location if dequeing
    public void dequeueing() {
        // move right
        if (dequeing||position==0) {
            if (happy) {
                // change image to happy
                personimage = Gapp.personImages[0];
            } else {
                personimage = Gapp.personImages[2];
                y-=10;
            }
            y-=5;
            x += 10;
        }
    }

}
