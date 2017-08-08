package com.hzw.elasticdragdismiss;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by User on 2017/7/31.
 */

public class ElasticDragDismissView extends FrameLayout {

    private float dragDismissDistance=Float.MAX_VALUE;
    private float dragDismissScale=1;
    private float dragElacticity=0.8f;
    private float dragDismissFraction;
    private float scale;
    private float dragTo;
    private DragDismissListener dragDismissListener;
    private boolean downDrag=false;
    private boolean upDrag=false;
    private int totalDragDistance=0;

    public interface DragDismissListener{
        void dragDismiss();
    }

    public void registerListener(DragDismissListener listener){
        this.dragDismissListener=listener;
    }


    public ElasticDragDismissView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray attr=context.obtainStyledAttributes(attrs,R.styleable.ElasticDragDismissView);
        if(attr.hasValue(R.styleable.ElasticDragDismissView_dragDismissDistance)){
            dragDismissDistance=attr.getDimensionPixelSize(R.styleable.ElasticDragDismissView_dragDismissDistance,0);
        }
        if(attr.hasValue(R.styleable.ElasticDragDismissView_dragDismissScale)){
            dragDismissScale=attr.getFloat(R.styleable.ElasticDragDismissView_dragDismissScale,1);
        }
        System.out.println("dragDismissDistance:"+dragDismissDistance+"  "+"dragDismissScale:"+dragDismissScale);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
          if((downDrag&&dy>0)||(upDrag&&dy<0)){
              dragScale(dy);
              consumed[1]=dy;
          }
    }

    //当dyUnconsumed不为0时表示内部子元素滑到顶或者滑到底时手指还在滑动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dragScale(dyUnconsumed);
    }


    @Override
    public void onStopNestedScroll(View child) {
        if(Math.abs(totalDragDistance)>dragDismissDistance){
            if(dragDismissListener!=null){
                dragDismissListener.dragDismiss();
            }
        }
        //使用动画恢复成初始值，不会显得突兀
        animate()
                .setDuration(200)
                .translationY(0)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
        downDrag=upDrag=false;
        totalDragDistance=0;
    }

    //scroll>0表示往上拉，scroll<0表示往下拉
    public void dragScale(int scroll){
         if(scroll==0)
             return;
        totalDragDistance+=scroll;
        if(totalDragDistance>0&&!downDrag&&!upDrag){
            upDrag=true;
            setPivotY(getHeight());
        }else if(totalDragDistance<0&&!downDrag&&!upDrag){
            downDrag=true;
            setPivotY(0);
        }
        dragDismissFraction= (float) Math.log10(1+(Math.abs(totalDragDistance)/dragDismissDistance));
        dragTo=dragDismissDistance*dragDismissFraction*dragElacticity;
        if(upDrag){
            dragTo*=-1;
        }
        setTranslationY(dragTo);
        scale=1-(1-dragDismissScale)*dragDismissFraction;
        setScaleX(scale);
        setScaleY(scale);
        if((downDrag&&totalDragDistance>=0)||(upDrag&&totalDragDistance<=0)){
            downDrag=upDrag=false;
            totalDragDistance=0;
        }

    }
}
