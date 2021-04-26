package net.cuiwei.xiangle.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.model.TokenModel;

public class CustomBottomTabItem {
    private long user_id=0;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Context mContext;
    private FragmentManager mFragmentManager;
    BottomTabListener listener;
    /**
     * 把点击的序号传递给activity
     */
    public interface BottomTabListener{
        public void bottomTabNumber(int id);
    }

    //底部Tab标题
    private final String[] mTitles = {"首页", "发现", "发布", "消息", "我的"};
    //返回CustomBottomTabItem实例
    public static CustomBottomTabItem create() {
        return TabItemHolder.sCustomTabItem;
    }
    //创建CustomBottomTabItem实例
    private static class TabItemHolder {
        private static CustomBottomTabItem sCustomTabItem = new CustomBottomTabItem();
    }
    //引入布局需要的Context
    public CustomBottomTabItem setContext(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager=fragmentManager;
        listener=(BottomTabListener)mContext;
        TokenModel cache=new TokenModel(context);
        user_id=cache.getUserId();
        return this;
    }
    //需要自定义的TabLayout
    public CustomBottomTabItem setTabLayout(TabLayout tabLayout) {
        mTabLayout = tabLayout;
        return this;
    }
    //设置与TabLayout关联的ViewPager
    public CustomBottomTabItem setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        return this;
    }
    //创建Tab
    public CustomBottomTabItem build() {
        initTabLayout();
        return this;
    }
    //初始化Tab
    private void initTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        //第二个参数为selector，下同
        mTabLayout.getTabAt(0).setCustomView(getTabView(0, R.drawable.tab_home_selector));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1, R.drawable.tab_discover_selector));
        mTabLayout.getTabAt(2).setCustomView(getTabView(2, R.drawable.tab_publish_selector));
        mTabLayout.getTabAt(3).setCustomView(getTabView(3, R.drawable.tab_notify_selector));
        mTabLayout.getTabAt(4).setCustomView(getTabView(4, R.drawable.tab_my_selector));
//        mTabLayout.getTabAt(4).setCustomView(getTabView(4, R.drawable.money_icon_selector));
        tabSelectListener();
    }
    //自定义Tab样式
    private View getTabView(final int position, int resId) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_tab_item, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.title_text_view);
        final ImageView ivTitle = (ImageView) view.findViewById(R.id.title_image_view);
        ivTitle.setImageResource(resId);
        tvTitle.setText(mTitles[position]);
        //默认第一个tab选中，设置字体为选中色
        if (position == 0) {
            tvTitle.setTextColor(Color.parseColor("#ef8633"));
        } else {
            tvTitle.setTextColor(Color.parseColor("#262a3b"));
        }
        //点击Tab切换
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==2){
                    if (user_id==0){
                        Alert1Dialog alert1Dialog=new Alert1Dialog();
                        alert1Dialog.show(mFragmentManager, "1");
                    }else {
                        BottomDialog bottomDialogFr = new BottomDialog();
                        bottomDialogFr.show(mFragmentManager, "2");
                    }
                   // Toast.makeText(mContext, "good.", Toast.LENGTH_LONG).show();
                }else
                mViewPager.setCurrentItem(position);
            }
        });
        return view;
    }
    //Tab监听
    private void tabSelectListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    //切换Tab文字是否选中的的颜色
    private void changeTabStatus(TabLayout.Tab tab, boolean selected) {
        View view = tab.getCustomView();
        if (view == null) {
            return;
        }
        TextView tvTitle = (TextView) view.findViewById(R.id.title_text_view);
        if (selected) {
            listener.bottomTabNumber(tab.getPosition());
            Log.e("currentFragment", String.valueOf(tab.getPosition()));
            tvTitle.setTextColor(Color.parseColor("#ef8633"));
        } else {
            tvTitle.setTextColor(Color.parseColor("#262a3b"));
        }
    }
}