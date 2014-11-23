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
  
  private String[] constructSoColumnNames(int a) {
	  if(a == 0) {
		  String[] returnArray = { "Et.", "Ft." };
		  return returnArray;
	  }
	  else if(a == 1) {
		  String[] returnArray = { "Germynd", "Miðmynd" };
		  return returnArray;  
	  }
	  else if(a == 2) {
		  String[] returnArray = { "Germynd", "Miðmynd" };
		  return returnArray; 
	  }
	  else if(a == 4) {
		  String[] returnArray = { "Karlkyn", "Kvennkyn", "Hvorugkyn" };
		  return returnArray;
	  }
	  else if(a == 5) {
		  String[] returnArray = { "Et.", "Ft." };
		  return returnArray;
	  }
	  return null;
  }
  
  private String[] constructSoRowNames(int a) {
	  if(a == 0) {
		  String[] returnArray = { "1. pers.", "2. pers.", "3. pers." };
		  return returnArray;
	  }
	  else if(a == 1) {
		  String[] returnArray = { "Stýfður", "Et.", "Ft." };
		  return returnArray;
	  }
	  else if(a == 2) {
		  return null;
	  }
	  else if(a == 4) {
		  String[] returnArray = { "Nf.", "Þf.", "Þgf.", "Ef." };
		  return returnArray;
	  }
	  else if(a == 5) {
		  String[] returnArray = { "3. pers." };
		  return returnArray;
	  }
	  return null;
  }
  
  private boolean isIllegalSoString(String a) {
	  return (a.contains("tr") && (a.contains("Nútíð") || a.contains("Þátíð"))) ||
			  a.contains(". pers.") || a.contains(".pers") || a.contains("Et. Ft.") ||
			  (a.length() <= 3) || (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) ||
			  (a.contains("tr") && (a.contains("Germynd") || a.contains("Miðmynd"))) ||
			  (a.contains("tr") && (a.contains("Karlkyn") || a.contains("Kvennkyn") || a.contains("HvorugKyn")));
  }
  
  private Tables constructSoTables(ArrayList<String> a, String bT, String sBT) {
	  
	  ArrayList<String> content = new ArrayList<String>();
	  
	  return new Tables("", null, null, content);
  }
  
  private SubBlock constructSoSubBlocks(ArrayList<String> a, String bT) {
	  
	  ArrayList<Tables> tb = new ArrayList<Tables>();
	  
	  String title = destroyPointer(a.get(0));
	  
	  ArrayList<ArrayList<String>> rawTables = new ArrayList<ArrayList<String>>();
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  int count = 0;
	  for( String s : a ) {
		  
		  if( s.contains("h3") && count != 0 ) {
			  rawTables.add(temp);
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
	  rawTables.add(temp);
	  
	  Tables oTB;
	  
	  for( ArrayList<String> rTB : rawTables ) {
		  
		  oTB = constructSoTables(rTB, bT, title);
		  tb.add(oTB);
		  
	  }
	  
	  return new SubBlock(title, tb);
  }
  
  private void consructSoBlocks(ArrayList<String> a) {
	  
	  ArrayList<SubBlock> sb = new ArrayList<SubBlock>();
	  
	  String title = destroyPointer(a.get(0));
	  
	  ArrayList<ArrayList<String>> rawSubBlocks = new ArrayList<ArrayList<String>>();
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  int count = 0;
	  for( String s : a ) {
		  
		  if( !isIllegalSoString(s) ) {
			  
			  if( s.contains("h3") && count != 0 ) {
				  rawSubBlocks.add(temp);
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
	  
	  rawSubBlocks.add(temp);
	  SubBlock oSB;
	  
	  for( ArrayList<String> bje : rawSubBlocks ) {
		  
		 oSB = constructSoSubBlocks(bje, a.get(0));
		 sb.add(oSB);
		  
	  }
	  
	  this.blocks.add(new Block(title, sb));
  }
  
  
  private boolean isIllegalLoString(String a) {
    return a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"));
  }
  
  private boolean isIllegalLoRaw(String a) {
	  return a.contains("h3") || (a.contains("tr") && (a.contains("Eintala") || a.contains("Fleirtala"))) || (a.contains("tr") && !a.contains("."));
  }
  
  private Tables constructLoTables(ArrayList<String> a) {
	  
	  String tableTitle = destroyPointer(a.get(0));
	  String[] columnNames = { "Karlkyn", "Kvennkyn", "HvorugKyn" };
	  String[] rowNames = { "Nf.", "Þf.", "Þgf.", "Ef." };
	  
	  ArrayList<String> content = new ArrayList<String>();
	  
	  ArrayList<String> temp;
	  
	  for( String s : a ) {
		  
		  if( s.contains(".") ) {
			  
			  temp = new ArrayList<String>();
			  
			  if( s.contains("/") ) {
				  temp = constructSpecialCases(destroyPointer(destroyPointer(s)));
			  }
			  else {
				  temp = constructNormalCases(destroyPointer(destroyPointer(s)));
			  }
			  
			  for( String j : temp ) {
				  
				  content.add(j);
				  
			  }
			  
		  }
		  
	  }
	  
	  return new Tables(tableTitle, columnNames, rowNames, content);
  }
  
  private SubBlock constructLoSubBlock(ArrayList<String> a) {
	  
	  ArrayList<Tables> tab = new ArrayList<Tables>();
	  
	  ArrayList<ArrayList<String>> rawTables = new ArrayList<ArrayList<String>>();
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  int count = 0;
	  for( String s : a ) {
		  
		  if( s.contains("h4") ) {
			  // DO NOTHING
		  }
		  else if( s.contains("th") && count != 0 ) {
			  rawTables.add(temp);
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
	  rawTables.add(temp);
	  
	  for( ArrayList<String> y : rawTables ) {
		  
		  Tables ttable = constructLoTables(y);
		  
		  tab.add(ttable);
		  
	  }
	  
	  return new SubBlock(destroyPointer(a.get(0)), tab);
  }
  
  private void constructLoBlocks(ArrayList<String> a) {
  
	  String blockTitle = destroyPointer(a.get(0));
	  
	  ArrayList<SubBlock> sB = new ArrayList<SubBlock>();
	  
	  ArrayList<ArrayList<String>> rawSubBlocks = new ArrayList<ArrayList<String>>();
	  
	  ArrayList<String> temp = new ArrayList<String>();
	  
	  int count = 0;
	  for( String s : a ) {
		
		  if( !isIllegalLoRaw(s)) {
			  
			  if( s.contains("h4") && count != 0 ) {
				  rawSubBlocks.add(temp);
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
	  rawSubBlocks.add(temp);
	  
	  for( ArrayList<String> t : rawSubBlocks ) {
		  
		  SubBlock nSB = constructLoSubBlock(t);
		  
		  sB.add(nSB);
	  }
	  
	  
	  this.blocks.add(new Block(blockTitle, sB));
    
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
  
	  for( ArrayList<String> aList : dump ) {
		  
		  consructSoBlocks(aList);
		  
	  }
	  
  }
  
}