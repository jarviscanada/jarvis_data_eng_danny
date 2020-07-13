package ca.jrvs.practice.codingChallenge;

import java.util.Map;

/**
 * https://www.notion.so/How-to-compare-two-maps-855f78098fae4c44b58678cce7898ff0
 */
public class CompareMap {

  public <K,V> boolean compareUsingEquals(Map<K,V> a, Map<K,V> b) {
    if (a.size() !=  b.size()) {
      return false;
    }
    for (Map.Entry<K,V> entry: a.entrySet()) {
      if (!b.entrySet().contains(entry)) {
        return false;
      }
    }
    return true;
  }
}
