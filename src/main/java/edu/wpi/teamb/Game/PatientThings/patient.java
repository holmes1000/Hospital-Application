package edu.wpi.teamb.Game.PatientThings;

import java.util.Random;

import edu.wpi.teamb.Game.Gapp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

public class patient {
    PatientNeed need;
    Image personimage;
    Image patientImage;
    int position;

    /* render */
    int x = 250, y = 140, w = 75, h = 75, positionOffest;
    private int border = 13;

    double patients;

    public patient(PatientNeed need, double patients, int position) {
        this.need = need;
        setPatientImage(need);
        this.patients = patients;
        this.position = position;
        personimage = Gapp.personImages[0];
        positionOffest = 75;

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
        return Math.random() * 5 + 3;

    }

    private void setPatientImage(PatientNeed pn) {
        switch (pn) {
            case HEART -> patientImage = Gapp.patientImages[0];
            case BROKEN_LIMB -> patientImage = Gapp.patientImages[1];
            case HUNGRY -> patientImage = Gapp.patientImages[2];
            case NON_SEVERE -> patientImage = Gapp.patientImages[3];
        }
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
        patients -= amount * 2 / position;
    }

    public void update(double time) {
        decresePaditents(time);
        // switch person image to the second one if patiets is below 3
        if (personimage != Gapp.personImages[1] && patients < 3) {
            personimage = Gapp.personImages[1];
            System.out.println("HAHA");
        }
    }

    public void show(GraphicsContext gc) {
        // draw person image

        gc.drawImage(personimage, x - w / 2 * (position - 1), y, w, h);
        if (position == 1) {
            gc.setFill(Paint.valueOf("orange"));
            gc.fillOval(x + positionOffest, y - positionOffest, 100, 100);
            gc.drawImage(patientImage, x + positionOffest + border, y - positionOffest + border, 75, 75);
        }

    }

}
