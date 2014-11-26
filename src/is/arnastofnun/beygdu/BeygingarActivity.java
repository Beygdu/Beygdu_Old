package is.arnastofnun.beygdu;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.WordResult;
import is.arnastofnun.utils.TableFragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beygdu.R;

/**
 * @author Jón Friðrik Jónatansson
 * @since 25.10.14
 * @version 1.0
 *
 *BeygingarActivity inniheldur LinearLayout með ScrollViewi sem inniheldur TableLayout.
 *Töflurnar eru síðan framleiddar inní TableLayoutið.
 */
public class BeygingarActivity extends FragmentActivity {

	/**
	 * tableLayout er Tablelayout activitiesins, hér inn fara öll TextView og TableFragmentin.
	 * tables er ArrayListi sem inniheldur þær töflur sem eru sýndar í TableLayoutinu.
	 * blockNames er ArrayListi sem inniheldur titil allra blocka bæði sýnilega og ósýnlega.
	 * mSelectedItems er ArrayListi sem inniheldur indexa taflana sem notandi hefur valið úr TableChooserDialogFragmentinu 
	 * words - er niðurstaðan frá parsernum.
	 */
	private TableLayout tableLayout;
	private ArrayList<TableFragment> tables = new ArrayList<TableFragment>();
	private ArrayList<String> blockNames = new ArrayList<String>();
	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
	private WordResult words;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//get WordResult from MainActivity.
		Intent intent = getIntent();
		words = (WordResult) intent.getSerializableExtra("word");
		tableLayout = (TableLayout) findViewById(R.id.data_table);
		
		//fill mSelectedItems with all possible blocks
		for (int i = 0; i < words.getBlocks().size(); i++) {
			mSelectedItems.add(i);
			blockNames.add(words.getBlocks().get(i).getTitle());
		}
		initTables();
	}
	
	/**
	 * Býr til TextView með titil á word og TextView með athugasemd um orð ( ef á við) og setur í tableLayoutið.
	 * smíðar síðan Tablefragment fyrir hverja block sem word inniheldur. 
	 */
	private void initTables(){
		//SetTitle
		TextView titleDesc = new TextView(this);
		titleDesc.setText(words.getTitle());
		titleDesc.setGravity(Gravity.CENTER);
		titleDesc.setTextSize(40);
		titleDesc.setHeight(130);
		titleDesc.setTypeface(Typeface.DEFAULT_BOLD);
		tableLayout.addView(titleDesc);
		
		//SetNote
		if(!words.getNote().equals("")) {
			TextView note = new TextView(this);
			note.setText(words.getNote());
			note.setBackgroundResource(R.drawable.noteborder);
			tableLayout.addView(note);
		}

		//Iterate through blocks and set title
		tables.clear();
		for (int i = 0; i < words.getBlocks().size(); i++){
			if (mSelectedItems.contains(i)) {
				Block block = words.getBlocks().get(i);
				TextView blockTitle = new TextView(this);
				blockTitle.setTextSize(30);
				blockTitle.setHeight(60);
				blockTitle.setText(block.getTitle());
				
				TableFragment tFragment = new TableFragment(BeygingarActivity.this, tableLayout, block, blockTitle);
				getFragmentManager().beginTransaction().add(tableLayout.getId(), tFragment).commit();
				tables.add(tFragment);				
			}
		}
	}
	
	/**
	 * if word contains more than one block a TableChooserDialogFragment is constructed.
	 */
	public void filterAction(){
		if (words.getBlocks().size() > 1) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new TableChooserDialogFragment();
			newFragment.show(fM, "tableChooserFragment");
		} else {
			Toast.makeText(BeygingarActivity.this, 
					"Ekkert til að sía.", Toast.LENGTH_SHORT).show();
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
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_filter:
			filterAction();
			break;
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

	protected void sendEmail() {
		Log.i("Senda post", "");
		String[] TO = {"sth132@hi.is"};
		String[] CC = {"sth132@hi.is"};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Titt vidfang");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Skilabod her");
		try {
			startActivity(Intent.createChooser(emailIntent, "Sendu post....."));
			finish();
			Log.i("Buin ad senda post...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(BeygingarActivity.this, 
					"Engin póst miðill uppsettur.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * tableLayout is cleared and new Table is constructed.
	 */
	public void updateFragments() {
		tableLayout.removeAllViews();
		initTables();
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 1.0
	 * 
	 * Úbýr Dialog þar sem notandinn getur valið mörg ekkert, eitt eða fleirri blocks til að sýna í tablelayoutinu.
	 */
	public class TableChooserDialogFragment extends DialogFragment {

		/**
		 * charArr - er Charsequence fylki með titil allra blocka
		 */

		private CharSequence[] charArr;

		/**
		 *  Smiður fyrir TableChooserDialogFragment.
		 *  Dialog þar sem notandi getur valið um töflur.
		 */
		public TableChooserDialogFragment() {
			makeCharArr();
		}
		
		/**
		 * fyllir charArr af block titlum.
		 */
		private void makeCharArr() {
			charArr = new CharSequence[blockNames.size()];
			for (int i = 0; i < charArr.length; i++){
				charArr[i] = blockNames.get(i);
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);			
			boolean[] prevChoices = new boolean[blockNames.size()];
			for (int i = 0; i < blockNames.size(); i++) {
				if (mSelectedItems.contains(i)){
					prevChoices[i] = true; 
				} else {
					prevChoices[i] = false;
				}
			}
			
			builder.setMultiChoiceItems(charArr, prevChoices,
					new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which,
						boolean isChecked) {
					if (isChecked) {
						// If the user checked the item, add it to the selected items
						mSelectedItems.add(which);
					} else if (mSelectedItems.contains(which)) {
						// Else, if the item is already in the array, remove it 
						mSelectedItems.remove(Integer.valueOf(which));
					}
				}
			})
			// Set the action buttons
			.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					updateFragments();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//Ekkert gerist
				}
			});
			return builder.create();
		}	
	}
}
