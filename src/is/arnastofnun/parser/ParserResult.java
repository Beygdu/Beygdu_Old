package is.arnastofnun.parser;



import java.util.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ParserResult {

  private String type;

  private String searchWord;
  
  private int id;
  
  private int[] ids;
  private String[] desc;
  
  
  private String title;
  private String note;
  private ArrayList<ArrayList<String>> results;
  
  //////
  //////
  //////
  public ParserResult() {
  
  }
  
  //
  //
  //
  public String getSearchWord() {
    return this.searchWord;
  }
  
  ///
  ///
  ///
  public void setSearchWord(String a) {
    this.searchWord = a;
  }
  
  //
  //
  //
  public int getId() {
    return this.id;
  }
  
  ///
  ///
  ///
  public void setId(int a) {
    this.id = a;
  }
  
  //
  //
  //
  public String getType() {
    return this.type;
  }
  
  ///
  ///
  ///
  public void setType(String a) {
    this.type = a;
  }
  
  //
  //
  //
  public int[] getIds() {
    return this.ids;
  }
  
  ///
  ///
  ///
  public void setIds(int[] a) {
    this.ids = a;
  }
  
  //
  //
  //
  public String[] getDesc() {
    return this.desc;
  }
  
  ///
  ///
  ///
  public void setDesc(String[] a) {
    this.desc = a;
  }
  
  //
  //
  //
  public String getTitle() {
    return this.title;
  }
  
  ///
  ///
  ///
  public void setTitle(String a) {
    this.title = a;
  }
  
  //
  //
  //
  public String getNote() {
    return this.note;
  }
  
  ///
  ///
  ///
  public void setNode(String a) {
    this.note = a;
  }
  
  //
  //
  //
  public ArrayList<ArrayList<String>> getResults() {
    return this.results;
  }
  
  ///
  ///
  ///
  public void setResults(ArrayList<ArrayList<String>> a) {
    this.results = a;
  }
  
  public WordResult getWordResult() {
  
  return new WordResult(this.type, this.title, this.note, this.results);
  
  }

 
}