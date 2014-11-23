package is.arnastofnun.parser;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {

  private String title;
  
  private ArrayList<SubBlock> sb;
  
  public Block(String title, ArrayList<SubBlock> sb) {
    this.title = title;
    this.sb = sb;
  }
  
  //
  //
  //
  public String getTitle() {
    return this.title;
  }
  
  //
  //
  //
  public ArrayList<SubBlock> getBlocks() {
    return this.sb;
  }
  
  
}