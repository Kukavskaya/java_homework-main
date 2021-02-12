package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class DeleteGroupFromContactTest extends TestBase{

  @BeforeMethod
  public void ensurePreconditions(){
    if (app.db().contacts().size() == 0) {
      if (app.db().groups().size() > 0) {
        Groups groups = app.db().groups();
        ContactData newContact = new ContactData().withFirstname("user1").withLastname("user1").withAddress("address1")
                .inGroups(groups.iterator().next());
        app.goTo().homePage();
        app.contact().create(newContact, true);
      } else {
        app.goTo().GroupPage();
        GroupData group = new GroupData().withName("test1");
        app.group().create(group);
        ContactData newContact = new ContactData().withFirstname("user1").withLastname("user1").withAddress("address1")
                .inGroups(group);
        app.goTo().homePage();
        app.contact().create(newContact, true);
      }
    }

      GroupData groupToAdd = app.db().groups().stream()
              .filter((g) -> g.getContacts().size() > 0)
              .findFirst().get();
      app.goTo().homePage();
      app.contact().addGroupToContact(groupToAdd);
  }

  @Test
  public void testDeleteGroupFromContact() {
    Groups groups = getGroupDataStream();

    long before = groups.stream()
            .filter((g) -> g.getContacts().size() > 0).count();

    GroupData groupToDelete = groups.stream()
            .filter((g) -> g.getContacts().size() > 0)
            .findFirst()
            .orElseThrow(NoSuchElementException::new);

    app.goTo().homePage();
    app.contact().deleteGroupFromContact(groupToDelete);

    long after = getGroupDataStream().stream()
            .filter((g) -> g.getContacts().size() > 0).count();
    assertEquals(after, before - 1);

  }

  private Groups getGroupDataStream() {
    return app.db().groups();
  }
}
