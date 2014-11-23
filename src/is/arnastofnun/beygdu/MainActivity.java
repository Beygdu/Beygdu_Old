package is.arnastofnun.beygdu;

import is.arnastofnun.parser.HTMLParser;
import is.arnastofnun.parser.ParserResult;
import is.arnastofnun.parser.WordResult;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.AlertDialog;
import android.app.Dialog;
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
 * 
 * Fyrsti skjárinn í forritinu. Inniheldur innsláttarsvæði og takka fyrir leit.
 * Inniheldur einnig actionbar þar sem notandinn getur opnað önnur activity eins og AboutActivity.
 * 
 */
public class MainActivity extends FragmentActivity {

	/**
	 * The result from the parser search.
	 */
	public ParserResult PR = new ParserResult();
	
	public void setParserResult(ParserResult a) {
		this.PR = a;
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
			new ParseThread(word).execute();
		}
	}
	
	/**
	 * sér hvort results sé:
	 * <strong>Partial hit: </strong> séu mörg orð með mismunandi merkingar, en eins skrifuð
	 * <strong>Critical hit: </strong> orðið fundið og búið að fylla niðurstöðum í object.
	 * Eða engin leitarniðurstaða.
	 * 
	 */
	private void checkWordCount() {
		String pr = PR.getType();
		if (pr.equals("Multiple results")) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new WordChooserDialogFragment();
			newFragment.show(fM, "wordChooserFragment");
		} else if (pr.equals("Single result")) {
			WordResult word = PR.getWordResult();
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
		 * selectedItem - það stak sem er valið í Dialognum, fyrsta stikið á listanum ef ekkert er valið.
		 */
		private String selectedItem = null;
		private CharSequence[] charArr;

		/**
		 *  Smiður fyrir WordChooserDialog.
		 *  Dialog þar sem notandi getur valið um leitarniðurstöður.
		 *  Einungis hægt að velja eitt orð.
		 */
		public WordChooserDialogFragment() {
			makeCharArr();
		}

		/**
		 * Breytir results ArrayListanum í CharSequence fylki
		 */
		private void makeCharArr() {
			charArr = new CharSequence[PR.getDesc().length];
			for (int i = 0; i < PR.getDesc().length; i++){
				charArr[i] = PR.getDesc()[i];
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
					selectedItem = PR.getIds()[which]+"";
				}
			});

			builder.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				/**
				 * listener hlustar á ef notandinn ýtir á afram takkann
				 */
				public void onClick(DialogInterface dialog, int id) {
					if( selectedItem != null) {
						int wordId = Integer.parseInt(selectedItem);
						new ParseThread(wordId).execute();
					} else {
						//TODO: make clever error handling here:Toast.makeText(mContext, "Vinsamlegast veljið orð", Toast.LENGTH_SHORT).show();
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
		private ParserResult PR;
		private String url;
		
		/**
		 * 
		 * @param searchWord -strengurinn sem á að skeita aftast á urlinn
		 */
		public ParseThread(String searchWord) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?q=";
			url = baseURL + searchWord;
		}
		
		/**
		 * @param searchId - heiltalan sem á að skeita aftast á urlinn.
		 */
		public ParseThread(int searchId) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?id=";
			url = baseURL + searchId;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * Fallið sem þráðurinn keyrir þegar hann er búinn að keyra doInBackground
		 */
		@Override
		protected void onPostExecute(Void args) {
			setParserResult(parser.getParserResult());
			checkWordCount();
		}
	}
}