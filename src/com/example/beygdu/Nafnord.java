package com.example.beygdu;
import java.io.Serializable;
import java.util.*;

public class Nafnord implements Serializable {

    ArrayList<String> Eintala;
    ArrayList<String> Fleirtala;
    ArrayList<String> results;
    
    public Nafnord(ArrayList<String> results) {
      
      this.Eintala = new ArrayList<String>();
      this.Fleirtala = new ArrayList<String>();
      
      this.results = new ArrayList<String>();
      this.results = results;
      
      constructTables();
    }

    
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
    
    public ArrayList<String> getEintala() {
      return this.Eintala;
    }
    
    public ArrayList<String> getFleirtala() {
      return this.Fleirtala;
    }

}