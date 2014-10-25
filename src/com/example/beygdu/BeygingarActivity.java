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
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author jfj1
 * @since 25.10.14
 * @version 0.2
 *
 */
public class BeygingarActivity extends Activity {

	TableLayout tables;
	private ArrayList<String> searchResults;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);
		
		Intent intent = getIntent();
		searchResults = intent.getStringArrayListExtra("searchResults");
		
		int numTables = searchResults.size() / 8;
		tables = (TableLayout) findViewById(R.id.data_table);
		ArrayList<String> tmp;
		for (int j = 0; j < numTables; j++) {
			tmp = new ArrayList<String>();
			for (int i = 0; i < 8; i++){
				tmp.add(searchResults.remove(0));				
			}
			getFragmentManager().beginTransaction().add(tables.getId(), TableFragment.newInstance(tmp), "table" + j).commit();
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
	public static class TableFragment extends Fragment {

		ArrayList<String> content;
				
		/**
		 * @param content the content of the table
		 * The constructor to build the table
		 */
		public TableFragment(ArrayList<String> content) {
			this.content = content;
		}
		
		public static TableFragment newInstance(ArrayList<String> content) {
	        TableFragment f = new TableFragment(content);;
	        return f;
	    }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.table,
					container, false);
			setTableText(rootView);
			return rootView;
		}
		
		/**
		 * @param view the view
		 * Sets the text in the TextViews in the table
		 */
		private void setTableText(View view) {
			int index = 0;
			for(int row=0; row < 4; row++) {
				   for(int col = 0; col < 2; col++) {
				    String cellID = "cell" + row + col;
				    int resID = getResources().getIdentifier(cellID, "id", "com.example.beygdu");
				    ((TextView) view.findViewById(resID)).setText(content.get(index));
				    index++;
				   }
			}
		}
		
	}
}
