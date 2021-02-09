package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.stqa.pft.mantis.model.UserData;

public class PasswordHelper extends HelperBase{

  public PasswordHelper(ApplicationManager app) {
    super(app);
  }

  public void login (String username, String password) {
    wd.get(app.getProperty("web.baseUrl") + "login_page.php");
    type(By.name("username"), username);
    click(By.xpath("//input[@value='Войти']"));
    type(By.name("password"), password);
    click(By.xpath("//input[@value='Войти']"));
  }

  public void openManagePage(String password) {
    click(By.xpath("//div[@id='sidebar']//span[text()=' Управление ']"));
//    type(By.name("password"), password);
    click(By.xpath("//div[@class='main-container']//a[text()='Управление пользователями']"));
  }

  public void resetPassword (UserData userData) {
    String username = userData.getUsername();
    type(By.id("search"), username);
    click(By.xpath("//input[@value='Фильтровать']"));
    click(By.xpath(String.format("//div[@class='table-responsive']//a[text()='%s']", username)));
    click(By.xpath("//input[@value='Сбросить пароль']"));
    click(By.linkText("Продолжить"));
  }

  public void finish(String confirmationLink, String userpw, String username) {
    wd.get(confirmationLink);
    type(By.id("realname"), username);
    type(By.id("password"), userpw);
    type(By.id("password-confirm"), userpw);
    click(By.xpath("//span[contains(text(), 'Изменить пользователя')]"));
  }
}
