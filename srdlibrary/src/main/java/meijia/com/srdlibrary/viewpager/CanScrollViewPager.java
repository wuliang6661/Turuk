package meijia.com.srdlibrary.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by srd on 2017/9/7.
 * 不可以滑动的ViewPager
 */

public class CanScrollViewPager extends ViewPager {
    private boolean isEnable;
    public CanScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isEnable = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnable){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnable){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isEnable() {
        return isEnable;
    }
}
