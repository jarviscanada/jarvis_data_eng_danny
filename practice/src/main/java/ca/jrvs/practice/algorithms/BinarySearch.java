package ca.jrvs.practice.algorithms;

import java.util.Optional;

public class BinarySearch {

  /**
   * find the the target index in a sorted array
   *
   * @param arr input arry is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not ound
   */
  public Optional<Integer> binarySearchRecursion(Integer[] arr, Integer target) {
    int mid = arr.length / 2;
    if (arr[mid] == target) {
      return Optional.of((Integer)target);
    } else if (arr[mid] < target) {
      return binarySearchRecursion(arr, target, mid, arr.length);
    } else if (arr[mid] > target) {
      return binarySearchRecursion(arr, target, 0, mid);
    }
    return Optional.empty();
  }

  public Optional<Integer> binarySearchRecursion(Integer[] arr, Integer target,
      Integer start, Integer end) {
    int mid = (start + end) / 2;
    if (arr[mid] == target) {
      return Optional.of((Integer)target);
    } else if (arr[mid] < target) {
      return binarySearchRecursion(arr, target, mid, end);
    } else if (arr[mid] > target) {
      return binarySearchRecursion(arr, target, start, mid);
    }
    return Optional.empty();
  }

  /**
   * find the the target index in a sorted array
   *
   * @param arr input arry is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not ound
   */
  public Optional<Integer> binarySearchIteration(Integer[] arr, Integer target) {
    int mid;
    int start = 0;
    int end = arr.length - 1;

    while (start <= end) {
      mid = (start + end)/ 2;
      if (arr[mid] == target) {
        return Optional.of(arr[mid]);
      } else if (arr[mid] > target) {
        end = mid - 1;
      } else {
        start = mid + 1;
      }
    }
    return Optional.empty();
  }
}