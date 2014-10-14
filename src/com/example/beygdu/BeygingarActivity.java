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
import android.widget.TextView;

public class BeygingarActivity extends Activity {

	private ArrayList<String> searchResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);
		
		//Get the message from the intent
		Intent intent = getIntent();
		searchResults = intent.getStringArrayListExtra("searchResults");
		setTableText();
	}

	private void setTableText() {
		//layout = (TableLayout) findViewById(R.id.tablelayout);
		//((TextView) findViewById(R.id.cell11)).setText(searchResults.get(1));
		int counter = 0;
		for(int row=0; row < 4; row++) {
			   for(int col = 0; col < 2; col++) {
			    String cellID = "cell" + row + col;
			    int resID = getResources().getIdentifier(cellID, "id", "com.example.beygdu");
			    ((TextView) findViewById(resID)).setText(searchResults.get(counter));
			    counter++;
			   }
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
