package com.tuacy.nestedscrolldemo.newtype_views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tuacy.nestedscrolldemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制技巧练习
 * @author lupeng
 */
public class TestDrawActivity extends AppCompatActivity {
    private static final String TAG = "TestDrawActivity";

    private TestAnimationTongJiView animate_view;
    private List<SaveData> list = new ArrayList<SaveData>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_draw);
        fillDataList();
        animate_view = (TestAnimationTongJiView)findViewById(R.id.animate_view);
        animate_view.setLocal_duration(1000);
        animate_view.setData_list(list);
    }

    public void fillDataList(){
        SaveData data1 = new SaveData();
        data1.setId(1);
        data1.setPercent(50f);
        data1.setAll_percent(50f);
        data1.setColor_value("#FF3746");
        data1.setCurrent_percent(0f);
        data1.setTotal_percent(0f);

        SaveData data2 = new SaveData();
        data2.setId(2);
        data2.setPercent(100f);
        data2.setAll_percent(100f);
        data2.setColor_value("#3299CC");
        data2.setCurrent_percent(50f);
        data2.setTotal_percent(50f);

        SaveData data3 = new SaveData();
        data3.setId(3);
        data3.setPercent(60f);
        data3.setAll_percent(60f);
        data3.setColor_value("#ffa500");
        data3.setCurrent_percent(150f);
        data3.setTotal_percent(150f);

        SaveData data4 = new SaveData();
        data4.setId(4);
        data4.setPercent(80f);
        data4.setAll_percent(80f);
        data4.setColor_value("#666666");
        data4.setCurrent_percent(210f);
        data4.setTotal_percent(210f);

        SaveData data5 = new SaveData();
        data5.setId(5);
        data5.setPercent(70f);
        data5.setAll_percent(70f);
        data5.setColor_value("#009900");
        data5.setCurrent_percent(290f);
        data5.setTotal_percent(290f);

        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
    }
}
