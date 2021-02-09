package ru.stqa.pft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

public class RestAssuredTests {

  @BeforeClass
  public void init() {
    RestAssured.authentication = RestAssured.basic("28accbe43ea112d9feb328d2c00b3eed", "");
  }

  @Test
  public void testCreateIssue() throws IOException {
    Set<Issue> oldIssues = getIssues();
    Issue newIssue = new Issue().withSubject("Test issue").withDescription("New description");
    int issueId = createdIssue(newIssue);
    Set<Issue> newIssues = getIssues();
    oldIssues.add(newIssue.withId(issueId));
    Assert.assertEquals(newIssues, oldIssues);
  }

  private Set<Issue> getIssues() throws IOException {
//    String json = getExecutor().execute(Request.Get("http://demo.bugify.com/api/issues.json"))
//            .returnContent().asString();
    String json =RestAssured.get("http://demo.bugify.com/api/issues.json").asString();
    JsonElement parsed = new JsonParser().parse(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
  }

//  private Executor getExecutor(){
//    return Executor.newInstance().auth("28accbe43ea112d9feb328d2c00b3eed", "");
//  }

  private int createdIssue(Issue newIssue) throws IOException {
//    String json = getExecutor().execute(Request.Post("http://demo.bugify.com/api/issues.json")
//            .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
//                    new BasicNameValuePair("description", newIssue.getDescription())))
//            .returnContent().asString();
    String json = RestAssured.given()
            .parameter("subject", newIssue.getSubject())
            .parameter("description", newIssue.getDescription())
            .post("http://demo.bugify.com/api/issues.json").asString();
    JsonElement parsed = new JsonParser().parse(json);
    return  parsed.getAsJsonObject().get("issue_id").getAsInt();
  }
}
