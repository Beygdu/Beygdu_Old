package is.arnastofnun.beygdu;

import is.arnastofnun.parser.WordResult;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

		//TODO Remove TextView if "" .. or create TextView programatically if not ""
		//SetNote
		TextView note = (TextView) findViewById(R.id.note);
		if(!words.getNote().equals("")) {
			note.setText(words.getNote());
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
		private TableLayout tables;
		private WordResult words;
		
				
		/**
		 * @param context the context in which the fragment will be placed
		 * @param tables 
		 * @param word 
		 */
		public TableFragment(Context context, TableLayout tables, WordResult words) {
			this.context = context;
			this.tables = tables;
			this.words = words;
		}
		
		@SuppressWarnings("javadoc")
		public static TableFragment newInstance(Context context, TableLayout tables, WordResult words) {
	        TableFragment f = new TableFragment(context, tables, words);
	        return f;
	    }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.table,
					container, false);
			createTable();
//			setTableText(rootView);
			return rootView;
		}
		
		private void createTable() {
			TextView tableTitle = new TextView(context);
			tableTitle.setText(words.getTitle());
			tables.addView(tableTitle);
			
			//Iterate through blocks
			for (int block = 0; block < words.getBlocks().size(); block++){
				TextView blockTitle = new TextView(context);
				blockTitle.setText(words.getBlocks().get(block).getTitle());
				tables.addView(blockTitle);
				
				//Iterate through subBlocks
//				for (int row = 0; row < ; row++) {
//					TableRow tr = new TableRow(context);
//					tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//					for (int col = 0; col < word.colNum; col++) {
//						TextView cell = new TextView(context);
//						cell.setTextAppearance(context, R.style.BodyText);
//						String id = ""+row+col;
//						cell.setId(Integer.parseInt(id));
//						cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//						cell.setText("Cell"+row+col+"\t");
//						tr.addView(cell);
//					}
//					tables.addView(tr);
//				}
				
			}
				
			
		}		
	}
}
