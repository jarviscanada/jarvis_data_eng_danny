package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;

public class TwitterUtils {
  public static Tweet buildTweet(String status) {
    Tweet tweet = new Tweet();
    tweet.setText(status);
    return tweet;
  }

  public static Tweet buildTweet(String status, float longitude, float latitude) {
    Tweet tweet = new Tweet();
    tweet.setText(status);

    float[] coordinateValues = {longitude, latitude};
    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(coordinateValues);
    tweet.setCoordinates(coordinates);
    return tweet;
  }

  public static <T> T toObjectFromJson(String json, Class paramClass) throws IOException {
    ObjectMapper m = new ObjectMapper();
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) m.readValue(json, paramClass);
  }

  public static String escapeText(String text) {
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    return percentEscaper.escape(text);
  }
}
