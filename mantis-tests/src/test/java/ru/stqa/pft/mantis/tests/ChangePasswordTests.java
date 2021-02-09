package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.DbHelper;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends TestBase {
private DbHelper dbHelper = new DbHelper();

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testChangePassword () throws IOException {
    String adminname = "administrator";
    String adminpw = "root";
    String password = "password";
    UserData notAdminUser = dbHelper.getNotAdminUser();
    String email = notAdminUser.getEmail();
    String username =notAdminUser.getUsername();
    
    app.passwordHelper().login(adminname, adminpw);
    app.passwordHelper().openManagePage(adminpw);
    app.passwordHelper().resetPassword(notAdminUser);
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 60000);
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.passwordHelper().finish(confirmationLink, password, username);
    assertTrue(app.newSession().login(username, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }

  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop();
  }
}
