package edu.wpi.teamb.Game.PatientThings;

import javafx.scene.shape.Rectangle;

import java.util.Random;

import edu.wpi.teamb.Game.Gapp;
import edu.wpi.teamb.Game.Contollers.GameScnController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class patient {
    PatientNeed need;
    Image personimage;
    Image patientImage;
    int position;

    /*render */
    int x = 100, y = 250, w = 75, h = 75, positionOffest;


    double patients;

    public patient(PatientNeed need, double patients, int position) {
        this.need = need;
        setPatientImage(need);
        this.patients = patients;
        this.position = position;
        personimage = Gapp.personImages[0];
        positionOffest = position;

    }

    public static patient genRandPat(int position) {
        System.out.println("new Patient");
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
     * genrates a random amount of time the patient will wait
     * 
     * @return the amount of time the patient will wait
     */
    private static double randPatients() {
        return Math.random() * 10 + 10;

    }

    private void setPatientImage(PatientNeed pn) {
        // switch (pn) {
        //     case HEART -> patientImage = new Image("heart.png");
        //     case BROKEN_LIMB -> patientImage = new Image("brokenLimb.png");
        //     case HUNGRY -> patientImage = new Image("hungry.png");
        //     case NON_SEVERE -> patientImage = new Image("nonSevere.png");
        // }
    }

    public PatientNeed getNeed() {
        return need;
    }

    public int getPosition() {
        return position;
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
        patients -= amount / position;
    }

    public void update(double time) {
        //switch person image to the second one if patiets is below 3
        if (patients < 3) {
            personimage = Gapp.personImages[1];
        }
    }

    public void show(GraphicsContext gc) {
        //draw person image
        gc.drawImage(personimage, x-w*position, y,w,h);
    }

}
