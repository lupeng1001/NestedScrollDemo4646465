package cn.tsou.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tuacy.nestedscrolldemo.R;


/**
 * 自定义dialog封装
 * @author lupeng
 * @Company 天搜科技
 * @date 2017/8/10
 */
public class UtilAlertDialog {
	public static void ShowDialog(Context context, int layoutid, boolean isbottom) {
		Builder builder = new Builder(context,
				R.style.custom_dialog);
		dialog = builder.create();
		//dialog.setView(new EditText(context));
		dialog.show();
		dialog.setContentView(layoutid);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		if (isbottom) {
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.x = 0;
           // 以下这两句是为了保证按钮可以水平满屏
			Resources resources = context.getResources();
			DisplayMetrics dm = resources.getDisplayMetrics();
			float density1 = dm.density;
			lp.width = dm.widthPixels;
         // 设置显示位置
			dialog.onWindowAttributesChanged(lp);

			dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		}
	
	}
	public static void ShowDialog(Context context, int layoutid) {
		Builder builder = new Builder(context,
				R.style.custom_dialog);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(layoutid);
		
	}
	/**
	 * }
	 * 
	 * 关闭等待对话框
	 */
	public static void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public static AlertDialog dialog;
}
