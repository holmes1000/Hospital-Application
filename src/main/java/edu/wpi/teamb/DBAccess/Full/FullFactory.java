package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import javafx.scene.image.Image;

public class FullFactory {
    public IFull getFullRequest(String requestType) {
        switch (requestType) {
            case "Meal":
                return new FullMealRequest();
            case "Conference":
                return new FullConferenceRequest();
            case "Flower":
                return new FullFlowerRequest();
            case "Office":
                return new FullOfficeRequest();
            case "Furniture":
                return new FullFurnitureRequest();
            default:
                return null;
        }
    }
}
