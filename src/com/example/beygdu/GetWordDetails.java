package com.example.beygdu;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetWordDetails {

	private ArrayList<String> results = new ArrayList<String>();
	private boolean isReady = false;
	
	public GetWordDetails(String word) {
		//searchURL(word);
	}
	
	public ArrayList<String> getResults() {
		return results;
	}
	
	public boolean getIsReady() {
		return isReady;
	}
	
	private boolean Found(Element e) {
		return !e.toString().toLowerCase().contains(" fannst ekki.");
	}

	private boolean multiHits(Element e) {
		return e.toString().toLowerCase().contains("fundust. smelltu");
	}

	private void parseMultipleHits(Element e) {
		Elements es = e.getElementsByTag("li");
		String id, description, info;

		results.add("multiHit");
		
		for (Element element : es) {
			description = element.text();
			id = element.getElementsByTag("a").attr("onClick");
			id = id.substring(id.indexOf("'") + 1, id.lastIndexOf("'"));
			info = description + " - " + id;
			results.add(info);
		}
	}

	private void parseSingleHit(Element e) {
		
	}
	
	

	public void searchURL(String word) {
		String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?q=";
		final String url = baseURL + word;
		try {
			Document doc = Jsoup.connect(url).get();
			Element body = doc.body();
			if (Found(body)) {

				System.out.println("Ordid fannst");

				if (multiHits(body)) {
					parseMultipleHits(body);
				} else {
					parseSingleHit(body);
				}
				isReady = true;

			} else {
				System.out.println("ordid fannst ekki");
				isReady = true;
			}
		} catch (IOException e) {
			System.out.print("Something went wrong, horribly wrong");
			isReady = true;
		}
	}
}