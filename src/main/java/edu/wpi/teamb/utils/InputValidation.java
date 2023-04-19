package edu.wpi.teamb.utils;

public class InputValidation {
  public static boolean isDateValid(String date) {
    return date.matches(
        "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) (0?[1-9]|[12][0-9]|3[01]), (19|20)\\d{2}$");
  }
}
