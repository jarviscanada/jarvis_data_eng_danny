package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://www.notion.so/Find-Largest-Smallest-e34c51c80a424f31bf8cf9161d8f7cfb
 */
public class FindLargestSmallest {

  public static int[] oneLoop(int[] arr) {
    int min = arr[0];
    int max = arr[0];

    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      } else if (arr[i] > max) {
        max = arr[i];
      }
    }

    return new int[]{min, max};
  }

  public static int[] withStreamApi(int[] arr) {
    int min, max;
    min = Arrays.stream(arr).min().orElse(0);
    max = Arrays.stream(arr).max().orElse(0);
    return new int[]{min, max};
  }

  public static int[] withCollectionApi(int[] arr) {
    List<Integer> collection = Arrays.stream(arr).boxed().collect(Collectors.toList());
    int min = Collections.min(collection);
    int max = Collections.max(collection);
    return new int[]{min, max};
  }
}
