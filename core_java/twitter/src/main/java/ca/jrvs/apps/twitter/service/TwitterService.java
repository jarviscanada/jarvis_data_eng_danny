package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

  private final CrdDao dao;
  private final int CHARACTER_LIMIT = 280;
  private final int ID_LENGTH = 19;
  private final float LAT_MAX = 90f;
  private final float LAT_MIN = -90f;
  private final float LON_MAX = 180f;
  private final float LON_MIN = -180f;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  /**
   * Validate and post a user input Tweet
   *
   * @param tweet tweet to be created
   * @return created tweet
   * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long
   *                                  out of range
   */
  @Override
  public Tweet postTweet(Tweet tweet) {
    validatePostTweet(tweet);
    return (Tweet) dao.create(tweet);
  }

  /**
   * Search a tweet by ID
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {
    validateGetTweet(id);
    Tweet tweet = (Tweet) dao.findById(id);
    Method[] methods = Tweet.class.getMethods();
    for (Method m : methods) {
      if (m.getName().startsWith("set") && m.getDeclaredAnnotations().length > 0) {
        Annotation annotation = m.getAnnotation(JsonProperty.class);
        String annotationStr = ((JsonProperty) annotation).value();
        if (!Arrays.asList(fields).contains(annotationStr)) {
          try {
            m.invoke(tweet, new Object[]{null});
          } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
          }
        }
      }
    }
    return tweet;
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> tweets = new ArrayList<Tweet>();
    for (String id : ids) {
      validateGetTweet(id);
      tweets.add((Tweet) dao.deleteById(id));
    }
    return tweets;
  }

  /**
   * Validate that all Tweet fields comply with Twitter constraints
   *
   * @param tweet
   * @throws IllegalArgumentException
   */
  private void validatePostTweet(Tweet tweet) throws IllegalArgumentException {
    String text = tweet.getText();
    float lat = tweet.getCoordinates().getCoordinates()[1];
    float lon = tweet.getCoordinates().getCoordinates()[0];

    if (text.length() > CHARACTER_LIMIT) {
      throw new IllegalArgumentException("Character limit exceeded.");
    }
    if (lat > LAT_MAX || lat < LAT_MIN) {
      throw new IllegalArgumentException("Latitude value out of bounds.");
    }
    if (lon > LON_MAX || lon < LON_MIN) {
      throw new IllegalArgumentException("Longitude value out of bounds");
    }
  }

  private void validateGetTweet(String tweetId) throws IllegalArgumentException {
    String idRegex = "\\d{" + ID_LENGTH + "}";
    if (!tweetId.matches(idRegex)) {
      throw new IllegalArgumentException("Invalid Tweet id format");
    }
  }
}
