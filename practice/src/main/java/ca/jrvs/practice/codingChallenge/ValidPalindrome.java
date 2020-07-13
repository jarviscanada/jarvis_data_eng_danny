package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/Valid-Palindrome-37ad302a09904442afc5425e0d67e5ef
 */
public class ValidPalindrome {

  public boolean isPalindrome(String s) {
    int i = 0;
    int j = s.length()-1;
    while(i <= j) {
      while(i <= j && !Character.isLetterOrDigit(s.charAt(i))) {
        i++;
      }
      while(i <= j && !Character.isLetterOrDigit(s.charAt(j))) {
        j--;
      }
      if (i<=j && Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
        return false;
      }
      i++;
      j--;
    }
    return true;
  }

  public boolean isPalindromeRec(String s) {
    int i = 0;
    int j = s.length()-1;
    if (i<=j && !Character.isLetterOrDigit(s.charAt(i))) {
      return isPalindromeRec(s.substring(i+1, j));
    } else if (i<=j && !Character.isLetterOrDigit(s.charAt(j))) {
      return isPalindromeRec(s.substring(i, j-1));
    } else if (i<=j && Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
      return false;
    }
    return true;
  }
}
