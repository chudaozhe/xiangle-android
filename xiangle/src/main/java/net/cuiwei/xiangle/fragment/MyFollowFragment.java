package net.cuiwei.xiangle.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import net.cuiwei.xiangle.R;

import java.util.ArrayList;

public class MyFollowFragment extends Fragment {
    //fragment集合
    ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
    //标题集合
    ArrayList<String> titleList=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_follow, container, false);
        getActivity().setTitle("我的关注");
        //找控件
        TabLayout tabs = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);

        //添加title
        titleList.add("用户");
        titleList.add("话题");

        //实例化fragment并添加的数组
        fragmentList.add(new MyFollowUserFragment());
        fragmentList.add(new MyFollowTopicFragment());

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