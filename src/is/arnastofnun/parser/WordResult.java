package is.arnastofnun.parser;


import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher; 

public class WordResult {

  private String type;
  private String title;
  private String note;
  
  private ArrayList<ArrayList<String>> dump;
  
  private ArrayList<Block> blocks = new ArrayList<Block>();

  public WordResult(String type, String title, String note, ArrayList<ArrayList<String>> dump) {
  
    this.type = type;
    this.title = title;
    this.note = note;
    this.dump = dump;
    
    populateBlockList();
  
  }
  
  //
  //
  //
  public ArrayList<Block> getBlocks() {
    return this.blocks;
  }
  
  private void populateBlockList() {
    
    if(this.title.contains("Sagnorð")) {
      populateSOBlocks();
    }
    
    else if(this.title.contains("Lýsingarorð")) {
      populateLOBlocks();
    }
    
    else {
      populateSimpleBlocks();
    }
    
  }
  
//////
//////	Helper Functions
//////

  ////
  ////
  ////
  private boolean isLegalSimpleBlock(String a) {
    return a.contains("Nf.") || a.contains("Þf.") || a.contains("Þgf.") || a.contains("Ef.");
  }
  
  ////
  ////
  ////
  private String destroyPointer(String a) {
    return a.substring(a.indexOf(" ")+1, a.length());
  }
  
  ////
  ////
  ////
  private ArrayList<String> constructNormalCases(String a) {
    ArrayList<String> returnValue = new ArrayList<String>();
    
    Pattern pattern = Pattern.compile("\\s");
    Matcher matcher = pattern.matcher(a);
    
    ArrayList<Integer> starts = new ArrayList<Integer>();
    
    while( matcher.find() ) {
      starts.add(matcher.start());
    }
    
    for( int i = starts.size()-1; i > -1; i-- ) {
      returnValue.add(a.substring(starts.get(i)+1, a.length()));
      a = a.substring(0, starts.get(i));
    }
    returnValue.add(a);
    
    return listReverse(returnValue);
  }
  
  ////
  ////
  ////
  private ArrayList<String> constructSpecialCases(String a) {
    ArrayList<String> returnValue = new ArrayList<String>();
    
    Pattern pattern = Pattern.compile("\\w\\s\\w");
    Matcher matcher = pattern.matcher(a);
    
    ArrayList<Integer> starts = new ArrayList<Integer>();
    
    while( matcher.find() ) {
      starts.add(matcher.start());
    }
    
    for( int i = starts.size()-1; i > -1; i-- ) {
      returnValue.add(a.substring(starts.get(i)+2, a.length()));
      a = a.substring(0, starts.get(i)+1);
    }
    returnValue.add(a);
    
    return listReverse(returnValue);
  }
  
  ////
  ////
  ////
  private ArrayList<String> listReverse(ArrayList<String> a) {
    
    ArrayList<String> temp = new ArrayList<String>();
    
    for( int i = a.size()-1; i > -1; i-- ) {
    
      temp.add(a.get(i));
    
    }
    
    return temp;
  }
  
  
  private boolean isIllegalLoString(String a) {
    return a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"));
  }
  
  private void constructLoBlocks(ArrayList<String> a) {
  /*
    ArrayList<String> cleanResults = new ArrayList<String>();
    
    int subBlockCount = 0;
    for( int i = 0; i < a.size(); i++ ) {
    
      if( !isIllegalLoString(a.get(i)) ) {
	cleanResults.add(a.get(i));
      }
      
      if( a.get(i).contains("h4") ) {
	subBlockCount++;
      }
    
    }
    
    ArrayList<ArrayList<String>> subBlocks = new ArrayList<ArrayList<String>>();
    
    ArrayList<String> temp = new ArrayList<String>();
    
    int iCount = 0;
    for( int i = 1; i < cleanResults.size(); i++ ) {
      
      if( cleanResults.get(i).contains("h4") && iCount != 0 ) {
	subBlocks.add(temp);
	iCount = 0;
	temp = new ArrayList<String>();
	temp.add(cleanResults.get(i));
	iCount++;
      }
      
      temp.add(cleanResults.get(i));
      iCount++;
      
    }
    
    subBlocks.add(temp);
    
    for( ArrayList<String> t : subBlocks ) {
    
      
    
    }
  */
    //Costruct block title
    String blockTitle = destroyPointer(a.get(0));
    a.remove(0);
    
    ArrayList<String> cleanList = new ArrayList<String>();
    
    for( String line : a ) {
    
      if( !isIllegalLoString(line) ) {
	cleanList.add(line);
      }
    
    }
    
    ArrayList<ArrayList<String>> subBlocks = new ArrayList<ArrayList<String>>();
    ArrayList<String> temp = new ArrayList<String>();
    int subBlockCount = 0;
    for( String line : cleanList ) {
    
      if( line.contains("h4") && subBlockCount != 0 ) {
	subBlocks.add(temp);
	temp = new ArrayList<String>();
	subBlockCount = 0;
	temp.add(line);
	subBlockCount++;
      }
      else {
	temp.add(line);
	subBlockCount++;
      }
      
    }
    subBlocks.add(temp);
  
    
  }
  
  
//////
////// Populate Functions
//////

  private boolean isIllegalNoString(String a) {
  
    return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) || (a.length() < 4);
  
  }
  
  private String consructTableTitle(String a) {
  
    return a.substring(3, a.length());
  
  }
  
  private ArrayList<String> constructContent(String a) {
  
    a = a.substring(3, a.length());
    a = a.substring(a.indexOf(" ")+1, a.length());
    
    ArrayList<String> returnList;
    
    if( a.contains("/") ) {
      returnList = constructSpecialCases(a);
    }
    else {
      returnList = constructNormalCases(a);
    }
    
    return returnList;
  
  }
  
  private String destroyHeader(String a) {
	  return a.substring(3, a.length());
  }
  
  private Tables constructNOTables(ArrayList<String> a) {
	  
	  String tableTitle = "";
	  String[] columnNames = { "án greinis", "með greini" };
	  String[] rowNames = { "Nf.", "Þf.", "Þgf.", "Ef." };
	  ArrayList<String> content = new ArrayList<String>();
	  
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  for( String s : a) {
		  
		  if( s.contains("th") ) {
			  
			  tableTitle = destroyHeader(s);
			  
		  }
		  
		  if( s.contains(".") ) {
			  
			  if( s.contains("/") ) {
				  temp = constructSpecialCases(destroyPointer(destroyPointer(s)));
			  }
			  else {
				  temp = constructNormalCases(destroyPointer(destroyPointer(s)));
			  }
			  for( String t : temp ) {
				  content.add(t);
			  }
			  
		  }
		  
	  }
	  
	  return new Tables(tableTitle, columnNames, rowNames, content);
  }

  private boolean isIllegalRawNo(String a) {
	  return (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala")) ) || (a.length() < 4);
  }
  
  private void constructNoBlock(ArrayList<String> a) {
  /*
    String[] columnNames = { "án greinis", "með greini" };
    String[] rowNames = { "Nf.", "Þf.", "Þgf.", "Ef." };
    
    ArrayList<ArrayList<String>> tableConstruct = new ArrayList<ArrayList<String>>();
    ArrayList<String> temp = new ArrayList<String>();
    
    int internalCount = 0;
    for( String line : a ) {
      
      if( !isIllegalNoString(line) ) {
	
	if( line.contains("th") && internalCount != 0 ) {
	
	  tableConstruct.add(temp);
	  internalCount = 0;
	  temp = new ArrayList<String>();
	  temp.add(line);
	  internalCount++;
	
	}
	else {
	
	  temp.add(line);
	  internalCount++;
	
	}
	
      }
      
    }
    
    tableConstruct.add(temp);
    
    ArrayList<Tables> tables = new ArrayList<Tables>();
    
    String title = "";
    ArrayList<String> res = new ArrayList<String>();
    ArrayList<String> tempt = new ArrayList<String>();
    
    
    for( ArrayList<String> aList : tableConstruct ) {
    
      res = new ArrayList<String>();
      
      for( String line : aList ) {
      
	if( a.contains("th") ) {
	  title = consructTableTitle(line);
	}
	
	if( a.contains(".") ) {
	  tempt = constructContent(line);
	  
	  for( String l : temp ) {
	    res.add(l);
	  }
	  
	}
	
      
      }
      
      tables.add(new Tables(title, columnNames, rowNames, res));
    
    }
    
    ArrayList<SubBlock> sbal = new ArrayList<SubBlock>();
    SubBlock subB = new SubBlock("", tables);
    sbal.add(subB);
    Block oneBlock = new Block("", sbal);
    
    this.blocks.add(oneBlock);
    
  */

	  
	  String subBlockTitle = "";
	  
	  ArrayList<ArrayList<String>> rawData = new ArrayList<ArrayList<String>>();
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  int count = 0;
	  for( String s : a ) {
		  
		  if( !isIllegalRawNo(s) ) {
			  
			  if( s.contains("th") && count != 0 ) {
				  
				  rawData.add(temp);
				  temp = new ArrayList<String>();
				  count = 0;
				  temp.add(s);
				  count++;
				  
			  }
			  else {
				  
				  temp.add(s);
				  count++;
				  
			  }
			  
		  }
		  
	  }
	  
	  rawData.add(temp);
	  
	  ArrayList<Tables> tAL = new ArrayList<Tables>();
	  
	  for( ArrayList<String> d : rawData ) {
		  
		  Tables tempTwo = constructNOTables(d);
		  
		  tAL.add(tempTwo);
		  
	  }
	  
	  SubBlock sB = new SubBlock(subBlockTitle, tAL);
	  
	  ArrayList<SubBlock> sAL = new ArrayList<SubBlock>();
	  sAL.add(sB);
	  
	  this.blocks.add(new Block("", sAL));
	  
	  
  }

  private void populateSimpleBlocks() {
    
    for( ArrayList<String> aList : this.dump ) {
    
      constructNoBlock(aList);
    
    }
    
  }
  
  private void populateLOBlocks() {
  
    for( ArrayList<String> aList : dump ) {
      
      constructLoBlocks(aList);
      
    }
  
  }
  
  private void populateSOBlocks() {
  
  }
  
}