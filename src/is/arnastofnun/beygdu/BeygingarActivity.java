package is.arnastofnun.beygdu;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.parser.WordResult;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.beygdu.R;

/**
 * @author Jón Friðrik Jónatansson
 * @since 25.10.14
 * @version 0.2
 *
 *BeygingarActivity inniheldur eitt TextView og eitt TableLayout. 
 */
public class BeygingarActivity extends Activity {

	/**
	 * tables er tablelayout activitiesins, hér fara TableFragmentin.
	 * no - er container fyrir Nafnorð. 
	 */
	private TableLayout tableLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);
		
		Intent intent = getIntent();
		WordResult words = (WordResult) intent.getSerializableExtra("word");
		tableLayout = (TableLayout) findViewById(R.id.data_table);
		
		
		//SetTitle
		TextView titleDesc = (TextView) findViewById(R.id.tableTitle);
		titleDesc.setText(words.getTitle());

		//SetNote
		if(!words.getNote().equals("")) {
			TextView note = new TextView(this);
			note.setText(words.getNote());
			tableLayout.addView(note);
		}
		
		getFragmentManager().beginTransaction().add(tableLayout.getId(), TableFragment.newInstance(BeygingarActivity.this, tableLayout, words)).commit();		
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
	

	/**
	 * @author Jón Friðrik Jónatansson
	 * @since 20.10.14
	 * @version 0.2
	 * A placeholder fragment containing a simple view.
	 */
	public static class TableFragment extends Fragment {

		/**
		 * content er arraylisti af þeim strengjum, í réttri röð, sem eiga að birtast
		 * description - er nafn töflunar.
		 */
		private Context context;
		private TableLayout tableLayout;
		private WordResult words;
		
				
		/**
		 * @param context the context in which the fragment will be placed
		 * @param tableLayout
		 * @param tables
		 */
		public TableFragment(Context context, TableLayout tableLayout, WordResult words) {
			this.context = context;
			this.tableLayout = tableLayout;
			this.words = words;
		}
		
		@SuppressWarnings("javadoc")
		public static TableFragment newInstance(Context context, TableLayout tableLayout, WordResult words) {
	        TableFragment f = new TableFragment(context, tableLayout, words);
	        return f;
	    }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.table,
					container, false);
			createBlock();
			return rootView;
		}
		
		private void createBlock() {
			//Iterate through blocks and set title
			for (Block block : words.getBlocks()) {
				if(!block.getTitle().equals("")) {
					TextView blockTitle = new TextView(context);
					blockTitle.setText(block.getTitle());
					tableLayout.addView(blockTitle);
				}	
				//Iterate through sub-blocks and set title
				for (SubBlock sBlock: block.getBlocks()){
					if(!sBlock.getTitle().equals("")) {
						TextView subBlockTitle = new TextView(context);
						subBlockTitle.setText(sBlock.getTitle());
						tableLayout.addView(subBlockTitle);
					}
					//Create the tables and set title
					for (Tables tables : sBlock.getTables()) {
						if(!tables.getTitle().equals("")) {
							TextView tableTitle = new TextView(context);
							tableTitle.setText(tables.getTitle());
							tableLayout.addView(tableTitle);
						}
						createTable(tables);
					}
				}				
			}
		}	
		
		private void createTable(Tables tables) {
			int rowNum = tables.getRowNames().length;
			int colNum = tables.getColumnNames().length;
			
			TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(10);
			tableRowParams.setMargins(1, 1, 1, 1);
			tableRowParams.weight = 1;
			tableRowParams.height = 100;
			
			int contentIndex = 0;
			for (int row = 0; row < rowNum; row++) {
				TableRow tr = new TableRow(context);
				
				
				tr.setLayoutParams(tableRowParams);
				
				tr.setBackgroundColor(Color.BLACK);
				for (int col = 0; col < colNum; col++) {
					TextView cell = new TextView(context);
					cell.setTextAppearance(context, R.style.BodyText);
					cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
					cell.setGravity(Gravity.CENTER);
					cell.setBackgroundColor(Color.LTGRAY);
					cell.setTextColor(Color.WHITE);
					if (row == 0) {
						cell.setText(tables.getColumnNames()[col]);
					} else {
						if (col == 0) {
							cell.setText(tables.getRowNames()[row]);
						} else {
							cell.setText(tables.getContent().get(contentIndex++));
						}
					}
					tr.addView(cell);
				}
				tableLayout.addView(tr);
			}
			
		}
	}
}
