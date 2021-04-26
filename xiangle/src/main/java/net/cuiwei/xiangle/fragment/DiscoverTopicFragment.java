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
import net.cuiwei.xiangle.adapter.TopicAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.FollowModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 发现页，右侧话题列表
 */
public class DiscoverTopicFragment extends Fragment implements OnListListener<Topic>, TopicAdapter.OnItemButtonClickListener {
    private long user_id=0;
    private static final String ARG_CATEGORY_ID = "category_id";
    Long category_id;
    public ListView listView;
    private ArrayList<Topic> fields=new ArrayList<>();
    public boolean first=true;
    public int page=1;
    public int max=10;
    RefreshLayout refreshLayout;
    TopicAdapter adapter;
    
    public static DiscoverTopicFragment newInstance(Long category_id) {
        DiscoverTopicFragment fragment = new DiscoverTopicFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, category_id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_id = getArguments().getLong(ARG_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        View view= inflater.inflate(R.layout.fragment_discover_topic, container, false);

        listView = view.findViewById(R.id.listView);
        adapter=new TopicAdapter(fields, getContext());
        adapter.setButtonClickListener(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic field=fields.get(position);
                long topic_id = field.getId();
                if (topic_id >0) {
                    Intent intent = new Intent(getActivity() , SettingActivity.class);
                    String action="discover_topic";
                    intent.setAction(action);
                    intent.putExtra("id", topic_id);
                    //intent.putExtra("topic", field.setData(field));//传递对象
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
    public void onSuccess(BaseList2Response<Topic> data) {
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
        TopicModel model = new TopicModel();
        model.list(user_id, category_id, map, getContext(), this);
    }

    @Override
    public void followButtonClick(int position) {
        int topic_id=fields.get(position).getId();
        int is_follow=fields.get(position).getIs_follow();
        FollowModel followModel=new FollowModel();
        if (is_follow==1){
            followModel.doFollow2(user_id, topic_id, false, getContext());
        }else{
            followModel.doFollow2(user_id, topic_id, true, getContext());
        }
    }
}