package com.haoyigou.hyg.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.haoyigou.hyg.view.widget.ScrollViewListener;

/**
 * Created by zhusiliang on 15/12/8.
 */
public class ObservableScrollView  extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private OnScrollToBottomListener onScrollToBottom; 

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    
    @Override  
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,  
            boolean clampedY) {  
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);  
        if(scrollY != 0 && null != onScrollToBottom){  
            onScrollToBottom.onScrollBottomListener(clampedY);  
        }  
    }  
      
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){  
        onScrollToBottom = listener;  
    }  
  
    public interface OnScrollToBottomListener{  
        void onScrollBottomListener(boolean isBottom);
    }  

}