package is.arnastofnun.beygdu;

import is.arnastofnun.parser.Nafnord;

import java.util.ArrayList;

import com.example.beygdu.R;

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
	private TableLayout tables;
	private Nafnord no = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_beygingar);
		
		Intent intent = getIntent();
		no = (Nafnord) intent.getSerializableExtra("NO");				
		tables = (TableLayout) findViewById(R.id.data_table);
		TextView titleDesc = (TextView) findViewById(R.id.tableTitle);
		titleDesc.setText(no.getEintala().get(0));
		getFragmentManager().beginTransaction().add(tables.getId(), TableFragment.newInstance(no.getEintala(), "Eintala"), 
													"table1").commit();
		getFragmentManager().beginTransaction().add(tables.getId(), TableFragment.newInstance(no.getFleirtala(), "Fleirtala"), 
													"table2").commit();	
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
		private ArrayList<String> content;
		private String description;
				
		/**
		 * @param content the content of the table
		 * @param description the description of the table.
		 * The constructor to build the table
		 */
		public TableFragment(ArrayList<String> content, String description) {
			this.content = content;
			this.description = description;
		}
		
		@SuppressWarnings("javadoc")
		public static TableFragment newInstance(ArrayList<String> content, String description) {
	        TableFragment f = new TableFragment(content, description);
	        return f;
	    }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.table_no,
					container, false);
			setTableText(rootView);
			return rootView;
		}
		
		/**
		 * @param view the view
		 * Sets the text in the TextViews in the table
		 */
		private void setTableText(View view) {
			int desID = getResources().getIdentifier("description", "id", "com.example.beygdu");
			((TextView) view.findViewById(desID)).setText(description);
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
