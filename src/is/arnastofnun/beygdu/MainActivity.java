package is.arnastofnun.beygdu;

import is.arnastofnun.json.JsonActivity;
import is.arnastofnun.parser.HTMLParser;
import is.arnastofnun.parser.Nafnord;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

import com.example.beygdu.R;

/**
 * @author Jón Friðrik, Arnar, Snær, Máni
 * @since 05.11.14
 * @version 0.2
 */
public class MainActivity extends FragmentActivity {

	/**
	 * The result from the parser search.
	 */
	public ArrayList<String> results = new ArrayList<String>();

	/**
	 * @param result setur results inní tilviksbreytu klasans.
	 */
	public void setOrd(ArrayList<String> results) {
		this.results = results;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @return item 
	 *  switch fyrir möguleika í ActionBar glugga. 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			break;
			
			//JSON test
//		case R.id.tabs:
//			Intent intent2 = new Intent(this, JsonActivity.class);
//			startActivity(intent2);
//			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * @param view the view points to this activity. 
	 * Keyrist þegar notandi ýtir á leita takkann.
	 * Býr til nýjan þráð sem sér um að ná í beygingarmynd þess orðs sem var slegið inn.
	 * Ef ekkert eða fleirri en eitt orð var slegið inn fær notandinn áminningu.
	 * 
	 */
	public void btnOnClick(View view){
		EditText editText = (EditText) findViewById(R.id.mainSearch);
		String word = editText.getText().toString();
		if(word.contains(" ")){
			Toast.makeText(this, "Einingis hægt að leita að einu orði í einu", Toast.LENGTH_SHORT).show();
		}
		if(word.isEmpty()){
			Toast.makeText(this, "Vinsamlegasta sláið inn orð í reitinn hér að ofan", Toast.LENGTH_SHORT).show();
		}
		//New Thread to get word
		new ParseThread(word).execute();
	}
	
	private void checkWordCount() {
		if (results.get(0).equalsIgnoreCase("partialHit")) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new WordChooserDialogFragment();
			newFragment.show(fM, "wordChooserFragment");
		} else if(results.get(0).equalsIgnoreCase("criticalHit")){
			//TODO: Tímabundið þar til við búum fleirri klasa fyrir aðra orðflokka
			Nafnord no = new Nafnord(results);
			createNewActivity(no);
		} else {
			Toast.makeText(this, "Engin leitarniðurstaða", Toast.LENGTH_SHORT).show();
		}
	}

	private void createNewActivity(Nafnord no) {
		Intent intent = new Intent(this, BeygingarActivity.class);
		intent.putExtra("NO", no);
		startActivity(intent);
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 0.1
	 */
	public class WordChooserDialogFragment extends DialogFragment {
		private String selectedItem = null;
		private Context mContext;
		private CharSequence[] charArr;


		/**
		 *  Smiður fyrir WordChooserDialog.
		 *  Dialog þar sem notandi getur valið um leitarniðurstöður.
		 *  Einungis hægt að velja eitt orð.
		 *  
		 */
		public WordChooserDialogFragment() {
			mContext= getActivity();
			makeCharArr();
		}

		private void makeCharArr() {
			charArr = new CharSequence[results.size()-1];
			for (int i = 0; i < results.size()-1; i++){
				charArr[i] = results.get(i+1).substring(0, results.get(i+1).indexOf("-"));
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
					selectedItem = results.get(which+1);
				}
			});


			builder.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					int wordId = Integer.parseInt(selectedItem.split("- ")[1]);
					new ParseThread(wordId).execute();
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

	
	/**
	 * 
	 * @author Arnar, Jón Friðrik
	 * @since 23.10.14
	 * @version 0.2
	 * 
	 */
	private class ParseThread extends AsyncTask<Void, Void, Void> {
		HTMLParser parser;
		String url;
		
		/**
		 * 
		 * @param searchWord
		 */
		public ParseThread(String searchWord) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?q=";
			url = baseURL + searchWord;
		}
		
		/**
		 * 
		 * @param searchId
		 */
		public ParseThread(int searchId) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?id=";
			url = baseURL + searchId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... args) {
			Document doc;
			try {
				//url = java.net.URLDecoder.decode(url, "UTF-8");
				//url = Charset.forName("UTF-8").encode(url);
				//System.out.println(url);
				doc = Jsoup.connect(url).get();
				parser = new HTMLParser(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			setOrd(parser.getResults());
			checkWordCount();
		}
	}
}