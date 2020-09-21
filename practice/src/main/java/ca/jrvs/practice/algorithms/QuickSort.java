package ca.jrvs.practice.algorithms;

public class QuickSort {

  public void quickSort(int[] arr, int begin, int end) {
    if(begin < end) {
      int partitionIndex = partition(arr, begin, end);

      quickSort(arr, begin, partitionIndex - 1);
      quickSort(arr, partitionIndex + 1, end);
    }
  }

  public int partition(int[] arr, int begin, int end) {
    int pivot = arr[end];
    int i = begin - 1;

    for(int j = begin; j < end; j++) {
      if (arr[j] <= pivot) {
        i++;

        int swap = arr[i];
        arr[i] = arr[j];
        arr[j] = swap;
      }
    }

    int swap = arr[i+1];
    arr[i+1] = arr[end];
    arr[end] = swap;

    return i + 1;
  }
}
