package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy/";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;
  private PercentEscaper percentEscaper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet entity) {
    URI uri = createPostURI(entity);
    HttpResponse response = httpHelper.httpPost(uri);

    return parseEntityFromBody(response);
  }

  private URI createPostURI(Tweet tweet) {
    String status = TwitterUtils.escapeText(tweet.getText());
    if (tweet.getCoordinates() != null) {
      return URI.create(API_BASE_URI + POST_PATH + QUERY_SYM +
          "status" + EQUAL + status + AMPERSAND +
          "lat" + EQUAL + tweet.getCoordinates().getCoordinates()[0] + AMPERSAND +
          "long" + EQUAL + tweet.getCoordinates().getCoordinates()[1]);
    } else {
      return URI.create(API_BASE_URI + POST_PATH + QUERY_SYM +
          "status" + EQUAL + status);
    }
  }

  @Override
  public Tweet findById(String id) {
    URI uri = URI.create(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id=" + id);
    HttpResponse response = httpHelper.httpGet(uri);

    return parseEntityFromBody(response);
  }

  @Override
  public Tweet deleteById(String id) {
    URI uri = URI.create(API_BASE_URI + DELETE_PATH + id + ".json");
    HttpResponse response = httpHelper.httpGet(uri);

    return parseEntityFromBody(response);
  }

  public Tweet parseEntityFromBody(HttpResponse response) {
    Tweet tweet = null;
    String jsonString;
    int responseStatus = response.getStatusLine().getStatusCode();
    if (responseStatus != HTTP_OK) {
      throw new RuntimeException("Unexpected response status: " + responseStatus);
    }

    try {
      jsonString = EntityUtils.toString(response.getEntity());
      System.out.println("JSON: " + jsonString);
    } catch (IOException ioe) {
      throw new RuntimeException("Error converting response entity to String", ioe);
    }

    try {
      tweet = TwitterUtils.toObjectFromJson(jsonString, Tweet.class);
    } catch (IOException ioe) {
      throw new RuntimeException("Error mapping JSON String to object", ioe);
    }
    return tweet;
  }
}
