package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  @Test
  public void postTweet() throws Exception {
    //test failed request
    String hashtag = "#testtag";
    String text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
    float lat = 1f;
    float lon = -1f;
    //exception is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.create(TwitterUtils.buildTweet(text, lon, lat));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(true);
    }

    String tweetJson = "{\n"
        + " \"created_at\":\"Mon Feb 18 21:23:39 +000 2019\",\n"
        + " \"id\":1097607853932564480,\n"
        + " \"id_str\":\"1097607853932564480\",\n"
        + " \"text\":\"test with loc223\",\n"
        + " \"entities\":{"
        + "   \"hashtag\":[],"
        + "   \"user_mentions\":[]"
        + " },\n"
        + " \"coordinates\":null,\n"
        + " \"retweet_count\":0,\n"
        + " \"favorite_count\":0,\n"
        + " \"favorited\":false,\n"
        + " \"retweeted\":false\n"
        + "}\n";

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = TwitterUtils.toObjectFromJson(tweetJson, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseEntityFromBody(any());
    Tweet tweet = spyDao.create(TwitterUtils.buildTweet(text, lon, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweet() throws Exception {
    //test failed request
    String id = "1097607853932564480";
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.deleteById(id);
      fail();
    } catch (RuntimeException ex) {
      assertTrue(true);
    }

    String tweetJson = "{\n"
        + " \"created_at\":\"Mon Feb 18 21:23:39 +000 2019\",\n"
        + " \"id\":1097607853932564480,\n"
        + " \"id_str\":\"1097607853932564480\",\n"
        + " \"text\":\"test with loc223\",\n"
        + " \"entities\":{"
        + "   \"hashtag\":[],"
        + "   \"user_mentions\":[]"
        + " },\n"
        + " \"coordinates\":null,\n"
        + " \"retweet_count\":0,\n"
        + " \"favorite_count\":0,\n"
        + " \"favorited\":false,\n"
        + " \"retweeted\":false\n"
        + "}\n";

    Tweet expectedTweet = TwitterUtils.toObjectFromJson(tweetJson, Tweet.class);
    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    doReturn(expectedTweet).when(spyDao).parseEntityFromBody(any());
    Tweet tweet = spyDao.deleteById(id);
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void showTweet() throws Exception {
    //test failed request
    String id = "1097607853932564480";
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.findById(id);
      fail();
    } catch (RuntimeException ex) {
      assertTrue(true);
    }

    String tweetJson = "{\n"
        + " \"created_at\":\"Mon Feb 18 21:23:39 +000 2019\",\n"
        + " \"id\":1097607853932564480,\n"
        + " \"id_str\":\"1097607853932564480\",\n"
        + " \"text\":\"test with loc223\",\n"
        + " \"entities\":{"
        + "   \"hashtag\":[],"
        + "   \"user_mentions\":[]"
        + " },\n"
        + " \"coordinates\":null,\n"
        + " \"retweet_count\":0,\n"
        + " \"favorite_count\":0,\n"
        + " \"favorited\":false,\n"
        + " \"retweeted\":false\n"
        + "}\n";

    Tweet expectedTweet = TwitterUtils.toObjectFromJson(tweetJson, Tweet.class);
    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    doReturn(expectedTweet).when(spyDao).parseEntityFromBody(any());
    Tweet tweet = spyDao.findById(id);
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }
}
