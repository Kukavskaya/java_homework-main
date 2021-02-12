package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;

public class AddContactToGroupTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    if ( app.db().contacts().size() == 0 ) {
      ContactData newContact = new ContactData().withFirstname("user1").withLastname("user1").withAddress("address1");
      app.goTo().homePage();
      app.contact().create(newContact, true);
    }
    if ( app.db().groups().size() == 0 ) {
      app.goTo().GroupPage();
      GroupData group = new GroupData().withName("test1");
      app.group().create(group);
    }
  }

  @Test
  public void testDeleteGroupFromContact() {
    Groups groups = getGroupDataStream();

    long before = groups.stream()
            .filter((g) -> g.getContacts().size() > 0).count();

    GroupData groupToAdd = groups.stream()
            .filter((g) -> g.getContacts().size() == 0)
            .findFirst()
            .orElseThrow(NoSuchElementException::new);

    app.goTo().homePage();
    app.contact().addGroupToContact(groupToAdd);

    long after = getGroupDataStream().stream()
            .filter((g) -> g.getContacts().size() > 0).count();
    assertEquals(after, before + 1);

  }

  private Groups getGroupDataStream() {
    return app.db().groups();
  }
}
