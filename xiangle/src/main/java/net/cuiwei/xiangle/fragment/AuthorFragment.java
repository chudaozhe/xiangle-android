package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import net.cuiwei.xiangle.BaseActivity;
import net.cuiwei.xiangle.DetailActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.adapter.JokeAdapter;
import net.cuiwei.xiangle.bean.Author;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Joke;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.AuthorModel;
import net.cuiwei.xiangle.model.FollowModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.view.Alert1Dialog;
import net.cuiwei.xiangle.view.dialog.BaseDialog;
import net.cuiwei.xiangle.view.dialog.CommonDialog;
import net.cuiwei.xiangle.view.dialog.ViewConvertListener;
import net.cuiwei.xiangle.view.dialog.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthorFragment extends Fragment implements OnDetailListener<Author>, OnListListener<Joke>, View.OnClickListener {
    private static final String ID = "author_id";
    private long authorId;
    private long user_id=0;
    private RecyclerView recyclerview;
    public boolean first=true;
    public Author field;//编辑详情
    public ArrayList<Joke> fields;
    public int page=1;
    RefreshLayout refreshLayout;
    GridView gridview;//底部弹窗
    public Button follow;

    public ImageView avatar;
    public TextView nickname;
    public TextView quotes;
    public TextView followers;//粉丝
    public TextView following;//关注数

    public static AuthorFragment newInstance(long param1) {
        AuthorFragment fragment = new AuthorFragment();
        Bundle args = new Bundle();
        args.putLong(ID, param1);
        fragment.setArguments(args);
        return fragment;
    }
       @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           if (getArguments() != null) {
               authorId = getArguments().getLong(ID);
           }
       }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        Toolbar toolbar=((BaseActivity) getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle("作者主页");
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        View view=inflater.inflate(R.layout.fragment_author, container, false);
        AuthorModel authorModel=new AuthorModel();
        authorModel.get(user_id, authorId, getContext(), this);

        avatar=view.findViewById(R.id.avatar);
        nickname=view.findViewById(R.id.nickname);
        quotes=view.findViewById(R.id.quotes);

        follow=view.findViewById(R.id.follow);
        follow.setOnClickListener(this);//关注

        followers=view.findViewById(R.id.followers);
        following=view.findViewById(R.id.following);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerview=view.findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //第一屏
        loadFirst();

        MaterialHeader sr= new MaterialHeader(getContext());
        sr.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light);
        refreshLayout.setRefreshHeader(sr);
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("page", "Refresh");
                loadFirst();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Log.e("page", "LoadMore");
                loadMore();
            }
        });
        //refreshLayout.autoRefresh();//自动刷新
        return view;
    }
    /**
     * 作者详情
     * @param field
     */
    @Override
    public void onSuccess(Author field){
        this.field=field;
        String quotesStr=field.getQuotes();
        if (TextUtils.isEmpty(quotesStr)){
            quotes.setVisibility(View.GONE);
        }else quotes.setText(field.getQuotes());
        nickname.setText(field.getNickname());
        followers.setText(getString(R.string.followers, field.getFollowers()));//粉丝数
        following.setText(getString(R.string.following, field.getFollowing()));//关注数
        //关注按钮
        if (field.getIs_follow()==1){
            follow.setText("已关注");
            follow.setTextColor(0x67000000);//灰色
            follow.setBackgroundResource(R.drawable.btn_follow_normal);
        }else{
            follow.setTextColor(0xffff8100);//橙色
            follow.setBackgroundResource(R.drawable.btn_follow_highlighted);
        }
        //头像
        Glide.with(getContext()).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+field.getAvatar()+"?x-oss-process=image/circle,r_100/format,png").into(avatar);
    }
    public void setView(ArrayList<Joke> list){
        JokeAdapter jokeAdapter=new JokeAdapter(getContext(), getFragmentManager(), list);
        jokeAdapter.setOnItemClickListener(new JokeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Joke joke, int position) {
                //Toast.makeText(getContext(), "id:"+joke.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("id", joke.getId());
                intent.putExtras(data);
                startActivity(intent);
            }
        });
        recyclerview.setAdapter(jokeAdapter);
    }
    @Override
    public void onSuccess(BaseList2Response<Joke> data) {
        //刷新，or加载？
        if (!first){
            //加载
            int count=data.count;
            if (fields.size()>=count) {
                //全部数据加载完毕
                refreshLayout.finishLoadMoreWithNoMoreData();
            }else{
                refreshLayout.finishLoadMore();
            }
            fields.addAll(data.list);
        }else {
            //刷新
            fields=data.list;
            refreshLayout.finishRefresh();
            setView(fields);
        }
        setView(fields);
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();
    }
    public void loadFirst(){
        loadData(true);
    }
    public void loadMore(){
        loadData(false);
    }
    public void loadData(boolean first) {
        this.first=first;
        //段子列表
        AuthorModel authorModel = new AuthorModel();
        HashMap<String, String> map = new HashMap<String, String>();
        if (!first) {
            ++page;
        } else page = 1;
        map.put("max", "10");
        map.put("page", String.valueOf(page));
        authorModel.list(user_id, authorId, map, getContext(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.follow:
                if (user_id > 0) {
                    FollowModel followModel=new FollowModel();
                    if (field.is_follow==1){
                        followModel.doFollow(user_id, field.getId(), false, getContext());
                        follow.setText("关注");
                        follow.setTextColor(0xffff8100);//橙色
                        follow.setBackgroundResource(R.drawable.btn_follow_highlighted);
                    }else{
                        followModel.doFollow(user_id, field.getId(), true, getContext());
                        follow.setText("已关注");
                        follow.setTextColor(0x67000000);//灰色
                        follow.setBackgroundResource(R.drawable.btn_follow_normal);
                    }
                    follow.setEnabled(false);
                }else Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_sample, menu);
        menu.add(1, Menu.FIRST, Menu.FIRST, "点点").setIcon(R.mipmap.diandian).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//always
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==1){
            //Toast.makeText(getContext(), "good..", Toast.LENGTH_SHORT).show();
            CommonDialog.newInstance()
                    .setLayoutId(R.layout.dialog_author)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseDialog dialog) {
                            holder.getView(R.id.close).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            gridview=holder.getView(R.id.gridView);
                            setGridviewData(gridViewData());
                        }
                    })
                    .setShowBottom(true)
                    .setSize(0, 160)
                    .show(getFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }
    public ArrayList<HashMap<String, Object>> gridViewData(){
        ArrayList<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "拉黑");
        map1.put("icon", R.mipmap.lahei);
        menuList.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "举报");
        map2.put("icon", R.mipmap.jubao);
        menuList.add(map2);
        return menuList;
    }
    public void setGridviewData(ArrayList<HashMap<String, Object>> menuList){
        SimpleAdapter saMenuItem = new SimpleAdapter(this.getActivity(),
                menuList, //数据源
                R.layout.dialog_author_item, //xml实现
                new String[]{"icon","name"}, //对应map的Key
                new int[]{R.id.icon,R.id.name});  //对应R的Id
        //添加Item到网格中
        gridview.setAdapter(saMenuItem);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                System.out.println("click index:" + arg2);
                if (arg2 == 0) {
                    if (user_id == 0) {
                        Alert1Dialog alert1Dialog = new Alert1Dialog();
                        alert1Dialog.show(getFragmentManager(), "1");
                    } else {
                        AuthorModel authorModel = new AuthorModel();
                        if (user_id != authorId) {
                            authorModel.shield(user_id, authorId, getContext());
                        } else Toast.makeText(getContext(), "不能拉黑自己！", Toast.LENGTH_SHORT).show();

                    }
                } else if (arg2 == 1) {
                    Toast.makeText(getContext(), "感谢您的举报，我们会很快处理～", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}