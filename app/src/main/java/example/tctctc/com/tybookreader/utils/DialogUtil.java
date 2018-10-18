package example.tctctc.com.tybookreader.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import example.tctctc.com.tybookreader.BookApplication;
import example.tctctc.com.tybookreader.R;

/**
 * Function:
 * Created by tanchao on 2018/10/13.
 */

public class DialogUtil {


    private static Dialog makeBaseDialog(Context context){
        Dialog dialog = new Dialog(context, R.style.base_dialog_style);
        return dialog;
    }

    public interface RemindInfoOneBtnCallBack{
        void click();
    }

    public static Dialog makeRemindInfoOneBtnDialog(Context context, String title, String content, String btnText, final RemindInfoOneBtnCallBack back){
        final Dialog dialog = makeBaseDialog(context);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) UiUtils.dpToPx(context,240);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window .setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        View view = View.inflate(context,R.layout.dialog_remind_info_one_btn,null);
        TextView titleTv = view.findViewById(R.id.title);
        TextView contentTv = view.findViewById(R.id.content);
        TextView confirmTv = view.findViewById(R.id.confirm);
        titleTv.setText(title);
        contentTv.setText(content);
        confirmTv.setText(btnText);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                back.click();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog makeRemindInfoOneBtnDialog(Context context, int title, int content, int btnText, final RemindInfoOneBtnCallBack back){
        return makeRemindInfoOneBtnDialog(context,getString(title),getString(content),getString(btnText),back);
    }

    private static String getString(int strId){
        Context context = BookApplication.getContext();
        return context.getString(strId);
    }
}
