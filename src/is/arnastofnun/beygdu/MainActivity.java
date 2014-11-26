package is.arnastofnun.beygdu;

import is.arnastofnun.parser.HTMLParser;
import is.arnastofnun.parser.ParserResult;
import is.arnastofnun.parser.WordResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
 * @version 1.0
 * 
 * Fyrsti skjárinn í forritinu. Inniheldur innsláttarsvæði og takka fyrir leit.
 * Inniheldur einnig actionbar þar sem notandinn getur opnað önnur activity eins og AboutActivity 
 * og send póst á með kvörtun eða ábendingu.
 * 
 */
public class MainActivity extends FragmentActivity {

	/**
	 * The result from the parser search.
	 */
	public ParserResult pR = new ParserResult();

	/**
	 * @param pR the parser results.
	 */
	public void setParserResult(ParserResult pR) {
		this.pR = pR;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkNetworkState();
	}
	
	/**
	 * Checks if the user is connected to a network.
	 * TODO - Should be implemented so that it shows a dialog if 
	 * the user is not connected
	 */
	private void checkNetworkState() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			Toast.makeText(MainActivity.this, 
					"Þú ert ekki nettengdur.", Toast.LENGTH_LONG).show();
		}
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
		case R.id.action_about:
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			break;
		case R.id.action_mail:
			sendEmail();
			break;
		} 
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Sendir póst á sth132@hi.is
	 */
	protected void sendEmail() {
		Log.i("Senda post", "");
		String[] TO = {"sth132@hi.is"};
		String[] CC = {"sth132@hi.is"};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Þitt vidfang");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Skilabod her");
		try {
			startActivity(Intent.createChooser(emailIntent, "Sendu post....."));
			finish();
			Log.i("Buin ad senda post...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this, 
					"Engin póst miðill uppsettur í þessu tæki.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * @param view the view points to this activity. 
	 * Keyrist þegar notandi ýtir á leita takkann.
	 * Býr til nýjan þráð sem sér um að ná í beygingarmynd þess orðs sem var slegið inn.
	 * Ef ekkert eða fleirri en eitt orð var slegið inn fær notandinn áminningu.
	 * 
	 */
	public void btnOnClick(@SuppressWarnings("unused") View view){
		EditText editText = (EditText) findViewById(R.id.mainSearch);
		String word = editText.getText().toString();
		if(word.contains(" ")){
			Toast.makeText(this, "Einingis hægt að leita að einu orði í einu", Toast.LENGTH_SHORT).show();
		}
		if(word.isEmpty()){
			Toast.makeText(this, "Vinsamlegasta sláið inn orð í reitinn hér að ofan", Toast.LENGTH_SHORT).show();
		} else {
			//New Thread to get word
			word = convertToUTF8(word);
			new ParseThread(word).execute();
		}
	}
	
	/**
	 * @param word the searchword
	 * @return word converted to UTF-8
	 */
	private String convertToUTF8(String word) {
		try {
			word = URLEncoder.encode(word, "UTF-8");
			return word;
		}
		catch( UnsupportedEncodingException e ) {
			return "";
		}
	}

	/**
	 * sér hvort results sé:
	 * <strong>Partial hit: </strong> séu mörg orð með mismunandi merkingar, en eins skrifuð
	 * <strong>Critical hit: </strong> orðið fundið og búið að fylla niðurstöðum í object.
	 * Eða engin leitarniðurstaða.
	 */
	private void checkWordCount() {
		String pr = pR.getType();
		if (pr.equals("Multiple results")) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new WordChooserDialogFragment();
			newFragment.show(fM, "wordChooserFragment");
		} else if (pr.equals("Single result")) {
			WordResult word = pR.getWordResult();
			createNewActivity(word);
		} else if (pr.equals("Word not found")) {
			Toast.makeText(this, "Engin leitarniðurstaða", Toast.LENGTH_SHORT).show();
		}
	}

	private void createNewActivity(WordResult word) {
		Intent intent = new Intent(this, BeygingarActivity.class);
		intent.putExtra("word", word);
		startActivity(intent);
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 0.1
	 * 
	 * Úbýr Dialog þar sem notandinn þarf að velja <strong>eitt</strong> af þeim orðum sem eru í results Arraylistanum
	 * eða fara tilbaka.
	 */
	public class WordChooserDialogFragment extends DialogFragment {

		/**
		 * selectedItem - það stak sem er valið í Dialognum, fyrsta stakið á listanum ef ekkert er valið.
		 * charArr - Eru þau orð sem ParseThread skilar ef partialHit.
		 */
		private String selectedItem = null;
		private CharSequence[] charArr;

		/**
		 *  Smiður fyrir WordChooserDialog.
		 *  Dialog þar sem notandi getur valið um leitarniðurstöður.
		 *  Einungis hægt að velja eitt orð.
		 */
		public WordChooserDialogFragment() {
			toCharArr();
		}

		/**
		 * Breytir results ArrayListanum í CharSequence fylki
		 */
		private void toCharArr() {
			charArr = new CharSequence[pR.getDesc().length];
			for (int i = 0; i < pR.getDesc().length; i++){
				charArr[i] = pR.getDesc()[i];
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);

			builder.setSingleChoiceItems(charArr, -1 , 
					new DialogInterface.OnClickListener() {
				/**
				 * listener hlustar á það atriði sem notandinn velur í dialognum
				 */
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedItem = pR.getIds()[which]+"";
				}
			});

			builder.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				/**
				 * listener hlustar á ef notandinn ýtir á afram takkann.
				 * Ef ekkert er valið og notandi ýtir á áfram þá lokast Dialoginn.
				 */
				public void onClick(DialogInterface dialog, int id) {
					if( selectedItem != null) {
						int wordId = Integer.parseInt(selectedItem);
						new ParseThread(wordId).execute();
					}
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				/**
				 * listener hlustar á ef notandi ýtir á cancel takka, engin virkni þar sem hún er óþörf
				 */
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
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
		/**
		 * parser - parserinn sem er búinn til að til ná í gögnin
		 * url - urlinn sem parserinn notar. 
		 */
		private HTMLParser parser;
		private String url;

		/**
		 * 
		 * @param searchWord -strengurinn sem á að skeita inn í urlinn.
		 * Búið að converta searchWord strengnum í UTF-8 streng. 
		 * Má leita af hvaða orðmynd.
		 */
		public ParseThread(String searchWord) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?q=";
			url = baseURL + searchWord + "&ordmyndir=on";
		}

		/**
		 * @param searchId - heiltalan sem á að skeita inn í urlinn.
		 */
		public ParseThread(int searchId) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?id=";
			url = baseURL + searchId + "&ordmyndir=on";
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * fallið sem þráðurinn keyrir eftir að hann er smíðaður. 
		 */
		@Override
		protected Void doInBackground(Void... args) {
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
				parser = new HTMLParser(doc);
			} catch (IOException e) {
				Toast.makeText(MainActivity.this, 
						"Tenging rofnaði, vinsamlega reynið aftur.", Toast.LENGTH_LONG).show();
			}
			return null;
		}
		/**
		 * Fallið sem þráðurinn keyrir þegar hann er búinn að keyra doInBackground.
		 * setur síðan ParserResults í MainActivity
		 */
		@Override
		protected void onPostExecute(Void args) {
			setParserResult(parser.getParserResult());
			checkWordCount();
		}
	}
}