package net.cuiwei.xiangle.fragment;

import android.os.Bundle;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.adapter.NotifyAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Notify;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.NotifyModel;
import net.cuiwei.xiangle.model.TokenModel;

import java.util.ArrayList;
import java.util.HashMap;


public class NotifyFragment extends Fragment implements OnListListener<Notify> {
    private long user_id=0;
    private RecyclerView recyclerview=null;
    public boolean first=true;
    public ArrayList<Notify> fields;
    public int page=1;
    RefreshLayout refreshLayout;

    public static NotifyFragment newInstance() {
        NotifyFragment fragment = new NotifyFragment();
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        View view= inflater.inflate(R.layout.fragment_notify, container, false);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
        //refreshLayout.autoRefresh();//自动刷新
        return view;
    }
    public void setView(ArrayList<Notify> list){
        NotifyAdapter adapter=new NotifyAdapter(getContext(), list);
        adapter.setOnItemClickListener(new NotifyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Notify notify, int position) {
                //Toast.makeText(getContext(), "id:"+notify.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerview.setAdapter(adapter);
    }
    @Override
    public void onSuccess(BaseList2Response<Notify> data) {
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
    public void loadData(boolean first){
        this.first=first;
        HashMap<String, String> map=new HashMap<String, String>();
        if (!first) {
            ++page;
        }else page=1;
        map.put("max", "10");
        map.put("page", String.valueOf(page));
        NotifyModel model=new NotifyModel();
        model.list(user_id, map, getContext(), this);
    }
}