package com.example.beygdu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BeygingarActivity extends Activity {

	private ArrayList<String> searchResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_beygingar);
//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		//Get the message from the intent
		Intent intent = getIntent();
		searchResults = intent.getStringArrayListExtra("searchResults");
		initGUI();
	}
	
	private void initGUI(){
		setContentView(R.layout.activity_beygingar); // use your layout xml file here
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
		TableRow tableRows[] = new TableRow[1];
		for (int i = 0; i < tableRows.length; i++){
			tableRows[i] = new TableRow(this);
			tableRows[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		            LayoutParams.WRAP_CONTENT));
			tableRows[i].setWeightSum(2.0f);
		    tableRows[i].setPadding(5, 5, 5, 5);
		    for (String s: searchResults){
		    	TextView text = new TextView(this);
		    	text.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
		    			android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
		    	text.setText(s);
		    	tableRows[i].addView(text);
		    	
		    }
		    
		    tableLayout.addView(tableRows[i]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beygingar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_beygingar,
					container, false);
			return rootView;
		}
	}
}
