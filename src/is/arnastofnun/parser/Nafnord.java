package is.arnastofnun.parser;
import java.io.Serializable;
import java.util.*;

/**
 * @author Arnar Jónsson
 * @since 25.10.14
 * @version 0.1
 * Klasi fyrir orð sem heyra undir orðflokkana Nafnorð, örnefni
 * og mannanöfn.
 * Tekur inn lista af beyginarmyndum eins og þær koma út úr
 * HTML5 kóða frá sérstakri vefsíðu og klippir hann niður í smærri lista.
 *
 */
public class Nafnord implements Serializable {

	
	/**
	 * Staðværar breytur sem innihalda beyginarmyndir nafnorðs, án og með greini, flokkað
	 * í eintölu og fleirtölu.
	 */
    ArrayList<String> Eintala;
    ArrayList<String> Fleirtala;
    
    /**
     * Breyta til að geyma þann lista sem sendur var
     * inn í smiðinn
     */
    ArrayList<String> results;
    
    /**
     * @param results er listi af beygingarmyndum eins og þær
     * koma úr HTML5 kóða sérstakrar vefsíðu.
     */
    public Nafnord(ArrayList<String> results) {
      
      this.Eintala = new ArrayList<String>();
      this.Fleirtala = new ArrayList<String>();
      
      this.results = new ArrayList<String>();
      this.results = results;
      
      constructTables();
    }

    /**
     * Flokkar niðurstöður úr results ArrayListanum niður í
     * smærri einingarlista, svo þær verða tilbúnar til sýningar
     */
    private void constructTables() {
    
      String ident, noGreinir, withGreinir;
      
      for( int i = 2; i < this.results.size(); i++ ) {
	
      	if( i <= 5 ) {
      	  ident = this.results.get(i);
      	  withGreinir = ident.substring(ident.lastIndexOf(" ")+1, ident.length());
      	  noGreinir = ident.substring(ident.indexOf(" ")+1, ident.length() - (withGreinir.length() + 1) );
      	  this.Eintala.add(noGreinir);
      	  this.Eintala.add(withGreinir);
      	}
      	else {
      	  ident = this.results.get(i);
      	  withGreinir = ident.substring(ident.lastIndexOf(" ")+1, ident.length());
      	  noGreinir = ident.substring(ident.indexOf(" ")+1, ident.length() - (withGreinir.length() + 1) );
      	  this.Fleirtala.add(noGreinir);
      	  this.Fleirtala.add(withGreinir);	
      	}
      }
    
    }
    
    /**
     * @return er listi yfir beygingarmyndir orðsins í eintölu,
     * bæði án og með greini, í röðinni:
     * Nefnifall (án/með)
     * Þolfall (án/með)
     * Þágufall (án/með)
     * Eignarfall (án/með).
     */
    public ArrayList<String> getEintala() {
      return this.Eintala;
    }
    
    /**
     * @return er listi yfir beygingarmyndir orðsins í fleirtölu,
     * bæði án og með greini, í röðinni:
     * Nefnifall (án/með), Þolfall (án/með), Þágufall (án/með) og
     * Eignarfall (án/með).
     */
    public ArrayList<String> getFleirtala() {
      return this.Fleirtala;
    }

}