package ca.jrvs.apps.twitter.model;

import java.util.List;

public class Hashtag {
  private String text;
  private int[] indices;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int[] getIndices() {
    return indices;
  }

  public void setIndices(int[] indices) {
    this.indices = indices;
  }
}
