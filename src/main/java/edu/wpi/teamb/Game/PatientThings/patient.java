package edu.wpi.teamb.Game.PatientThings;

import javafx.scene.image.Image;

public class patient {
    PatientNeed need;
    Image patientImage;

    int patients;

    public patient(PatientNeed need, int patients) {
        this.need = need;
        setPatientImage(need);
        this.patients = patients;
    }

    private void setPatientImage(PatientNeed pn) {
        switch (pn) {
            case HEART -> patientImage = new Image("heart.png");
            case BROKEN_LIMB -> patientImage = new Image("brokenLimb.png");
            case HUNGRY -> patientImage = new Image("hungry.png");
            case NON_SEVERE -> patientImage = new Image("nonSevere.png");
        }
    }

    public PatientNeed getNeed() {
        return need;
    }

    public Image getPatientImage() {
        return patientImage;
    }

    public int getPatients() {
        return patients;
    }

    public void setPatients(int patients) {
        this.patients = patients;
    }

    public void decresePaditents(int amount) {
        patients -= amount;
    }


}
