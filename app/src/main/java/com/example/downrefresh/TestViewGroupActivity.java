package com.example.downrefresh;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.downrefresh.new_part_views.WordWrapView;
import com.tuacy.nestedscrolldemo.R;

public class TestViewGroupActivity extends AppCompatActivity {
    private static final String TAG = "TestViewGroupActivity";
    private WordWrapView view_wordwrap;
    private LayoutInflater minflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view_group);
        //int[] new_array = bubbleSort(new int[]{25,35,1,58,36,589});
        int[] new_array = chooseSort(new int[]{25,35,1,58,36,589});
        for(int i=0;i<new_array.length;i++){
            Log.e(TAG,new_array[i]+",");
        }


//        view_wordwrap = (WordWrapView)findViewById(R.id.view_wordwrap);
//        minflater = LayoutInflater.from(this);
//        LinearLayout layout1 = (LinearLayout) minflater.inflate(R.layout.test_view,null);
//        TextView view1 =(TextView) layout1.findViewById(R.id.text_bu);
//        view1.setText("我一定是孩子");
//        view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view1.setBackgroundColor(getResources().getColor(R.color.white));
//
//        LinearLayout layout2 = (LinearLayout) minflater.inflate(R.layout.test_view,null);
//        TextView view2 =(TextView) layout2.findViewById(R.id.text_bu);
//        view2.setText("好好孩子");
//        view2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view2.setBackgroundColor(getResources().getColor(R.color.green));
//
//        LinearLayout layout3 = (LinearLayout) minflater.inflate(R.layout.test_view,null);
//        TextView view3 =(TextView) layout3.findViewById(R.id.text_bu);
//        view3.setText("好孩子");
//        view3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view3.setBackgroundColor(getResources().getColor(R.color.red));
//
//        LinearLayout layout4 = (LinearLayout) minflater.inflate(R.layout.test_view,null);
//        TextView view4 =(TextView) layout4.findViewById(R.id.text_bu);
//        view4.setText("我是好孩子");
//        view4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view4.setBackgroundColor(getResources().getColor(R.color.color_999999));
//
//
//        LinearLayout layout5 = (LinearLayout) minflater.inflate(R.layout.test_view,null);
//        TextView view5 =(TextView) layout5.findViewById(R.id.text_bu);
//        view5.setText("我一定是一个很好的孩子");
//        view5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//        view5.setBackgroundColor(getResources().getColor(R.color.zhyp_middle_black));
//
//        view_wordwrap.addView(layout5);
//        view_wordwrap.addView(layout2);
//        view_wordwrap.addView(layout3);
//        view_wordwrap.addView(layout4);
//        view_wordwrap.addView(layout1);

        //动态调整字体大小
//        float padding = 10;//px
//        float maxWidth = (500 - padding*2)/2;
//        reSizeTextView(view1,"我一定是一个很好很好很好很好很好的孩子",maxWidth);



//        for(int i=0;i<5;i++){
//            View view = minflater.inflate(R.layout.test_view,null);
//            Button text_button = (Button) view.findViewById(R.id.text_button);
//            text_button.setText("");
//        }

    }

    /**
     * 冒泡排序
     */
    public int[] bubbleSort(int[] array){
        for(int i=0;i<array.length-1;i++){//外层是排序次数
            boolean flag = false;
            for(int j=array.length-1;j>i;j--){//从最后一个元素开始两两比较
                    if(array[j]<array[j-1]){
                        int temp = array[j];
                        array[j] = array[j-1];
                        array[j-1] = temp;
                        flag = true;
                    }
            }
            if(!flag){
                break;
            }
        }
        return array;
    }

    /**
     * 选择排序
     */
    public int[] chooseSort(int[] array){
        for(int i=0;i<array.length-1;i++){
            int minIndex = i;//先假设最小的元素是i
            for(int j=i+1;j<array.length;j++){//从i+1开始，依次把后面的元素拿出来和minindex作对比
                if(array[j]<array[minIndex]){
                    minIndex = j;
                }
            }
            if(minIndex!=i){
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
            }

        }
        return array;
    }





    private void reSizeTextView(TextView textView, String text, float maxWidth){
        Paint paint = textView.getPaint();
        float textWidth = paint.measureText(text);
        int textSizeInDp = 30;
        if(textWidth > maxWidth){
            for(;textSizeInDp > 0; textSizeInDp--){
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeInDp);
                paint = textView.getPaint();
                textWidth = paint.measureText(text);
                if(textWidth <= maxWidth){
                    break;
                }
            }
        }
        textView.invalidate();
    }


}
