package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Merge-Sorted-Array-9abe3860e1ef4e7494bda8c106897342
 */
public class MergedSorted {

  public void merge(int[] nums1, int m, int[] nums2, int n) {
    int tail1 = m - 1, tail2 = n - 1, finished = m + n - 1;
    while (tail1 >= 0 && tail2 >= 0) {
      if (nums1[tail1] > nums2[tail2]) {
        nums1[finished--] = nums1[tail1--];
      } else {
        nums1[finished--] = nums2[tail2--];
      }
    }

    while (tail2 >= 0) {
      nums1[finished--] = nums2[tail2--];
    }
  }
}
