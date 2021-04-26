package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;

public class ViewpagerAdapter extends PagerAdapter {
    private String[] images;
    private Context context;
    public ViewpagerAdapter(String[] images, Context context) {
        super();
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        final int index=position%images.length;
        Glide.with(context).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+images[index]+"?x-oss-process=image/resize,w_800").into(imageView);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);//背景不拉伸
        //imageView.setLayoutParams(new ViewGroup.LayoutParams(500,100));

        container.addView(imageView,0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("viewpage-position", String.valueOf(index));
            }
        });
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}