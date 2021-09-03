package meijia.com.srdlibrary.liushibuju;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import meijia.com.srdlibrary.R;


public class TagLayout extends ViewGroup {

    private List<List<View>> mChildViews = new ArrayList<>();

    private BaseAdapter mAdapter;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 2.1 onMeasure() 指定宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 清空集合
        mChildViews.clear();

        int childCount = getChildCount();

        // 获取到宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);

        // 高度需要计算
        int height = getPaddingTop() + getPaddingBottom();

        // 一行的宽度
        int lineWidth = getPaddingLeft();

        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        // 子View高度不一致的情况下
        int maxHeight = 0;

        for (int i = 0; i < childCount; i++) {

            // 2.1.1 for循环测量子View
            View childView = getChildAt(i);

            if(childView.getVisibility() == GONE){
                continue;
            }

            // 这段话执行之后就可以获取子View的宽高，因为会调用子View的onMeasure
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // margin值 ViewGroup.LayoutParams 没有 就用系统的MarginLayoutParams
            // 想想 LinearLayout为什么有？
            // LinearLayout有自己的 LayoutParams  会复写一个非常重要的方法
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            // 什么时候需要换行，一行不够的情况下 考虑 margin
            if (lineWidth + (childView.getMeasuredWidth() + params.rightMargin + params.leftMargin) > width) {
                // 换行,累加高度  加上一行条目中最大的高度
                height += maxHeight;
                lineWidth = childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            } else {
                lineWidth += childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                maxHeight = Math.max(childView.getMeasuredHeight() + params.bottomMargin + params.topMargin, maxHeight);
            }

            childViews.add(childView);
        }

        height += maxHeight;

        Log.e("TAG", "width -> " + width + " height-> " + height);
        // 2.1.2 根据子View计算和指定自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 摆放子View
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left, top = getPaddingTop(), right, bottom;

        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            int maxHeight = 0;
            for (View childView : childViews) {

                if(childView.getVisibility() == GONE){
                    continue;
                }

                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                left += params.leftMargin;
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = childTop + childView.getMeasuredHeight();
                Log.e("TAG", childView.toString());

                Log.e("TAG", "left -> " + left + " top-> " + childTop + " right -> " + right + " bottom-> " + bottom);

                // 摆放
                childView.layout(left, childTop, right, bottom);
                // left 叠加
                left += childView.getMeasuredWidth() + params.rightMargin;

                // 不断的叠加top值
                int childHeight = childView.getMeasuredHeight()+ params.topMargin+params.bottomMargin;
                maxHeight = Math.max(maxHeight,childHeight);
            }

            top += maxHeight;
        }
    }

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter){
        if(adapter == null){
            // 抛空指针异常
        }

        // 清空所有子View
        removeAllViews();

        mAdapter = adapter;

        // 获取数量
        int childCount = mAdapter.getCount();
        for (int i=0;i<childCount;i++){
            // 通过位置获取View
            View childView = mAdapter.getView(i,this);
            addView(childView);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onChildClick(v, finalI);
                    }
                }
            });
        }
    }
    private String mSelecteColor = "#408c56";
    private String mNormalColor = "#777777";
    private int mSelecteId = R.drawable.item_tag_bg;
    private int mNormalId = R.drawable.item_tag_bg_normal;
    public void setStatus(String selecteColor,String normalColor,int selecteId,int normalId){
        mSelecteColor = selecteColor;
        mNormalColor = normalColor;
        mSelecteId = selecteId;
        mNormalId = normalId;
    }

    public void setItemSelecte(int positon){
        for (int i = 0; i < getChildCount(); i++) {
            if (i == positon){
//                getChildAt(i).setBackgroundDrawable(getResources().getDrawable(mSelecteId));
//                ((TextView) getChildAt(i)).setTextColor(Color.parseColor(mSelecteColor));
                getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.item_tag_bg));
                ((TextView) getChildAt(i)).setTextColor(Color.parseColor("#408c56"));
            }else {
//                ((TextView) getChildAt(i)).setTextColor(Color.parseColor(mNormalColor));
//                getChildAt(i).setBackgroundDrawable(getResources().getDrawable(mNormalId));
                ((TextView) getChildAt(i)).setTextColor(Color.parseColor("#777777"));
                getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.item_tag_bg_normal));
            }
        }
    }

    private OnChildViewClickListener mListener;
    public interface OnChildViewClickListener{
        void onChildClick(View view,int postion);
    }
    public void setOnChildViewClickListener(OnChildViewClickListener listener){
        this.mListener = listener;
    }
}
