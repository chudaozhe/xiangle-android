package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.SettingActivity;
import net.cuiwei.xiangle.adapter.MyUserAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.UserFollow;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.FollowModel;
import net.cuiwei.xiangle.model.TokenModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFollowUserFragment extends Fragment implements OnListListener<UserFollow> {
    private long user_id=0;
    public ListView listView;
    public ArrayList<UserFollow> fields=new ArrayList<>();
    public boolean first=true;
    public int page=1;
    public int max=12;
    RefreshLayout refreshLayout;
    MyUserAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        View view= inflater.inflate(R.layout.fragment_my_follow_user, container, false);
        listView = view.findViewById(R.id.listView);
        adapter=new MyUserAdapter(fields, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserFollow field=fields.get(position);
                long to_user_id = field.getTo_user_id();
                if (to_user_id >0) {
                    Intent intent=new Intent(getContext(), SettingActivity.class);
                    intent.setAction("author");
                    intent.putExtra("author_id", to_user_id);
                    startActivity(intent);
                }
            }
        });
        //第一屏
        loadFirst();

        refreshLayout = view.findViewById(R.id.refreshLayout);
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
        return view;
    }

    @Override
    public void onSuccess(BaseList2Response<UserFollow> data) {
        //刷新，or加载？
        if (!first){
            //加载
            int count=data.count;
            if (fields.size()>=count || data.list.size()<max) {
                //全部数据加载完毕
                refreshLayout.finishLoadMoreWithNoMoreData();
            }else{
                refreshLayout.finishLoadMore();
            }
            fields.addAll(data.list);
        }else {
            //刷新
            fields.clear();
            fields.addAll(data.list);
            refreshLayout.finishRefresh();
        }
        //通知listView重新加载数据
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        Toast.makeText(this.getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();
    }
    public void loadFirst(){
        loadData(true);
    }
    public void loadMore(){
        loadData(false);
    }
    public void loadData(boolean first){
        this.first=first;
        HashMap<String, String> map=new HashMap<String, String>();
        if (!first) {
            ++page;
        }else page=1;
        map.put("max", String.valueOf(max));
        map.put("page", String.valueOf(page));
        map.put("type", String.valueOf(1));
        FollowModel model = new FollowModel();
        model.list(user_id, map, getContext(), this);
    }
}