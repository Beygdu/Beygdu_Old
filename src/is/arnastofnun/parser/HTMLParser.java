package is.arnastofnun.parser;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Arnar
 * @version 0.1
 */
public class HTMLParser {
	
	private String criticalMiss = "criticalMiss";
	private String partialMiss = "partialHit";
	private String criticalHit = "criticalHit";
	
	private ArrayList<String> results;
	private final Document doc;
	
	/**
	 * @param doc the incoming document
	 */
	public HTMLParser(Document doc) {
		this.results = new ArrayList<String>();
		this.doc = doc;
		constructUsableData();
	}
	
	private boolean isLegalNO(String a) {
		return a.contains(".");
	}
	
	private void parseNO() {
		Element a = doc.body();
	    Elements e = a.getElementsByTag("tr");
	    
	    String parseString;
	    
	    for( Element element : e ) {
	      parseString = element.text();
	      if( isLegalNO(parseString) ) {
	    	  this.results.add(parseString);
	      }
	    }
	}
	
	private void parseLO() {
		Element a = doc.body();
	    Elements tr = a.getElementsByTag("tr");
	    
	    String parseString;
	    
	    for( Element element : tr ) {
	      parseString = element.text();
	      
	      if( isLegalNO(parseString) ) {
	    	  this.results.add(parseString);
	      }
	    }
	}
	
	private boolean isLegalSO(String a) {
		String[] illegal = { "pers", "Et. Ft.", "Nútíð", "Þátíð", "Eintala", "Fleirtala", "Germynd Miðmynd", "Karlkyn Kvenkyn Hvorugkyn" };
	    
	    for( int i = 0; i < illegal.length; i++ ) {
	    	if( a.contains(illegal[i]) ) {
	    		return false;
	    	}
	    }
	    return true;
	}
	
	private void parseSO() {
		Element a = doc.body();
	    Elements tr = a.getElementsByTag("tr");
	    
	    String parseString;
	    
	    for( Element element : tr ) {
	    	parseString = element.text();
	    	if( isLegalSO(parseString) ) {
	    		this.results.add(parseString);
	    	}
	    }
	}
	
	private void parseCriticalHit() {
		
		Element body = doc.body();
		
		String identify = body.getElementsByTag("small").text();
		
		if( identify.contains("nafnorð") ) {
			this.results.add(identify);
			parseNO();
		}
		else if( identify.contains("Lýsingarorð")) {
			this.results.add(identify);
			parseLO();
		}
		else {
			this.results.add(identify);
			parseSO();
		}
		
	}
	
	private void parsePartialHit() {
		Element body = doc.body();
		Elements e = body.getElementsByTag("li");
		
		String description, id, info;
		
		for( Element element : e ) {
			
			description = element.text();
			
			id = element.getElementsByTag("a").attr("onClick");
			id = id.substring(id.indexOf("'")+1, id.lastIndexOf("'"));
			
			info = description + " - " + id;
			
			this.results.add(info);
		}
	}
	
	private boolean multiHits(Element e) {
		return e.toString().toLowerCase().contains("fundust. smelltu");
	}
	
	private boolean found(Element e) {
		return !e.toString().toLowerCase().contains(" fannst ekki.");
	}
	
	private void constructUsableData() {
		Element e = doc.body();		
		if( found(e) ) {
			
			if( multiHits(e) ) {
				this.results.add(this.partialMiss);
				parsePartialHit();
			}
			else {
				this.results.add(this.criticalHit);
				parseCriticalHit();
			}
			
		}
		else {
			this.results.add(this.criticalMiss);
		}
	}
	
	public ArrayList<String> getResults() {
		return this.results;
	}
}

