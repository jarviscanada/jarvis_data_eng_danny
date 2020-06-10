package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  Service mockService;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweetTest() throws Exception {
    String request = "post";
    String text = "controller test " + System.currentTimeMillis();
    Float lat = 0.45f;
    Float lon = 1.25f;
    String coordString = lat + ":" + lon;
    String[] args = {request, text, coordString};
    //test failed post
    when(mockService.postTweet(isNotNull())).thenThrow(
        new IllegalArgumentException("mock invalid arg"));
    try {
      controller.postTweet(args);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    TwitterController spyController = Mockito.spy(controller);
    Tweet expectedTweet = TwitterUtils.buildTweet(text, lat, lon);
    doReturn(expectedTweet).when(spyController).postTweet(any());
    Tweet resultTweet = spyController.postTweet(args);

    assertNotNull(resultTweet);
    assertEquals(text, resultTweet.getText());
    assertEquals(lat, resultTweet.getCoordinates().getCoordinates()[1]);
    assertEquals(lon, resultTweet.getCoordinates().getCoordinates()[0]);
  }

  @Test
  public void showTweetTest() throws Exception {
    String request = "show";
    String id = "1097607853932564480";
    String fields = "id_str,text";
    String[] args = {request, id, fields};
    //test failed show request
    when(mockService.showTweet(anyString(), any())).thenThrow(
        new IllegalArgumentException("mock invalid args"));
    try {
      controller.showTweet(args);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    String tweetJson = "{\n"
        + " \"id_str\":\"1097607853932564480\",\n"
        + " \"text\":\"testing non-null on show\"\n"
        + "}";

    Tweet expectedTweet = TwitterUtils.toObjectFromJson(tweetJson, Tweet.class);
    TwitterController spyController = Mockito.spy(controller);
    doReturn(expectedTweet).when(spyController).showTweet(any());
    Tweet resultTweet = spyController.showTweet(args);

    assertNotNull(resultTweet);
    assertNotNull(resultTweet.getIdStr());
    assertNotNull(resultTweet.getText());
    assertNull(resultTweet.getCoordinates());
    assertNull(resultTweet.getCreatedAt());
  }

  @Test
  public void deleteTweetTest() throws Exception {
    String request = "delete";
    String ids = "1097607853932564480,1097607853932564481";
    String[] args = {request, ids};
    when(mockService.deleteTweets(isNotNull())).thenThrow(
        new IllegalArgumentException("mock invalid args"));
    try {
      controller.deleteTweet(args);
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    }

    String tweetJson1 = "{\n"
        + "\"id_str\":\"1097607853932564480\",\n"
        + "\"text\":\"test1\"}";
    String tweetJson2 = "{\n"
        + "\"id_str\":\"1097607853932564481\",\n"
        + "\"text\":\"test2\"}";

    Tweet expectedTweet1 = TwitterUtils.toObjectFromJson(tweetJson1, Tweet.class);
    Tweet expectedTweet2 = TwitterUtils.toObjectFromJson(tweetJson2, Tweet.class);
    List<Tweet> expectedTweets = new ArrayList<Tweet>();
    expectedTweets.add(expectedTweet1);
    expectedTweets.add(expectedTweet2);
    TwitterController spyController = Mockito.spy(controller);
    doReturn(expectedTweets).when(spyController).deleteTweet(any());
    List<Tweet> resultTweets = spyController.deleteTweet(args);

    assertNotNull(resultTweets);
    assertEquals("1097607853932564480", resultTweets.get(0).getIdStr());
    assertEquals("1097607853932564481", resultTweets.get(1).getIdStr());
  }
}
