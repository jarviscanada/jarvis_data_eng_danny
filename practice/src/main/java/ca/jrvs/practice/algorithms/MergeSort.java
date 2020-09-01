package ca.jrvs.practice.algorithms;

public class MergeSort {

  public static void mergeSort(int[] arr, int n) {
    if (n < 2) {
      return;
    }

    int mid = n / 2;
    int[] left = new int[mid];
    int[] right = new int[n-mid];

    for(int i = 0; i < mid; i++) {
      left[i] = arr[i];
    }

    for(int i = mid; i < n; i++) {
      right[i-mid] = arr[i];
    }

    mergeSort(left, mid);
    mergeSort(right, n - mid);

    merge(arr, left, right, mid, n - mid);
  }

  public static void merge(int[] arr, int[] left, int[] right,
      int leftIndex, int rightIndex) {
    int i = 0, j = 0, k = 0;
    while (i < leftIndex && j < rightIndex) {
      if (left[i] <= right[j]) {
        arr[k++] = left[i++];
      } else {
        arr[k++] = right[j++];
      }
    }

    while (i < leftIndex) {
      arr[k++] = left[i++];
    }

    while (j < rightIndex) {
      arr[k++] = right[j++];
    }
  }
}
