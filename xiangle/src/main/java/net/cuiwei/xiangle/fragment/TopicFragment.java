package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import net.cuiwei.xiangle.DetailActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.adapter.JokeAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Joke;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.JokeModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;

public class TopicFragment extends Fragment implements OnListListener<Joke>, OnDetailListener<Topic> {
    private long user_id=0;
    private RecyclerView recyclerview;
    public boolean first=true;
    public ArrayList<Joke> fields;
    public int page=1;
    RefreshLayout refreshLayout;
    public long topic_id;
    public ImageView iconView;
    public TextView titleView;
    public TextView countView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.e("收到id:", mId);
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();

        View view=inflater.inflate(R.layout.fragment_topic, container, false);
        getActivity().setTitle("话题");

        iconView=view.findViewById(R.id.icon);
        titleView=view.findViewById(R.id.title);
        countView=view.findViewById(R.id.count);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerview=view.findViewById(R.id.recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //接收传递来的值
        topic_id=getActivity().getIntent().getLongExtra("id", 0);
        TopicModel topicModel=new TopicModel();
        topicModel.get(user_id, topic_id, getContext(), this);
//        if (topic!=null){
//            topic_id=topic.getId();
//            Glide.with(this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+topic.getImage()).into(icon);
//            title.setText(topic.getName());
//            count.setText("段子数："+topic.getSum());
//        }else Toast.makeText(getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();

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
    public void setView(ArrayList<Joke> list){
        JokeAdapter jokeAdapter=new JokeAdapter(getContext(), getFragmentManager(), list, true);
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
    public void onSuccess(Topic field) {
        Glide.with(this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+field.getImage()).into(iconView);
        titleView.setText(field.getName());
        countView.setText("段子数："+field.getSum());
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
        JokeModel jokeModel = new JokeModel();
        HashMap<String, String> map = new HashMap<String, String>();
        if (!first) {
            ++page;
        } else page = 1;
        map.put("max", "10");
        map.put("page", String.valueOf(page));
        jokeModel.list(user_id, topic_id, map, getContext(), this);
    }
}