package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;

public class DeleteGroupFromContactTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    ensureGroupExists();
    ensureContactExists();
    ensureContactInGroupExists();
  }

  @Test
  public void testDeleteGroupFromContact() {
    Groups groups = getGroupDataStream();

    long before = groups.stream()
            .filter((g) -> g.getContacts().size() > 0).count();

    GroupData groupToDelete = groups.stream()           //select group with added contact
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
