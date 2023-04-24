package edu.wpi.teamb.utils;

public class TimeFormattingHelpers {
  public static boolean isDateValid(String date) {
    return date.matches(
        "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) (0?[1-9]|[12][0-9]|3[01]), (19|20)\\d{2}$");
  }

  public static int get24to12Hour(int hour) {
    if (hour > 12) {
      return hour - 12;
    } else if (hour == 0) {
      return 12;
    } else {
      return hour;
    }
  }
}
