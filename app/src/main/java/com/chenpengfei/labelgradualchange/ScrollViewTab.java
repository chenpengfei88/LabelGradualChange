package com.chenpengfei.labelgradualchange;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
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
    private int left;
    private ViewPager viewPager;
    int move;
    int offset;
    boolean leftMove;
    private int movePosition = 0;
    View view;
    int currnet;

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
        for(int i = 0; i<array.length; i++){
            View itemView = lf.inflate(R.layout.title_item, null);
            if(i == 0){
                itemView.findViewById(R.id.fid).setVisibility(View.VISIBLE);
            }
            itemView.setLayoutParams(new ViewGroup.LayoutParams(tabWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((TextView)itemView.findViewById(R.id.cid)).setText(array[i]);
            ((TextView)itemView.findViewById(R.id.fid)).setText(array[i]);
            if(itemView.getParent()!=null)
                ((ViewGroup)itemView.getParent()).removeAllViews();
            itemView.setOnClickListener(new TabOnClickListener(i));
            tabsContainer.addView(itemView);
        }
        addView(tabsContainer);
    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                currnet = i;
                if(movePosition != i){
                    offset = 0;
                    movePosition = i;
                 }
                if(v>0.9)
                    v=1.0f;
                    if (offset == 0) {
                        offset = i2;
                    } else {
                        if (i2 > offset) {
                            leftMove = false;
                        } else {
                            leftMove = true;
                        }
                        offset = i2;
                    }

                left = i * tabWidth + (int)(tabWidth*v);
                view = tabsContainer.getChildAt(i);
                //移动的view
                TextView ftextView = ((TextView)view.findViewById(R.id.fid));
                TextView ftextViewTid = ((TextView)view.findViewById(R.id.cid));
                RelativeLayout.LayoutParams relativeLayoutOne = (RelativeLayout.LayoutParams)ftextView.getLayoutParams();
                RelativeLayout.LayoutParams relativeLayoutTTidOne = (RelativeLayout.LayoutParams)ftextViewTid.getLayoutParams();
                relativeLayoutOne.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                relativeLayoutTTidOne.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                relativeLayoutOne.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                relativeLayoutTTidOne.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                int left = (int) (dip2px(getContext(), 36)* v);
                ftextView.setPadding(-left, dip2px(getContext(), 10),0,0);

                View viewTwo = tabsContainer.getChildAt(i + 1);
                if(viewTwo == null)
                    return;
                TextView ftextViewTwo = ((TextView)viewTwo.findViewById(R.id.fid));
                TextView ftextViewTwoTid = ((TextView)viewTwo.findViewById(R.id.cid));
                if(ftextViewTwo.getVisibility() == View.GONE){
                    ftextViewTwo.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams relativeLayout = (RelativeLayout.LayoutParams)ftextViewTwo.getLayoutParams();
                    RelativeLayout.LayoutParams relativeLayoutTTid = (RelativeLayout.LayoutParams)ftextViewTwoTid.getLayoutParams();
                    relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    relativeLayoutTTid.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    relativeLayout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    relativeLayoutTTid.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ftextViewTwo.setPadding(0, dip2px(getContext(), 10),-dip2px(getContext(), 36),0);
                }else {
                    RelativeLayout.LayoutParams relativeLayout = (RelativeLayout.LayoutParams)ftextViewTwo.getLayoutParams();
                    RelativeLayout.LayoutParams relativeLayoutTTid = (RelativeLayout.LayoutParams)ftextViewTwoTid.getLayoutParams();
                    relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    relativeLayoutTTid.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    relativeLayout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    relativeLayoutTTid.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    int leftTwo = (int) (dip2px(getContext(), 36) * v);
                    ftextViewTwo.setPadding(0, dip2px(getContext(), 10),-dip2px(getContext(), 36) +leftTwo ,0);
                }
            }

            @Override
            public void onPageSelected(int i) {
                System.out.println("=============onPageSelected====="+i + "==" + leftMove);
                if(leftMove)
                    i =i+1;
//                if(count!=0 && i>=count-1){
//                    if(!leftMove)
//                        move+=plus;
//                    else
//                        move-=plus;
//                    scroll(tabWidth);
//                }
                if(count!=0 && i>=count-1){
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
            if(position > currnet){
                leftMove = false;
            } else {
                leftMove = true;
            }
            currnet = position;
            TextView ftextView = ((TextView)view.findViewById(R.id.fid));
            TextView ftextViewTid = ((TextView)view.findViewById(R.id.cid));
            RelativeLayout.LayoutParams relativeLayoutOne = (RelativeLayout.LayoutParams)ftextView.getLayoutParams();
            RelativeLayout.LayoutParams relativeLayoutTTidOne = (RelativeLayout.LayoutParams)ftextViewTid.getLayoutParams();
            relativeLayoutOne.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayoutTTidOne.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayoutOne.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLayoutTTidOne.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int left = (int) (dip2px(getContext(), 36));
            ftextView.setPadding(-left, dip2px(getContext(), 10),0,0);
            viewPager.setCurrentItem(position, false);
            View viewTwo = tabsContainer.getChildAt(position);
            TextView ftextViewTwo = ((TextView)viewTwo.findViewById(R.id.fid));
            TextView ftextViewTwoTid = ((TextView)viewTwo.findViewById(R.id.cid));
            ftextViewTwo.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams relativeLayout = (RelativeLayout.LayoutParams)ftextViewTwo.getLayoutParams();
            RelativeLayout.LayoutParams relativeLayoutTTid = (RelativeLayout.LayoutParams)ftextViewTwoTid.getLayoutParams();
            relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLayoutTTid.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLayout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayoutTTid.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ftextViewTwo.setPadding(0, dip2px(getContext(), 10),0,0);
        }
    }
}
