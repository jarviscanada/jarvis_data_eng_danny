package ca.jrvs.apps.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private TwitterDao dao;
  private TwitterService service;
  private TwitterController controller;

  @Before
  public void setTestEnv() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    this.dao = new TwitterDao(httpHelper);
    this.service = new TwitterService(this.dao);
    this.controller = new TwitterController(this.service);
  }

  @Test
  public void mainTest() throws Exception {
    String request = "post";
    String tweetText = "test 1";
    String hashtag = "#testtag";
    String status = tweetText + " " + hashtag + " " + System.currentTimeMillis();
    Float latitude = 1.45f;
    Float longitude = -2.34f;
    String coords = latitude.toString() + ":" + longitude.toString();
    String[] args = {request, status, coords};

    Tweet response = controller.postTweet(args);
    System.out.println(TwitterUtils.toJson(response, true, false));
    assertNotNull(response);
    assertEquals(status, response.getText());
    assertEquals(longitude, response.getCoordinates().getCoordinates()[0]);
    assertEquals(latitude, response.getCoordinates().getCoordinates()[1]);
  }
}
