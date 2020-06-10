package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
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

  private final HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   *
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    URI uri = createPostURI(entity);
    HttpResponse response = httpHelper.httpPost(uri);

    return parseEntityFromBody(response);
  }

  /**
   * Assemble URI for posting Tweet using object fields
   *
   * @param tweet
   * @return completed URI
   */
  private URI createPostURI(Tweet tweet) {
    //assemble URI from Tweet fields
    String status = TwitterUtils.escapeText(tweet.getText());
    if (tweet.getCoordinates() != null) {
      return URI.create(API_BASE_URI + POST_PATH + QUERY_SYM +
          "status" + EQUAL + status + AMPERSAND +
          "lat" + EQUAL + tweet.getCoordinates().getCoordinates()[1] + AMPERSAND +
          "long" + EQUAL + tweet.getCoordinates().getCoordinates()[0]);
    } else {
      return URI.create(API_BASE_URI + POST_PATH + QUERY_SYM +
          "status" + EQUAL + status);
    }
  }

  /**
   * Find an entity(Tweet) by its id
   *
   * @param id entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(String id) {
    URI uri = URI.create(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id=" + id);
    HttpResponse response = httpHelper.httpGet(uri);

    return parseEntityFromBody(response);
  }

  /**
   * Delete an entity(Tweet) by its ID
   *
   * @param id of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(String id) {
    URI uri = URI.create(API_BASE_URI + DELETE_PATH + id + ".json");
    HttpResponse response = httpHelper.httpPost(uri);

    return parseEntityFromBody(response);
  }

  /**
   * Parse JSON entity and map according to Tweet object fields
   *
   * @param response
   * @return Tweet object
   */
  public Tweet parseEntityFromBody(HttpResponse response) {
    Tweet tweet = null;
    String jsonString;
    int responseStatus = response.getStatusLine().getStatusCode();
    if (responseStatus != HTTP_OK) {
      throw new RuntimeException("Unexpected response status: " + responseStatus);
    }
    //retrieve JSON entity from response
    try {
      jsonString = EntityUtils.toString(response.getEntity());
    } catch (IOException ioe) {
      throw new RuntimeException("Error converting response entity to String", ioe);
    }
    //map JSON to Tweet object
    try {
      tweet = TwitterUtils.toObjectFromJson(jsonString, Tweet.class);
    } catch (IOException ioe) {
      throw new RuntimeException("Error mapping JSON String to object", ioe);
    }
    return tweet;
  }
}
