package example.oversroll;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import com.tuacy.nestedscrolldemo.R;

import example.oversroll.widget.OverScrollView;

public class RefreshActivity extends Activity implements OverScrollView.OverScrollListener, OverScrollView.OverScrollTinyListener {


	private static final int PADDING = -100;
	
	private ImageView mHeaderImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh);
		
		mHeaderImage = (ImageView) findViewById(R.id.image);
		
		OverScrollView scrollView = (OverScrollView) findViewById(R.id.layout);
		
		scrollView.setOverScrollListener(this);
		scrollView.setOverScrollTinyListener(this);
		

		scrollLoosen();
	}

	@Override
	public void scrollDistance(int tinyDistance, int totalDistance) {
		if (totalDistance > 0 || tinyDistance == 0
				|| mHeaderImage.getPaddingBottom() == 0) {
			return;
		}
		int padding = PADDING - totalDistance / 2;
		if (padding > 0) {
			padding = 0;
		}
		mHeaderImage.setPadding(padding, 0, padding, padding);
	}

	@Override
	public void scrollLoosen() {
		int padding = PADDING;
		mHeaderImage.setPadding(padding, 0, padding, padding);
		

	}

	@Override
	public void headerScroll() {
		Toast.makeText(getApplicationContext(), "好好好", Toast.LENGTH_SHORT).show();
		

	}

	@Override
	public void footerScroll() {

	}

}
