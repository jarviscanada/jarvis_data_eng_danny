package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gdata.util.common.base.PercentEscaper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import org.apache.http.HttpResponse;

public class TwitterApiImp {

  public static void main(String[] args) throws Exception{
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    TwitterDao dao = new TwitterDao(httpHelper);
//    HttpResponse response = httpHelper
//        .httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=test_1"));
    HttpResponse response = httpHelper
        .httpGet(new URI("https://api.twitter.com/1.1/statuses/show.json?id=210462857140252672"));
    Tweet tweet = dao.parseEntityFromBody(response);
    String[] fields = {"id_str", "text"};
    Method[] methods = Tweet.class.getMethods();
    for (Method m: methods) {
      if (m.getName().startsWith("set") && m.getDeclaredAnnotations().length > 0) {
        Annotation annotation = m.getAnnotation(JsonProperty.class);
        if (!Arrays.asList(fields).contains(((JsonProperty)annotation).value())) {
          System.out.println(((JsonProperty) annotation).value());
        }
      }
    }
  }
}
