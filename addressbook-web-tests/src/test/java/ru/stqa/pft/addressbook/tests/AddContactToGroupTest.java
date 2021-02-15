package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static org.testng.Assert.assertEquals;

public class AddContactToGroupTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    ensureContactExists();
    ensureGroupExists();
    ensureNotAllContactsInAllGroups();
  }

  @Test
  public void testAddContactToGroup() {
    Groups groups = app.db().groups();

    Contacts contacts = app.db().contacts();

    ContactData testContact = contacts.stream().filter((c) -> c.getGroups().size() != groups.size())
            .findFirst()
            .orElseThrow(NoSuchElementException::new);

    int before = testContact.getGroups().size();

    GroupData groupToAdd = groups.stream()
            .filter((g) -> !g.getContacts().contains(testContact))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);

    app.goTo().homePage();
    app.contact().addGroupToContact(groupToAdd, testContact);



    int after = app.db().contacts().stream()
            .filter(c -> c.equals(testContact))
            .findAny()
            .orElseThrow(NoSuchElementException::new)
            .getGroups()
            .size();

    assertEquals(after, before + 1);
  }



  public void ensureNotAllContactsInAllGroups(){
    Contacts contacts = app.db().contacts();
    boolean allContactsInAllGroups = contacts.stream().allMatch(this::isContactPresentInAllGroups);
    if(allContactsInAllGroups){
      app.goTo().GroupPage();
      GroupData group = new GroupData().withName("test"+ ThreadLocalRandom.current().nextInt(0, 100));
      app.group().create(group);
    }

  }

  public boolean isContactPresentInAllGroups (ContactData contact) {
    int groupsTotal = app.db().groups().size();
    return contact.getGroups().size() == groupsTotal;
  }
}
