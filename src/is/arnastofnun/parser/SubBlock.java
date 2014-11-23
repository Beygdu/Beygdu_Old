package is.arnastofnun.parser;


import java.io.Serializable;
import java.util.ArrayList;

public class SubBlock implements Serializable {

  private String title;
  
  private ArrayList<Tables> tables;

  public SubBlock(String title, ArrayList<Tables> tables) {
  
    this.title = title;
    
    this.tables = tables;
  
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
  public ArrayList<Tables> getTables() {
    return this.tables;
  }

  
  

}