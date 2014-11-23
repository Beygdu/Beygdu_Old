package is.arnastofnun.beygdu;

import is.arnastofnun.fragments.TableFragment;
import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.WordResult;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.beygdu.R;

/**
 * @author Jón Friðrik Jónatansson
 * @since 25.10.14
 * @version 0.2
 *
 *BeygingarActivity inniheldur eitt TextView og eitt TableLayout. 
 */
public class BeygingarActivity extends FragmentActivity {

	/**
	 * tables er tablelayout activitiesins, hér fara TableFragmentin.
	 */
	private TableLayout tableLayout;
	private ArrayList<TableFragment> tables = new ArrayList<TableFragment>();
	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
	WordResult words;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);

		Intent intent = getIntent();
		words = (WordResult) intent.getSerializableExtra("word");
		tableLayout = (TableLayout) findViewById(R.id.data_table);
		
		for (int i = 0; i < words.getBlocks().size(); i++) {
			mSelectedItems.add(i);
		}
		
		initTables();
	}
	
	private void initTables(){
		//SetTitle
		TextView titleDesc = new TextView(this);
		titleDesc.setText(words.getTitle());
		tableLayout.addView(titleDesc);

		//SetNote
		if(!words.getNote().equals("")) {
			TextView note = new TextView(this);
			note.setText(words.getNote());
			tableLayout.addView(note);
		}


		//Iterate through blocks and set title
		//for (Block block : words.getBlocks()) {
		for (int i = 0; i < words.getBlocks().size(); i++){
			if (mSelectedItems.contains(i)) {
				Block block = words.getBlocks().get(i);
				TextView blockTitle = new TextView(this);
				blockTitle.setText(block.getTitle());
				
				TableFragment tFragment = new TableFragment(BeygingarActivity.this, tableLayout, block, blockTitle);
				getFragmentManager().beginTransaction().add(tableLayout.getId(), tFragment).commit();
				tables.add(tFragment);				
			}
		}
	}
	
	/**
	 * @param view The activies view.
	 */
	public void filterBtnOnClick(@SuppressWarnings("unused") View view){
		FragmentManager fM = getSupportFragmentManager();
		DialogFragment newFragment = new TableChooserDialogFragment();
		newFragment.show(fM, "tableChooserFragment");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beygingar, menu);
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
	
	public void updateFragments() {
		tableLayout.removeAllViews();
		initTables();
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 0.1
	 * 
	 * Úbýr Dialog þar sem notandinn þarf að velja <strong>eitt</strong> af þeim orðum sem eru í results Arraylistanum
	 * eða fara tilbaka.
	 */
	public class TableChooserDialogFragment extends DialogFragment {

		/**
		 * selectedItem - það stak sem er valið í Dialognum, fyrsta stikið á listanum ef ekkert er valið.
		 */

		private CharSequence[] charArr;

		/**
		 *  Smiður fyrir WordChooserDialog.
		 *  Dialog þar sem notandi getur valið um leitarniðurstöður.
		 *  Einungis hægt að velja eitt orð.
		 */
		public TableChooserDialogFragment() {
			makeCharArr();
		}
		
		private void makeCharArr() {
			charArr = new CharSequence[tables.size()];
			for (int i = 0; i < charArr.length; i++){
				charArr[i] = tables.get(i).getTitle();
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			mSelectedItems = new ArrayList<Integer>();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);			

			builder.setMultiChoiceItems(charArr, null,
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
					
				}
			});

			return builder.create();
		}	
	}
}
