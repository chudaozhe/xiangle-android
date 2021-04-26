package net.cuiwei.xiangle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
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
import net.cuiwei.xiangle.DetailActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.adapter.SearchAdapter;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Joke;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.JokeModel;
import net.cuiwei.xiangle.model.TokenModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends Fragment implements OnListListener<Joke> {
    private long user_id=0;
    public ListView listView;
    private ArrayList<Joke> fields=new ArrayList<>();
    public boolean first=true;
    public int page=1;
    public int max=10;
    public String keyword;
    RefreshLayout refreshLayout;
    SearchAdapter adapter;
    OnListListener<Joke> t=this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().setTitle("搜索");
        EditText search=view.findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String key = v.getText().toString().trim();
                Log.e("actionId", actionId+"");
                //具体是"搜索"，还是"完成"，需在xml里设置
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(TextUtils.isEmpty(key)){
                        Log.e("keyword", "请输入");
                        return true;
                    }
                    first=true;
                    keyword=key;
                    loadData();
                    Log.e("keyword", key);
                    //return false;
                }
                return false;
            }
        });
        //光标定位
        search.requestFocus();
        //拉起软键盘
        openSystemKeyBoard(true);

        listView = view.findViewById(R.id.listView);
        adapter=new SearchAdapter(fields, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Joke field=fields.get(position);
                long joke_id = (long)field.getId();
                if (joke_id >0) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    Bundle data = new Bundle();
                    data.putSerializable("id", joke_id);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });
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
                first=true;
                Log.e("page", "Refresh");
                loadData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                first=false;
                Log.e("page", "LoadMore");
                loadData();
            }
        });
        return view;
    }
    /**
     * 软键盘控制
     * @param show
     */
    public void openSystemKeyBoard(Boolean show) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show){
            imm.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }else imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onSuccess(BaseList2Response<Joke> data) {
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
    public void loadData(){
        HashMap<String, String> map=new HashMap<String, String>();
        if (!first) {
            ++page;
        }else page=1;
        JokeModel model = new JokeModel();
        map.put("type", "0");
        map.put("keyword", keyword);
        map.put("page", String.valueOf(page));
        map.put("max", String.valueOf(max));
        model.list(user_id, 0, map, getContext(), t);
    }
}