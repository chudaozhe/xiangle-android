package net.cuiwei.xiangle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import net.cuiwei.xiangle.adapter.ViewpagerAdapter;
import net.cuiwei.xiangle.view.NoScrollViewPager;


public class ImagePreviewActivity extends BaseActivity {
    private int index=0;
    private String[] images;
    private NoScrollViewPager viewpager;
    //指示器
    private LinearLayout indicators;

    @Override
    protected BaseActivity.TransitionMode getOverridePendingTransitionMode() {
        return BaseActivity.TransitionMode.SCALE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("图集");
        viewpager = findViewById(R.id.viewPager);
        indicators = findViewById(R.id.indicators);

        index=getIntent().getIntExtra("index", 0);
        images=getIntent().getStringArrayExtra("images");
        setIndicators();
        setViewpager();
        //1张图片时 隐藏指示器，并禁止滑动
        if (images.length<2) {
            indicators.setVisibility(View.GONE);
        }else {
            viewpager.setNoScroll(false);
        }
    }
    /**
     * 设置viewpager
     */
    public void setViewpager(){
        //设置适配器
        viewpager.setAdapter(new ViewpagerAdapter(images,this));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setImageBackground(position % images.length);
            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //默认第n张
        viewpager.setCurrentItem(index);
    }
    /**
     * 设置指示器
     */
    public void setIndicators(){
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(50,50);
            layout.setMargins(10, 0, 10, 0);
            imageView.setLayoutParams(layout);//图片宽高
            //imageView.setPadding(100, 0, 10, 0);
            if(i==0){
                imageView.setBackgroundResource(R.mipmap.white);
            }else {
                imageView.setBackgroundResource(R.mipmap.black);
            }
            indicators.addView(imageView);
        }
    }
    /**
     * 设置当前指示器
     */
    private void setImageBackground(int selectItems) {
        //Log.e("viewpage-count", indicators.getChildCount()+"");
        for (int i = 0; i < indicators.getChildCount(); i++) {
            ImageView imageView=(ImageView) indicators.getChildAt(i);
            //Log.e("viewpage-view", indicators.getChildAt(i).toString());
            if (i == selectItems) {
                imageView.setBackgroundResource(R.mipmap.white);
            } else {
                imageView.setBackgroundResource(R.mipmap.black);
            }
        }
    }
    @Override
    protected boolean hasBackIcon() {
        return true;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_image_preview);
    }

    @Override
    protected void initializeData(Bundle saveInstance) {

    }
}