package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import javafx.scene.image.Image;

public class FullFactory {
    public IFull getFullRequest(String requestType) {
        return switch (requestType) {
            case "Meal" -> new FullMealRequest();
            case "Conference" -> new FullConferenceRequest();
            case "Flower" -> new FullFlowerRequest();
            case "Office" -> new FullOfficeRequest();
            case "Furniture" -> new FullFurnitureRequest();
            case "Translation" -> new FullTranslationRequest();
            default -> null;
        };
    }
}
