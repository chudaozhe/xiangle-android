package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.SettingActivity;
import net.cuiwei.xiangle.adapter.MenuAdapter;
import net.cuiwei.xiangle.adapter.MyPersonInfoAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Menus;
import net.cuiwei.xiangle.bean.User;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.MenuModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.UserModel;
import net.cuiwei.xiangle.view.Alert1Dialog;
import net.cuiwei.xiangle.view.RecyclerViewUnderline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyFragment extends Fragment implements OnListListener<Menus>, OnDetailListener<User>, View.OnClickListener {
    private long user_id=0;
    private FragmentManager mFragmentManager;
    RecyclerView recyclerview;
    ImageView bannerView;
    //作品数，点赞数等
    GridView gridview;
    //未登录：登录/注册
    TextView noLogin;
    //昵称
    TextView nickname;
    //编辑资料按钮
    TextView personInfo;
    ImageView avatar;
    public int[] counts={0, 0, 0, 0};
    public String[] names={"获赞", "关注", "粉丝", "作品"};
    public static MyFragment newInstance(FragmentManager fragmentManager) {
        MyFragment fragment = new MyFragment();
        fragment.mFragmentManager=fragmentManager;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);

        MenuModel model = new MenuModel();
        model.myList(getContext(), this);

        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        if (user_id>0){
            UserModel model1=new UserModel();
            model1.get(user_id, getContext(),this);

            noLogin.setVisibility(View.GONE);
            nickname.setVisibility(View.VISIBLE);
            personInfo.setVisibility(View.VISIBLE);
            personInfo.setOnClickListener(this);
        }else {
            //default data
            Glide.with(getContext()).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/avatar/default/0.jpg?x-oss-process=image/circle,r_100/format,png").into(avatar);

            ArrayList<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < names.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("count", counts[i]);
                map.put("name", names[i]);
                menuList.add(map);
            }
            setGridviewData(menuList);
            //点击跳登录
            avatar.setOnClickListener(this);
            noLogin.setOnClickListener(this);
        }
        Glide.with(getContext()).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/banner/my_banner.jpg").into(bannerView);

        return view;
    }
    public void initView(View view) {
        recyclerview=view.findViewById(R.id.recyclerview);
        bannerView=view.findViewById(R.id.bannerView);
        noLogin=view.findViewById(R.id.noLogin);
        nickname=view.findViewById(R.id.nickname);
        personInfo=view.findViewById(R.id.personInfo);
        avatar=view.findViewById(R.id.avatar);
        gridview = view.findViewById(R.id.gridView);
    }
    @Override
    public void onSuccess(BaseList2Response<Menus> data) {
        List<Object> items = new ArrayList<>();
        //数据获取之后  将数据循环遍历，放进items集合中，至于服务器返回什么格式的数据，我想看下实体类就应该明白了
        for (int i=0; i < data.list.size(); i++){
            items.add(data.list.get(i));
            for(int k = 0; k < data.list.get(i).getMenus().size(); k ++){
                items.add(data.list.get(i).getMenus().get(k));
            }
        }
        //实例化适配器将遍历好的数据放进适配器中
        MenuAdapter adapter = new MenuAdapter(getContext() ,items);
        //new一个布局管理器，这里是用GridLayoutManager，要区分3列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 1);//多少列，如果数据项只需要1列，这里写1，下面return 也返回1即可实现
        //下面这个方法很重要，根据position获取当前这条数据是标题还是数据项，来设置他的跨列
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //适配器中有这么一个方法，根据position获取当前这条数据是标题还是数据项，来设置他的跨列
                switch (adapter.getItemViewType(position)){
                    case MenuAdapter.SITES://标题的话跨多少列 这个值要跟整个列数相等 如果大于会出错，小于布局会乱
                        return 1;
                    case MenuAdapter.SITE://数据项
                        return 1;//不跨列，就是分成三列显示
                    default:
                        return -1;
                }
            }
        });
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(adapter);
        //分割线
        //recyclerview.addItemDecoration(new RecyclerViewUnderline(getActivity(), OrientationHelper.VERTICAL));


        //item的点击事件，这里实现，进行具体的操作
        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(View itemview, int position) {
                switch (adapter.getItemViewType(position)){
                    case MenuAdapter.SITE:
                        //Toast.makeText(getActivity(), ((Menus.Menu) items.get(position)).getTitle(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity() , SettingActivity.class);
                        String action="my_favorite";
                        if (position==2) action="my_comment";
                        if (position==3) action="my_like";
                        if (position==5) action="my_feedback";
                        if (position==6) action="my_recommend";
                        if (position==7) action="my_setting";
                        intent.setAction(action);

                        Integer[] positions={1, 2, 3};
                        if(Arrays.asList(positions).contains(position)){
                            if (user_id==0){
                                Alert1Dialog alert1Dialog=new Alert1Dialog();
                                alert1Dialog.show(mFragmentManager, "1");
                            }else {
                                startActivity(intent);
                            }
                        }else {
                            startActivity(intent);
                        }
                        break;
                    case MenuAdapter.SITES:
                        //Toast.makeText(getActivity(), ((Menus) items.get(position)).getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        //System.out.println(list);
    }

    /**
     * stat
     * @param field
     */
    @Override
    public void onSuccess(User field) {
        Glide.with(getContext()).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+field.getAvatar()+"?x-oss-process=image/circle,r_100/format,png").into(avatar);
        nickname.setText(field.getNickname());

        List<User.stat> stat=field.getStat();
//        Toast.makeText(getActivity(), "fied:"+field.getNickname(), Toast.LENGTH_LONG).show();
        ArrayList<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < stat.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("count", stat.get(i).getCount());
            map.put("name", stat.get(i).getName());
            menuList.add(map);
        }
        setGridviewData(menuList);
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();
    }

    public void setGridviewData(ArrayList<HashMap<String, Object>> menuList){
        SimpleAdapter saMenuItem = new SimpleAdapter(this.getActivity(),
                menuList, //数据源
                R.layout.my_grid_item, //xml实现
                new String[]{"count","name"}, //对应map的Key
                new int[]{R.id.count,R.id.name});  //对应R的Id
        //添加Item到网格中
        gridview.setAdapter(saMenuItem);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                System.out.println("click index:" + arg2);
                Integer[] indexs={1, 2};
                if(Arrays.asList(indexs).contains(arg2)) {
                    if (user_id == 0) {
                        Alert1Dialog alert1Dialog = new Alert1Dialog();
                        alert1Dialog.show(mFragmentManager, "1");
                    } else {
                        Intent intent=new Intent(getContext(), SettingActivity.class);
                        String action="";
                        if (arg2==1){//关注
                            action="my_follow";
                        }else if (arg2==2){//粉丝
                            action="my_fans";
                        }
                        intent.setAction(action);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(), SettingActivity.class);
        String action="";
        int item=v.getId();
        if (item==R.id.avatar || item==R.id.noLogin){
            action="my_login";
        }else if (item==R.id.personInfo){
            action="my_person_info";
        }
        intent.setAction(action);
        startActivity(intent);
    }
}