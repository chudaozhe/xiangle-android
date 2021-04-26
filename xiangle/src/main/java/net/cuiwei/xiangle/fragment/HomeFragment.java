package net.cuiwei.xiangle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import net.cuiwei.xiangle.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    //fragment集合
    ArrayList<Fragment> fragmentList;
    //标题集合
    ArrayList<String> titleList;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        fragmentList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
        //找控件
        TabLayout tabs = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);

        //添加title
        titleList.add("推荐");
        titleList.add("视频");
        titleList.add("图片");
        titleList.add("文字");

        //实例化fragment并添加的数组
        fragmentList.add(HomePageFragment.newInstance(0));
        fragmentList.add(HomePageFragment.newInstance(3));
        fragmentList.add(HomePageFragment.newInstance(2));
        fragmentList.add(HomePageFragment.newInstance(1));

        //设置适配器
        MPagerAdapter mPagerAdapter = new MPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        //关联
        tabs.setupWithViewPager(viewPager);
        return view;
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
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}