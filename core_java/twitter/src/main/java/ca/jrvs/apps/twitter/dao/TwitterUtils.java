package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;

public class TwitterUtils {

  /**
   * Construct tweet object with only status text
   *
   * @param status tweet text
   * @return constructed Tweet object
   */
  public static Tweet buildTweet(String status) {
    Tweet tweet = new Tweet();
    tweet.setText(status);
    return tweet;
  }

  /**
   * Construct tweet object with status and coordinates
   *
   * @param status    tweet text
   * @param latitude
   * @param longitude
   * @return constructed Tweet object
   */
  public static Tweet buildTweet(String status, Float latitude, Float longitude) {
    Tweet tweet = new Tweet();
    tweet.setText(status);

    Float[] coordinateValues = {longitude, latitude};
    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(coordinateValues);
    tweet.setCoordinates(coordinates);
    return tweet;
  }

  /**
   * Takes a JSON string and maps it according to provided class' fields
   *
   * @param json       JSON string to be mapped
   * @param paramClass annotated class to be mapped to
   * @param <T>        generic object template
   * @return mapped Tweet object
   * @throws IOException
   */
  public static <T> T toObjectFromJson(String json, Class paramClass) throws IOException {
    ObjectMapper m = new ObjectMapper();
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) m.readValue(json, paramClass);
  }

  /**
   * Convert object to JSON-format string
   *
   * @param object            object to be parsed
   * @param prettyJson        whether or not to indent output
   * @param includeNullValues whether or not to include nulled fields in string
   * @return string representation of object
   * @throws JsonProcessingException
   */
  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper m = new ObjectMapper();
    if (!includeNullValues) {
      m.setSerializationInclusion(Include.NON_NULL);
    }
    if (prettyJson) {
      m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    return m.writeValueAsString(object);
  }

  /**
   * Escape special characters in text to prevent errors
   *
   * @param text
   * @return escaped text
   */
  public static String escapeText(String text) {
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    return percentEscaper.escape(text);
  }
}
