package net.cuiwei.xiangle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.core.view.MenuItemCompat;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import net.cuiwei.xiangle.adapter.TopicAdapter2;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.TopicModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseTopicActivity extends BaseActivity implements OnListListener<Topic> {
    private long user_id=0;
    private SearchView mSearchView;
    private AutoCompleteTextView mAutoCompleteTextView;//搜索输入框
    private ImageView mDeleteButton;//搜索框中的删除按钮

    public ListView listView;
    private ArrayList<Topic> fields=new ArrayList<>();
    public boolean first=true;
    public int page=1;
    public int max=20;
    RefreshLayout refreshLayout;
    TopicAdapter2 adapter;
    public String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TokenModel cache=new TokenModel(this);
        user_id=cache.getUserId();

//        mSearchView=findViewById(R.id.search);
//        mAutoCompleteTextView=mSearchView.findViewById(R.id.search_src_text);
//        mDeleteButton=mSearchView.findViewById(R.id.search_close_btn);
//        mSearchView.setIconifiedByDefault(false);//设置搜索图标是否显示在搜索框内
        //1:回车, 2:前往, 3:搜索, 4:发送, 5:下一项, 6:完成
        //mSearchView.setImeOptions(3);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
        //mSearchView.setInputType(1);//设置输入类型
        //mSearchView.setMaxWidth(200);//设置最大宽度
        //mSearchView.setQueryHint("ahdewoi");//设置查询提示字符串
        //mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        //setUnderLinetransparent(mSearchView);
        //SearchView设置监听
        //setListener();
        listView = findViewById(R.id.listView);
        adapter=new TopicAdapter2(fields, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic field=fields.get(position);
                long topic_id = field.getId();
                if (topic_id >0) {
                    Intent intent = new Intent();
                    intent.putExtra("topic", field);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        //第一屏
        loadFirst();

        refreshLayout = findViewById(R.id.refreshLayout);
        MaterialHeader sr= new MaterialHeader(this);
        sr.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light);
        refreshLayout.setRefreshHeader(sr);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
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
        Toast.makeText(this, "出错来哦！", Toast.LENGTH_SHORT).show();
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
        if (!TextUtils.isEmpty(keyword)) map.put("keyword", keyword);
        TopicModel model = new TopicModel();
        model.list(user_id, 0, map, this, this);
    }
    private void setListener(){
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.e("comin","=====query="+query);
                keyword=query;
                loadFirst();
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("comin","=====newText="+newText);
                return false;
            }
        });
    }
    @Override
    protected boolean hasBackIcon() {
        return true;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_choose_topic);
    }

    @Override
    protected void initializeData(Bundle saveInstance) {
        setTitle("选择话题");
    }
    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    /**设置SearchView下划线透明**/
    private void setUnderLinetransparent(SearchView searchView){
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //设置最大宽度
        //mSearchView.setMaxWidth();
        //设置是否显示搜索框展开时的提交按钮
        //mSearchView.setImeOptions(3);
        //mSearchView.setSubmitButtonEnabled(true);
        //设置输入框提示语
        mSearchView.setQueryHint("请输入关键词");
        //SearchView设置监听
        setListener();

        return super.onCreateOptionsMenu(menu);
    }

}