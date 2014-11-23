package is.arnastofnun.parser;


import java.io.Serializable;
import java.util.ArrayList;

public class Tables implements Serializable {

  private String title;
  
  private String[] columnNames;
  
  private String[] rowNames;
  
  private ArrayList<String> content;
  
  public Tables(String title, String[] columnNames, String[] rowNames, ArrayList<String> content) {
  
    this.title = title;
    this.columnNames = columnNames;
    this.rowNames = rowNames;
    this.content = content;
  
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
  public String[] getColumnNames() {
    return this.columnNames;
  }
  
  //
  //
  //
  public String[] getRowNames() {
    return this.rowNames;
  }
  
  //
  //
  //
  public ArrayList<String> getContent() {
    return this.content;
  }

}
