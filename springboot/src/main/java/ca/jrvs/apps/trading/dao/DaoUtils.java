package ca.jrvs.apps.trading.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class DaoUtils {

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
}
