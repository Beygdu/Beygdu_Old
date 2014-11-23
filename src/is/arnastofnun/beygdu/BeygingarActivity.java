package is.arnastofnun.beygdu;

import is.arnastofnun.fragments.TableFragment;
import is.arnastofnun.parser.WordResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
public class BeygingarActivity extends Activity {

	/**
	 * tables er tablelayout activitiesins, hér fara TableFragmentin.
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
		
		getFragmentManager().beginTransaction().add(tableLayout.getId(), new TableFragment(BeygingarActivity.this, tableLayout, words)).commit();		
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
}
