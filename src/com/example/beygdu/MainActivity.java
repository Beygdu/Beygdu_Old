package com.example.beygdu;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author jfj1
 * @since 09.10.14
 * @version 0.1
 */
public class MainActivity extends FragmentActivity {

	/**
	 * The result from the database search.
	 */
	public static ArrayList<String> ord = new ArrayList<String>();
	public static ArrayList<String> ordFlokkar = new ArrayList<String>();
	public static ArrayList<String> beygingar = new ArrayList<String>();

	public void setOrd(ArrayList<String> ord) {
		this.ord = ord;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//Notkun:onOptionsItemSelected(MenuItem item);
	//Fyrir:
	//Eftir:S�r um klikk fyrir �nnur activity
	/**
	 * @return item 
	 * @param MenuItem item
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			break;
		case R.id.tabs:
			Intent intent2 = new Intent(this, JsonActivity.class);
			startActivity(intent2);
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * @param view the view
	 * Fills the List with search results and makes Intent which is sent to new Activity.
	 */
	public void btnOnClick(View view){
		EditText editText = (EditText) findViewById(R.id.mainSearch);
		String word = editText.getText().toString();
		if(word.contains(" ")){
			Toast.makeText(this, "Einingis hægt að leita að einu orði í einu", Toast.LENGTH_SHORT).show();
		}
		//New Thread to get word
		new Parse(word).execute();
	}
	
	private void checkWordCount() {
		if (ord.size() > 1) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new WordChooserDialogFragment();
			newFragment.show(fM, "wordChooserFragment");
		} else {
			createNewActivity();
		}
	}

	private void createNewActivity() {
		Intent intent = new Intent(this, BeygingarActivity.class);
		beygingar = new ArrayList<String>();
		intent.putStringArrayListExtra("searchResults", (ArrayList<String>) beygingar);
		startActivity(intent);
	}

	/**
	 *
	 * @version 0.1
	 */
	public static class WordChooserDialogFragment extends DialogFragment {
		private String selectedItem = null;
		private Context mContext;
		private CharSequence[] charArr;


		/**
		 *  
		 */
		public WordChooserDialogFragment() {
			mContext= getActivity();
			makeCharArr();
		}

		private void makeCharArr() {
			charArr = new CharSequence[ord.size()-1];
			for (int i = 0; i < ord.size()-1; i++){
				charArr[i] = ord.get(i+1).substring(0, ord.get(i+1).indexOf("-"));
				//charArr[i] = ord.get(i);
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);

			builder.setSingleChoiceItems(charArr, -1 , 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedItem = ord.get(which);
				}
			});


			builder.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// FIRE ZE MISSILES!
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}	
	}

	private class Parse extends AsyncTask<Void, Void, Void> {

		ArrayList<String> results;
		String search;
		GetWordDetails gW;

		public Parse(String search) {
			this.search = search;
			this.results = new ArrayList<String>();
			gW = new GetWordDetails(search);
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... args) {
			gW.searchURL(search);
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			setOrd(gW.getResults());
			checkWordCount();
		}
	}
}
