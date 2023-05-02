package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;

public class EForgotPassword {

  public void sendForgotPasswordEmail(String toEmailAddress) {
    EEmail emailE = EEmail.getEEmail();
    emailE.sendMail(
            toEmailAddress,
            "Hospital Application Reset Password Request",
            "You have requested to reset your password. Here is your current password: "  +
                    Repository.getRepository().getUserByEmail(toEmailAddress).getPassword() + "\n\n" +
                    "If you did not request to reset your password, please contact the hospital administration immediately." + "\n\n" +
                    "Thank you,\n" + "The Hospital Application Team");
  }
}
