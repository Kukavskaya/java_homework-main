package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTest extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContacts() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contact.json")))) {
      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }
      Gson gson = new Gson();
      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>(){}.getType());
      return contacts.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
    }
  }

  @Test (dataProvider = "validContacts")
  public void testContactCreation(ContactData contact) {
    Contacts before = app.db().contacts();
    app.goTo().homePage();
    int index = before.size() + 1;
//    File photo = new File("src/test/resources/image.png");
//    ContactData contact = new ContactData().withFirstname("user1").withLastname("user2").withAddress("address1")
//            .withTelephone("+79999999999").withEmail("testuser@mail.ru").withPhoto(photo);
    app.contact().create(contact, true);
    Contacts after = app.db().contacts();
    assertThat(after.size(), equalTo(index));
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

//    app.Logout();
  }

  @Test (enabled = false)
  public void testBadContactCreation() {
    app.goTo().homePage();
    Contacts before = app.contact().all();
    ContactData contact = new ContactData().withFirstname("user1'").withLastname("user2").withAddress("address1").withTelephone("+79999999999").withEmail("testuser@mail.ru");
    app.contact().create(contact, true);
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.contact().all();
//    Assert.assertEquals(after.size(), before.size());
    assertThat(after, equalTo(before));

//    app.Logout();
  }

  @Test (enabled = false)
  public void testCurrentDir () {
    File currentDir = new File(".");
    System.out.println(currentDir.getAbsolutePath());
    File photo = new File("src/test/resources/image.png");
    System.out.println(photo.getAbsolutePath());
    System.out.println(photo.exists());
  }
}
