package ca.jrvs.apps.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao dao;

  @Before
  public void setTestEnv() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    this.dao = new TwitterDao(httpHelper);
  }

  @Test
  public void mainTest() {
    String tweetText = "test 1";
    String hashtag = "#testtag";
    String status = tweetText + " " + hashtag + " " + System.currentTimeMillis();
    Float longitude = -2.56f;
    Float latitude = 1.28f;

    Tweet tweet = dao.create(TwitterUtils.buildTweet(
        status, latitude, longitude));

    assertEquals(status, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().length);
    assertEquals(latitude, tweet.getCoordinates().getCoordinates()[1], 0.0f);
    assertEquals(longitude, tweet.getCoordinates().getCoordinates()[0], 0.0f);
    assertTrue(hashtag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }
}
