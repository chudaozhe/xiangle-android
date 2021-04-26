package net.cuiwei.xiangle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 禁止Viewpager左右滑动功能
 * 重写ViewPager，覆盖ViewPager的onInterceptTouchEvent(MotionEvent arg0)方法和onTouchEvent(MotionEvent arg0)方法，返回值都改为false，那么ViewPager就不能左右滑动了
 */
public class NoScrollViewPager extends ViewPager {
    //是否禁止左右滑动
    private boolean noScroll = true;
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollViewPager(Context context) {
        super(context);
    }
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    //返回false表示不消费事件
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
    //返回false表示不拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
    @Override
    public void setCurrentItem(int item) {
        //false 表示没有切换动画
        super.setCurrentItem(item,false);
    }
}
