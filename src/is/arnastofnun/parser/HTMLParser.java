package is.arnastofnun.parser;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Arnar Jónsson
 * @since 19.10.14
 * @version 0.1
 * Klasinn tekur inn jsoup Document sem innieldur
 * HTML5 kóða frá sérstakri vefsíðu.
 * Klasinn leitar af lykilorðum til að ná réttum niðurstöðum úr kóðanum.
 * 
 * Eins og er virkar hann bara fyrir nafnorð
 * 
 */
public class HTMLParser {
	
	/**
	 * Strengir sem notaðir eru til að athuga hvort leit skilaði núll eða fleiri niðurstöðum
	 */
	private String criticalMiss = "criticalMiss";
	private String partialMiss = "partialHit";
	private String criticalHit = "criticalHit";
	
	
	/**
	 * Inniheldur upplýsingar úr HTML5 kóðanum sem sendur
	 * var inn í klasan sem jsoup Document
	 */
	private ArrayList<String> results;
	
	/**
	 * doc er staðvær breyta fyrir það jsoup Document sem sent var inn í klasann
	 */
	private final Document doc;
	
	/**
	 * @param doc er jsoup Document sem inniheldur
	 * HTML5 kóða sérstakrar vefsíðu
	 */
	public HTMLParser(Document doc) {
		this.results = new ArrayList<String>();
		this.doc = doc;
		constructUsableData();
	}
	
	/**
	 * @param a er strengur
	 * @return skilar true ef strengurinn inniheldur punkt (".")
	 */
	private boolean isLegalNO(String a) {
		return a.contains(".");
	}
	
	
	/**
	 * Ef niðurstaða heyrir undir orflokkinn nafnorð þá sér fallið um að
	 * týna beygingarmyndir út úr HTML5 kóðanum.
	 */
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
	
	/**
	 * Ef niðurstaða heyrir undir orflokkinn lýsingarorð þá sér fallið um að
	 * týna beygingarmyndir út úr HTML5 kóðanum.
	 * 
	 * Fallið er ekki rétt
	 */
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
	
	/**
	 * 
	 * @param a er strengur
	 * @return skilar true ef a er löglegur strengur sem niðurstöður
	 * beygingarmynda fyrir sagnorð, false annars
	 * 
	 * Fallið virkar ekki rétt
	 */
	private boolean isLegalSO(String a) {
		String[] illegal = { "pers", "Et. Ft.", "Nútíð", "Þátíð", "Eintala", "Fleirtala", "Germynd Miðmynd", "Karlkyn Kvenkyn Hvorugkyn" };
	    
	    for( int i = 0; i < illegal.length; i++ ) {
	    	if( a.contains(illegal[i]) ) {
	    		return false;
	    	}
	    }
	    return true;
	}
	
	
	/**
	 * Ef niðurstaða heyrir undir orflokkinn sagnorð þá sér fallið um að
	 * týna beygingarmyndir út úr HTML5 kóðanum.
	 * 
	 * Fallið er ekki rétt
	 */
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
	
	
	/**
	 * Ef niðurstaðan var nákvæmlega ein þá athugar fallið orðflokk niðurstöðunnar sem fundinn var
	 * og kallar svo á viðeigandi föll svo hægt sé að fylla lista af viðeigandi niðurstöðum
	 */
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
	
	
	/**
	 * Ef fleiri en ein niðurstaða fannst þá fyllir fallið
	 * út listann með viðeigandi gildum
	 */
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
	
	
	/**
	 * 
	 * @param e er ekki tómt jsoup Element
	 * @return true ef fleiri en ein niðurstaða fannst
	 */
	private boolean multiHits(Element e) {
		return e.toString().toLowerCase().contains("fundust. smelltu");
	}
	
	/**
	 * 
	 * @param e er ekki tómt jsoup Element
	 * @return true ef einhver niðustaða fannst
	 */
	private boolean found(Element e) {
		return !e.toString().toLowerCase().contains(" fannst ekki.");
	}
	
	
	/**
	 * Athugar hvað er rökfræðilega rétt næsta skref í greiningu HTML5 kóðans.
	 * Þ.e. hvort niðurstaða eða niðurstöður voru fundnar
	 */
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
	
	/**
	 * @return lista yfir greiningu HTML5-kóðans sem
	 * kom inn með jsoup Document-inu doc
	 */
	public ArrayList<String> getResults() {
		return this.results;
	}
}

