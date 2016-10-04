package com.example.administrator.customerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Administrator on 2016/10/4.
 */

public class CustomerViewGroup extends ViewGroup {

    private int mChildWidth;
    private int mChildHeight;
    private Context mContext;
    private ImageView mAddIconView;

    public CustomerViewGroup(Context context) {
        super(context, null);
    }

    public CustomerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomerViewGroup);
        mChildWidth = (int) typedArray.getDimension(R.styleable.CustomerViewGroup_childWidth, dp2px(50));
        mChildHeight = (int) typedArray.getDimension(R.styleable.CustomerViewGroup_childHeight, dp2px(50));
        typedArray.recycle();

        createAddIconView();
        super.addView(mAddIconView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pointX = l;
        int pointY = t;
        int parentWidth = getMeasuredWidth();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if ((pointX + childWidth) > parentWidth) {
                //换行
                pointY += childHeight;
                pointX = l;
                child.layout(pointX, pointY, pointX + childWidth, pointY + childHeight);
            } else {
                child.layout(pointX, pointY, pointX + childWidth, pointY + childHeight);
                pointX += childWidth;
            }
        }
    }

    @Override
    public void addView(View child) {
        int childCount = getChildCount();
        View lastView = getChildAt(childCount - 1);
        if (lastView == mAddIconView) {
            //如果最后一个子view是添加图标的view，则将其删除，待添加完子view后再去添加
           removeView(lastView);
            super.addView(child);
        }
        super.addView(mAddIconView);
    }

    /**
     * 创建一个添加的view
     *
     * @return
     */
    private void createAddIconView() {
        mAddIconView = new ImageView(mContext);
        mAddIconView.setImageResource(R.mipmap.ic_launcher);
        LayoutParams layoutParams = new LayoutParams(mChildWidth, mChildHeight);
        mAddIconView.setLayoutParams(layoutParams);
        mAddIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addView(createChildView());
                requestLayout();
            }
        });
    }

    /**
     * 生成子view
     *
     * @return
     */
    private View createChildView() {
        View view = new View(mContext);
        view.setBackgroundColor(createRandomColor());
        LayoutParams layoutParams = new LayoutParams(mChildWidth, mChildHeight);
        view.setLayoutParams(layoutParams);
        return view;
    }

    private int dp2px(int dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 生成随机的颜色
     *
     * @return
     */
    private int createRandomColor() {
        Random random = new Random();
        return Color.argb(random.nextInt(225), random.nextInt(225), random.nextInt(225), random.nextInt(225));
    }
}
