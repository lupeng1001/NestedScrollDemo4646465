package com.tuacy.nestedscrolldemo;

import android.os.Binder;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				String result = new Java2CJNI().Java2C();
				Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();
            }
        });

//		Parcel _data = Parcel.obtain();//从池子里取出一个Parcel对象
//		_data.writeInterfaceToken();
//		_data.readException();

	}





}
