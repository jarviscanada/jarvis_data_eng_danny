package ca.jrvs.apps.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  private TwitterDao dao;
  private TwitterService service;

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
  }

  @Test
  public void mainTest() {
    String status = "test service post " + System.currentTimeMillis();
    Float longitude = -2.34f;
    Float latitude = 1.45f;

    Tweet testPostTweet = service.postTweet(TwitterUtils.buildTweet(status, latitude, longitude));
    assertNotNull(testPostTweet);
    assertEquals(status, testPostTweet.getText());

    String validId = testPostTweet.getIdStr();
    String[] fields = {"id_str", "text"};

    Tweet testShowTweet = service.showTweet(validId, fields);
    assertNotNull(testShowTweet);
    assertEquals(validId, testShowTweet.getIdStr());
    assertNotNull(testShowTweet.getText());
  }
}
