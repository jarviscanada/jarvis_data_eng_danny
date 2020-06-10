package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  TwitterDao mockDao;

  @InjectMocks
  TwitterService service;

  @Test
  public void showTweetTest() throws Exception {
    String id = "1097607853932564480";
    String[] fields = {"id_str", "text"};
    //test failed request
    when(mockDao.findById(anyString())).thenThrow(new IllegalArgumentException("mock illegal arg"));
    try {
      service.showTweet(id, fields);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    String tweetJson = "{\n"
        + "\"id_str\":\"1097607853932564480\",\n"
        + "\"text\":\"@TwitterAPI TREMENDOUS progress we are making here twitter.\"\n"
        + "}";

    Tweet expectedTweet = TwitterUtils.toObjectFromJson(tweetJson, Tweet.class);
    TwitterService spyService = Mockito.spy(service);
    doReturn(expectedTweet).when(spyService).showTweet(any(), any());
    Tweet resultTweet = spyService.showTweet(id, fields);

    assertNotNull(resultTweet);
    assertEquals(resultTweet.getIdStr(), "1097607853932564480");
    assertEquals(resultTweet.getText(),
        "@TwitterAPI TREMENDOUS progress we are making here twitter.");
  }

  @Test
  public void postTweetTest() throws Exception {
    String status = "test service post " + System.currentTimeMillis();
    Float longitude = -2.34f;
    Float latitude = 1.45f;
    //test failed request
    when(mockDao.create(isNotNull())).thenThrow(new IllegalArgumentException("mock illegal arg"));
    try {
      service.postTweet(TwitterUtils.buildTweet(status, longitude, latitude));
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    Tweet expectedTweet = TwitterUtils.buildTweet(
        "@TwitterAPI TREMENDOUS progress we are making here twitter.",
        1.45f, -2.34f);
    TwitterService spyService = Mockito.spy(service);
    doReturn(expectedTweet).when(spyService).postTweet(any());
    Tweet resultTweet = spyService.postTweet(expectedTweet);

    assertNotNull(resultTweet);
    assertEquals(resultTweet.getText(),
        "@TwitterAPI TREMENDOUS progress we are making here twitter.");
    assertEquals(resultTweet.getCoordinates().getCoordinates()[1], 1.45f, 0.0f);
    assertEquals(resultTweet.getCoordinates().getCoordinates()[0], -2.34f, 0.0f);
  }

  @Test
  public void deleteTweetTest() throws Exception {
    String[] ids = {"1269462845625896961", "1269462620102426625"};
    //test failed request
    when(mockDao.deleteById(isNotNull()))
        .thenThrow(new IllegalArgumentException("mock illegal arg"));
    try {
      service.deleteTweets(ids);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    String tweetJson1 = "{\n"
        + "\"id_str\":\"1269462845625896961\",\n"
        + "\"text\":\"test1\"}";
    String tweetJson2 = "{\n"
        + "\"id_str\":\"1269462620102426625\",\n"
        + "\"text\":\"test2\"}";

    Tweet expectedTweet1 = TwitterUtils.toObjectFromJson(tweetJson1, Tweet.class);
    Tweet expectedTweet2 = TwitterUtils.toObjectFromJson(tweetJson2, Tweet.class);
    List<Tweet> expectedTweets = new ArrayList<Tweet>();
    expectedTweets.add(expectedTweet1);
    expectedTweets.add(expectedTweet2);

    TwitterService spyService = Mockito.spy(service);
    doReturn(expectedTweets).when(spyService).deleteTweets(any());
    List<Tweet> resultTweets = spyService.deleteTweets(ids);
    Tweet result1 = resultTweets.get(0);
    Tweet result2 = resultTweets.get(1);

    assertNotNull(result1);
    assertNotNull(result2);
    assertEquals(expectedTweet1.getIdStr(), result1.getIdStr());
    assertEquals(expectedTweet2.getIdStr(), result2.getIdStr());
  }
}
