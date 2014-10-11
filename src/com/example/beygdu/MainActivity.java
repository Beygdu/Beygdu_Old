package com.example.beygdu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class MainActivity extends Activity {

	public static ArrayList<String> searchResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void btnOnClick(View view){
		Intent intent = new Intent(this, BeygingarActivity.class);
		
		//Get word from editText and
		
		// TODO Here the SQL query could be made and called:
		
		// TODO make sure it is only one word.
		EditText editText = (EditText) findViewById(R.id.mainSearch);
		String word = editText.getText().toString();
		searchResults = new ArrayList<String>();
		
		//tmp list
		searchResults.add(word);
		searchResults.add(word);
		searchResults.add(word);
		searchResults.add(word);
		
		intent.putStringArrayListExtra("searchResults", (ArrayList<String>) searchResults);
		startActivity(intent);
		
		
	}
}
