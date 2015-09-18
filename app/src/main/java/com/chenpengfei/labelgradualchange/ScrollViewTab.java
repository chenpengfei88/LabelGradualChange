package com.chenpengfei.labelgradualchange;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Chenpengfei on 2015/6/10.
 */
public class ScrollViewTab extends HorizontalScrollView {

    private LinearLayout tabsContainer;
    //单个tab的宽度
    private int tabWidth = 220;
    private int count;
    private Paint mPaint;
    private ViewPager viewPager;
    private int offset;
    boolean leftMove;
    private int movePosition = 0;
    private View view;
    private int currentPosition;

    public ScrollViewTab(Context context) {
        super(context);
    }

    public ScrollViewTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initData(Context context, String[] array, int screenWidth, LayoutInflater lf){
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        //linearlayout容器，tab添加到tabsContainer中
        tabsContainer = new LinearLayout(context);
        tabsContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setBackgroundColor(Color.RED);
        //如果所有tab的宽度小于屏幕的宽度就平分宽度
        if(screenWidth > tabWidth*array.length){
            tabWidth = screenWidth/array.length;
        } else {
            count = screenWidth / tabWidth;
        }
        //tab
        for(int i = 0; i < array.length; i++){
            View itemView = lf.inflate(R.layout.title_item, null);
            if(i == 0){
                itemView.findViewById(R.id.cover_tab_text).setVisibility(View.VISIBLE);
            }
            //设置整个item的宽度
            itemView.setLayoutParams(new ViewGroup.LayoutParams(tabWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            //设置tab textview 内容
            TextView tabText = (TextView) itemView.findViewById(R.id.tab_text);
            tabText.setText(array[i]);
            ((TextView)itemView.findViewById(R.id.cover_tab_text)).setText(array[i]);
            if(itemView.getParent()!=null)
                ((ViewGroup)itemView.getParent()).removeAllViews();
            //设置内容RelativeLayoutitem的宽度
            RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout_id);
            LinearLayout.LayoutParams rf = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
            rf.width = measureViewWidthOrHeight(relativeLayout);
            tabText.setTag(rf.width);
            itemView.setOnClickListener(new TabOnClickListener(i));
            tabsContainer.addView(itemView);
        }
        addView(tabsContainer);
    }

    public int measureViewWidthOrHeight(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return  width;
    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                currentPosition = i;
                if(movePosition != i){
                    offset = 0;
                    movePosition = i;
                 }
                if(v > 0.9)
                    v = 1.0f;
                if(offset!=0){
                    if (i2 > offset) {
                        leftMove = false;
                    } else {
                        leftMove = true;
                    }
                }
                offset = i2;
                view = tabsContainer.getChildAt(i);
                //移动的view
                TextView coverTabText = ((TextView)view.findViewById(R.id.cover_tab_text));
                TextView tabText = ((TextView)view.findViewById(R.id.tab_text));
                addOrRemoveRule(coverTabText, tabText, true, RelativeLayout.ALIGN_PARENT_RIGHT);
                addOrRemoveRule(coverTabText, tabText, false, RelativeLayout.ALIGN_PARENT_LEFT);
                int left = (int) ((int)tabText.getTag() * v);
                coverTabText.setPadding(- left, dip2px(getContext(), 10),0,0);

                View viewTwo = tabsContainer.getChildAt(i + 1);
                if(viewTwo == null)
                    return;
                TextView coverTwoTabText = ((TextView) viewTwo.findViewById(R.id.cover_tab_text));
                TextView tabTwoText = ((TextView) viewTwo.findViewById(R.id.tab_text));
                int leftTwo = 0;
                if(coverTwoTabText.getVisibility() == View.GONE){
                    coverTwoTabText.setVisibility(View.VISIBLE);
                }else {
                    leftTwo = (int) ((int)tabTwoText.getTag() * v);
                }
                addOrRemoveRule(coverTwoTabText, tabTwoText, false, RelativeLayout.ALIGN_PARENT_RIGHT);
                addOrRemoveRule(coverTwoTabText, tabTwoText, true, RelativeLayout.ALIGN_PARENT_LEFT);
                coverTwoTabText.setPadding(0, dip2px(getContext(), 10), - (int)tabTwoText.getTag() + leftTwo ,0);
            }

            @Override
            public void onPageSelected(int i) {
                if(leftMove)
                    i = i + 1;
                if(count!=0 && i >= count-1){
                    if(!leftMove)
                        scroll(tabWidth);
                    else
                        scroll(-tabWidth);
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void scroll(int tabWidth){
        this.smoothScrollTo(getScrollX() + tabWidth,0);
    }

    private void addOrRemoveRule(TextView textView, TextView coverTextView, boolean isAdd, int rule){
        RelativeLayout.LayoutParams textlf = (RelativeLayout.LayoutParams)textView.getLayoutParams();
        RelativeLayout.LayoutParams coverTextlf = (RelativeLayout.LayoutParams)coverTextView.getLayoutParams();
        if(isAdd){
            textlf.addRule(rule);
            coverTextlf.addRule(rule);
        } else {
            textlf.removeRule(rule);
            coverTextlf.removeRule(rule);
        }
    }

    public int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }


    private class TabOnClickListener implements OnClickListener {

        private int position;

        public TabOnClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(position > currentPosition){
                leftMove = false;
            } else {
                leftMove = true;
            }
            currentPosition = position;
            TextView converTabText = ((TextView)view.findViewById(R.id.cover_tab_text));
            TextView tabText = ((TextView)view.findViewById(R.id.tab_text));
            addOrRemoveRule(converTabText, tabText, true, RelativeLayout.ALIGN_PARENT_RIGHT);
            addOrRemoveRule(converTabText, tabText, false, RelativeLayout.ALIGN_PARENT_LEFT);
            int left = (int)tabText.getTag();
            converTabText.setPadding(-left, dip2px(getContext(), 10),0,0);
            viewPager.setCurrentItem(position, false);

            View viewTwo = tabsContainer.getChildAt(position);
            TextView coverTwoTabText = ((TextView)viewTwo.findViewById(R.id.cover_tab_text));
            TextView tabTwoText = ((TextView)viewTwo.findViewById(R.id.tab_text));
            coverTwoTabText.setVisibility(View.VISIBLE);
            addOrRemoveRule(coverTwoTabText, tabTwoText, false, RelativeLayout.ALIGN_PARENT_RIGHT);
            addOrRemoveRule(coverTwoTabText, tabTwoText, true, RelativeLayout.ALIGN_PARENT_LEFT);
            coverTwoTabText.setPadding(0, dip2px(getContext(), 10),0,0);
        }
    }
}
