package net.cuiwei.xiangle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.cuiwei.xiangle.fragment.DiscoverFragment;
import net.cuiwei.xiangle.fragment.HomeFragment;
import net.cuiwei.xiangle.fragment.MyFragment;
import net.cuiwei.xiangle.fragment.NotifyFragment;
import net.cuiwei.xiangle.view.CustomBottomTabItem;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements CustomBottomTabItem.BottomTabListener {
    //fragment集合
    ArrayList<Fragment> fragmentList;
    //标题集合
//    String[] titleList= {"首页", "发现", "消息", "我的"};
    int tabPosition=0;//底部tab当前位置
    Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeData(savedInstanceState);
        //申请存储权限
        requestWritePermission();

        fragmentList = new ArrayList<Fragment>();
        //找控件
        TabLayout tabs = findViewById(R.id.tabs1);
        ViewPager viewPager = findViewById(R.id.viewpager1);

        //实例化fragment并添加的数组
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(DiscoverFragment.newInstance());
        fragmentList.add(new Fragment());//占位
        fragmentList.add(NotifyFragment.newInstance());
        fragmentList.add(MyFragment.newInstance(getSupportFragmentManager()));

        //设置适配器
        MPagerAdapter mPagerAdapter = new MPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        //关联
        tabs.setupWithViewPager(viewPager);
        //底部tab
        CustomBottomTabItem item = CustomBottomTabItem.create();
        item.setContext(this, getSupportFragmentManager())
                .setViewPager(viewPager)
                .setTabLayout(tabs)
                .build();
    }
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }
    protected void initializeData(Bundle saveInstance) {
        setTitle("首页");
    }
    @Override
    protected boolean isCenter() {
        return true;
    }
    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    /**
     * 禁用实体返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
    //适配器
    private class MPagerAdapter extends FragmentPagerAdapter {
        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList[position];
//        }
    }
    private void requestWritePermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * 收到点击序号后设置标题
     * @param id
     */
    @Override
    public void bottomTabNumber(int id) {
        switch (id){
            case 0:
                setTitle("首页");
                tabPosition=0;
                mMenu.setGroupVisible(0, true);//显示
                break;
            case 1:
                setTitle("发现");
                tabPosition=1;
                mMenu.setGroupVisible(0, true);//显示
                break;
            case 3:
                setTitle("消息");
                tabPosition=3;
                mMenu.setGroupVisible(0, false);
                break;
            case 4:
                setTitle("我的");
                tabPosition=4;
                mMenu.setGroupVisible(0, false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==1){
            Intent intent=new Intent(this, SettingActivity.class);
            intent.setAction("search");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, Menu.FIRST, "搜索").setIcon(R.mipmap.search2).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//always
        mMenu = menu;
        //mMenu.setGroupEnabled(0, false);//默认禁用
        return super.onCreateOptionsMenu(menu);
    }
}
