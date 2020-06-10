package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.TwitterUtils;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private static final String POST_USAGE = "Usage: post \"TWEET TEXT\" latitude:longitude";
  private static final String SHOW_USAGE = "Usage: show \"TWEET_ID\" \"FIELD_1,FIELD_2...\"";
  private static final String DELETE_USAGE = "Usage: delete \"ID_1,ID_2...\"";
  private final Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    //validate args
    if (args.length != 3) {
      throw new IllegalArgumentException(POST_USAGE);
    }

    String text = args[1];
    String[] coords = args[2].split(COORD_SEP);

    if (coords.length != 2 || StringUtils.isEmpty(text)) {
      throw new IllegalArgumentException(POST_USAGE);
    }
    float latCoord = Float.parseFloat(coords[0]);
    float lonCoord = Float.parseFloat(coords[1]);

    Tweet tweet = TwitterUtils.buildTweet(text, latCoord, lonCoord);
    return service.postTweet(tweet);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(SHOW_USAGE);
    }

    String id = args[1];
    String[] fields = args[2].split(COMMA);

    return service.showTweet(id, fields);
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException(DELETE_USAGE);
    }

    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);
  }
}
