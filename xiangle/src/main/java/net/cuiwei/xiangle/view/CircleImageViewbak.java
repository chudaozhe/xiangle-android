package net.cuiwei.xiangle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import net.cuiwei.xiangle.R;

/**
 * 圆形头像
 */
public class CircleImageViewbak extends View {
    private Paint mPaint;
    private Bitmap imageBitmap;
    private float circleRadio;
    public CircleImageViewbak(Context context) {
        super(context);
        mPaint=new Paint();
    }

    public CircleImageViewbak(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageViewbak, 0, 0);
        int n=a.getIndexCount();
        int imageResourceId=0;
        for (int i=0; i<n; i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.CircleImageViewbak_imageSrc:
                    imageResourceId=a.getResourceId(attr, R.drawable.ic_launcher_background);
                break;
                case R.styleable.CircleImageViewbak_inSampieSize:
                    a.getInteger(attr, 1);
                    break;
            }
        }
        a.recycle();
        imageBitmap= BitmapFactory.decodeResource(getResources(), imageResourceId);//R.mipmap.head200

    }

//    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    /**
     * 组件大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 组件位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 渲染出各组件
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(getCircle(imageBitmap), 0, 0, mPaint);
    }
    public Bitmap getCircle(Bitmap bitmap){
        //第一步
        //新建bitmap, 跟传入图片一样宽对正方形bitmap
        Bitmap b=Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        //第二步
        //初始化画布，并将刚才创建对bitmap给这个画布，让画布画在这个bitmap上面
        Canvas canvas=new Canvas(b);
        //第三步
        //初始化画笔
        Paint p=new Paint();
        //圆形图片半径
        float circleRadius=bitmap.getWidth()/2;
        //在画布中画一个等宽的圆
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, p);
        //设置画笔属性，让画笔只在圈圈中画画，注意：画笔属性
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, p);
        return b;
    }

}
