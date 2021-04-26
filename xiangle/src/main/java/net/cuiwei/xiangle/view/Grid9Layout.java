package net.cuiwei.xiangle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.cuiwei.xiangle.R;

import static android.view.View.MeasureSpec.EXACTLY;

public class Grid9Layout extends ViewGroup {
    GridAdatper adapter;
    private int mMaxChildWidth = 0;
    private int mMaxChildHeight = 0;
    int column=3;//列数
    int margin=10;//边距
    int count = 0;
    boolean showChoose=false;//是否显示上传按钮

    public Grid9Layout(Context context) {
        super(context);
        //init();
    }

    public Grid9Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Grid9Layout);
            column = a.getInteger(R.styleable.Grid9Layout_numColumn, 3);
            margin = (int) a.getInteger(R.styleable.Grid9Layout_itemMargin, 10);
            showChoose = a.getBoolean(R.styleable.Grid9Layout_showChoose, false);
        }
    }

    public Grid9Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Grid9Layout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        mMaxChildWidth = 0;
        mMaxChildHeight = 0;

        int modeW = 0, modeH = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED)
            modeW = MeasureSpec.UNSPECIFIED;
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED)
            modeH = MeasureSpec.UNSPECIFIED;

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), modeW);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), modeH);

        count = getChildCount();
        if (count == 0) {
            super.onMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
            return;
        }
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
            mMaxChildHeight = Math.max(mMaxChildHeight,
                    child.getMeasuredHeight());
        }
        setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec),
                resolveSize(mMaxChildHeight, heightMeasureSpec));
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("onLayout", "changed="+changed+" l="+l+" t="+t+" r="+r+" b="+b);
        Log.e("onLayout", "count="+count+" column="+column);
        int width = r - l;// 布局区域宽度
        int height = b - t;// 布局区域高度
        //count=10, column=3;
        int rows= (int) Math.ceil((float)count/column);//行数, 向上取整(注：其中一个转为浮点数
        Log.e("onLayout", "float rows:"+rows);
        if (count == 0) return;
        //int column2=(int)column;
        int gridW = (width - margin * (column - 1)) / column;// 格子宽度
        //int gridH = (height - margin * rows) / rows;// 格子高度
        int gridH=gridW;
        Log.e("onLayout", "gridW="+gridW+" gridH="+gridH);
        //gridH=400;
        int left = 0;
        int top = 0;

        for (int i = 0; i < rows; i++) {// 遍历行
            for (int j = 0; j < column; j++) {// 遍历每一行的元素
                View child = this.getChildAt(i * column + j);//每个格子的下标（从0开始
                if (child == null) return;
                left = j * gridW + j * margin;//每个格子左边距离
                Log.e("onLayout", "getMeasuredWidth="+child.getMeasuredWidth()+" getMeasuredHeight="+child.getMeasuredHeight());
                // 如果当前布局宽度和测量宽度不一样，就直接用当前布局的宽度重新测量
                if (gridW != child.getMeasuredWidth()) {
                    int measuredWidth=MeasureSpec.makeMeasureSpec(gridW, EXACTLY);
                    child.measure(measuredWidth, measuredWidth);
                }
                //用于当前ViewGroup中的子控件的布局, 一个格子四个边 的位置
                child.layout(left, top, left + gridW, top + gridH);
                Log.e("onLayout(layout(l,t,r,b))", "left="+left+" top="+top+" right="+left+gridW+" bottom="+top+gridH);
            }
            top += gridH + margin;
        }
    }

    /**
     * 填充数据，测试用
     */
    private void init(){
        for (int i=0; i<10; i++){
            ImageView imageView=new ImageView(getContext());
            imageView.setBackgroundResource(R.mipmap.ic_launcher);
            this.addView(imageView);
        }
        //添加按钮
        ImageView imageView=new ImageView(getContext());
        imageView.setBackgroundResource(R.mipmap.jiahao);
        imageView.setId(R.id.choose);
        this.addView(imageView);
    }
    public interface GridAdatper {
        View getView(int index);

        int getCount();
    }
    /** 设置适配器 */
    public void setGridAdapter(GridAdatper adapter) {
        this.adapter = adapter;
        //移除掉子视图
        removeAllViews();
        // 动态添加视图
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            addView(adapter.getView(i));
        }
        Log.e("showChoose", showChoose+"");
        if (showChoose) {
            //添加按钮
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.add);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setId(R.id.choose);
            addView(imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(final OnItemClickListener click) {
        if (this.adapter == null)
            return;
        for (int i = 0; i < adapter.getCount(); i++) {
            final int index = i;
            View view = getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    click.onItemClick(v, index);
                }
            });
        }
    }
}
