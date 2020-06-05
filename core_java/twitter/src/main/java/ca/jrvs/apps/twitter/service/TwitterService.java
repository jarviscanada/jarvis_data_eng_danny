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

public class TwitterService implements Service {

  private CrdDao dao;
  private final int CHARACTER_LIMIT = 280;
  private final int ID_LENGTH = 19;
  private final float LAT_MAX = 90f;
  private final float LAT_MIN = -90f;
  private final float LON_MAX = 180f;
  private final float LON_MIN = -180f;

  public TwitterService(CrdDao dao) {this.dao = dao;}

  @Override
  public Tweet postTweet(Tweet tweet) {
    validatePostTweet(tweet);
    return (Tweet) dao.create(tweet);
  }

  @Override
  public Tweet showTweet(String id, String[] fields) throws IllegalArgumentException {
    validateGetTweet(id);
    Tweet tweet = (Tweet) dao.findById(id);
    Method[] methods = Tweet.class.getMethods();
    for (Method m: methods) {
      if (m.getName().startsWith("set") && m.getDeclaredAnnotations().length > 0) {
        Annotation annotation = m.getAnnotation(JsonProperty.class);
        String annotationStr = ((JsonProperty)annotation).value();
        if (!Arrays.asList(fields).contains(annotationStr)) {
          try {
            if (m.getParameterTypes()[0].equals(String.class)) {
              m.invoke(tweet, new Object[]{null});
            } else if (m.getParameterTypes()[0].equals(boolean.class)){
              m.invoke(tweet, false);
            } else if (m.getParameterTypes()[0].equals(int.class)){
              m.invoke(tweet, 0);
            }
          } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
          }
        }
      }
    }
    return tweet;
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> tweets = new ArrayList<Tweet>();
    for(String id: ids) {
      validateGetTweet(id);
      tweets.add((Tweet)dao.deleteById(id));
    }
    return tweets;
  }

  private void validatePostTweet(Tweet tweet) throws IllegalArgumentException {
    String text = tweet.getText();
    float lat = tweet.getCoordinates().getCoordinates()[0];
    float lon = tweet.getCoordinates().getCoordinates()[1];

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
