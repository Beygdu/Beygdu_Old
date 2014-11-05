package is.arnastofnun.json;

import com.example.beygdu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 *
 * @version $Id:  $
 */
public class JsonActivity extends Activity {
	private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
	private EditText location,country;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);
		location = (EditText)findViewById(R.id.editText1);
	    country = (EditText)findViewById(R.id.editText2);
	}
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items 
	      //to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }

	   /**
	 * @param view
	 */
	public void open(View view){
	      String url = location.getText().toString();
	      String finalUrl = url1 + url;
	      country.setText(finalUrl);
	      HandleJSON obj = new HandleJSON(finalUrl);
	      obj.fetchJSON();

	      while(obj.parsingComplete);
	      country.setText(obj.getCountry());

	   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
}
