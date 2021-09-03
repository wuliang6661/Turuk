package meijia.com.srdlibrary.commondialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import meijia.com.srdlibrary.R;
import meijia.com.srdlibrary.bottomView.UIUtils;


/**
 * [弹出dialog]
 *
 **/
public class CommonDialog extends Dialog {

    private View.OnClickListener confirmListener;
    private View.OnClickListener cancelListener;
    private View.OnClickListener middleListener;
    private View layoutId;
    private int margin;
    private int gravity;
    private Context mContext;

    /**
     * @param context
     */
    public CommonDialog(Context context,View layoutId,int margin,int gravity) {
        super(context, R.style.dialogFullscreen);
        this.layoutId = layoutId;
        this.margin = margin;
        this.gravity = gravity;
        this.mContext = context;
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(gravity);
        window.setAttributes(layoutParams);
        if (margin>0){
            window.setLayout(UIUtils.getMobileWidth(mContext)-margin*2, LayoutParams.WRAP_CONTENT);
        }else {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        //18901199332
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return true;
    }
}
