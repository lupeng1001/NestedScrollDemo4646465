package example.oversroll;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.tuacy.nestedscrolldemo.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aa_activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_test){
			startActivity(new Intent(this, TestActivity.class));
		}
		if(item.getItemId() == R.id.action_refresh){
			startActivity(new Intent(this, RefreshActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
}
