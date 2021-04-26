package net.cuiwei.xiangle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import net.cuiwei.xiangle.adapter.CommentAdapter;
import net.cuiwei.xiangle.bean.*;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.model.*;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.utility.DisplayUtil;
import net.cuiwei.xiangle.view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DetailActivity extends BaseActivity implements OnDetailListener<Joke>, OnListListener<Comment>, View.OnClickListener {
    private long joke_id=0;
    private long user_id=0;
    public TextView content;
    public TextView nickname;
    public ImageView avatar;
    public Button like;
    public Button favorite;
    public Button follow;
    public Joke field;//段子详情
    public Grid9Layout grid9Layout;
    private String[] imgs={};//{"flash/1.jpg"}
    TextView comment;
    public ListViewForScrollView listView;
    private ArrayList<Comment> fields=new ArrayList<>();
    public boolean first=true;
    public int page=1;
    public int max=10;
    RefreshLayout refreshLayout;
    CommentAdapter adapter;
    InputTextMsgDialog commentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
        initializeData(savedInstanceState);

        initView();
        //接收activity传参
        Intent intent = getIntent();
        this.joke_id = (long) intent.getSerializableExtra("id");
        TokenModel cache=new TokenModel(this);
        user_id=cache.getUserId();
        int screenWidth=DisplayUtil.getScreenWidth(this);
        Log.e("screen-width", String.valueOf(screenWidth));
        comment.setWidth(screenWidth-340);

        JokeModel model = new JokeModel();
        model.get(user_id, joke_id, this, this);

        adapter=new CommentAdapter(fields, DetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Comment field=fields.get(position);
//                long joke_id = (long)field.getJoke_id();
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
        //refreshLayout.autoRefresh();//自动刷新
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_detail);
    }
    @Override
    protected void initializeData(Bundle saveInstance) {
        setTitle("详情");
    }
    @Override
    protected boolean isCenter() {
        return false;
    }
    @Override
    protected boolean hasBackIcon() {
        return true;
    }

    public void initView() {
        follow = findViewById(R.id.follow);
        like = findViewById(R.id.like);
        favorite = findViewById(R.id.favorite);
        listView = findViewById(R.id.comment_list);
        nickname = findViewById(R.id.nickname);
        content = findViewById(R.id.content);
        avatar=findViewById(R.id.avatar);
        grid9Layout = findViewById(R.id.images);
        comment=findViewById(R.id.comment);

        avatar.setOnClickListener(this);
        follow.setOnClickListener(this);
        like.setOnClickListener(this);
        favorite.setOnClickListener(this);
        comment.setOnClickListener(this);
    }

    /**
     * 段子详情
     * @param joke
     */
    public void setView(Joke joke) {
        this.field=joke;
        content.setText(joke.getContent());
        nickname.setText(joke.getNickname());
        String image=joke.getImage();
        String images=joke.getImages();
        String video=joke.getVideo();
        int type=joke.getType();
        if (joke.getIs_like()==1) like.setBackgroundResource(R.mipmap.zan2);
        if (joke.getIs_favorite()==1) favorite.setBackgroundResource(R.mipmap.shoucang2);
        if (joke.getIs_follow()==1){
            follow.setText("已关注");
            follow.setTextColor(0x67000000);//灰色
            follow.setBackgroundResource(R.drawable.btn_follow_normal);
        }else{
            follow.setTextColor(0xffff8100);//橙色
            follow.setBackgroundResource(R.drawable.btn_follow_highlighted);
        }

        //头像
        Glide.with(DetailActivity.this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+joke.getAvatar()+"?x-oss-process=image/circle,r_100/format,png").into(avatar);

        //grid9Layout 传值
        grid9Layout.setVisibility(View.VISIBLE);
        if (type==1){
            grid9Layout.setVisibility(View.GONE);
        }else if(type==2){
            if (!imgs.equals("")) imgs=images.split(",");
        }else if (type==3) {
            //视频
            String[] temp=new String[1];
            temp[0]=image;
            imgs=temp;
        }
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) grid9Layout.getLayoutParams();
        params.width=RelativeLayout.LayoutParams.MATCH_PARENT;

        int rows= (int) Math.ceil((float)imgs.length/3);//行数, 向上取整(注：其中一个转为浮点数
        int gridH = (DisplayUtil.getScreenWidth(this)-DisplayUtil.dip2px(this, 20) - 10 * (3 - 1)) / 3;// 格子宽度, 20:左右两边各10dp
        params.height=rows*gridH+(rows-1)*10;//单位px,非dp
        grid9Layout.setLayoutParams(params);//动态改变grid9Layout的高度
        grid9Layout.setGridAdapter(new Grid9Layout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.publish_grid_item, null);
                ImageView imageView = view.findViewById(R.id.iv);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                ImageView play = view.findViewById(R.id.play);
                if (type!=3) play.setVisibility(View.GONE);
                //imageView.setBackgroundResource(R.mipmap.ic_launcher);//srcs[index]
                Glide.with(DetailActivity.this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+imgs[index]+"?x-oss-process=image/resize,w_300,h_300,m_fill").into(imageView);
                return view;
            }
            @Override
            public int getCount() {
                return imgs.length;
            }
        });
        grid9Layout.setOnItemClickListener(new Grid9Layout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                System.out.println("item="+index);
                if (type==3){
                    FullScreenDialog fullScreenDialog= FullScreenDialog.newInstance("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+video);
                    fullScreenDialog.show(getSupportFragmentManager(), "video");
                }else if (type==2){
                    //图片预览
                    if (imgs.length>0) {
                        Intent intent = new Intent(DetailActivity.this, ImagePreviewActivity.class);
                        intent.putExtra("index", index);
                        intent.putExtra("images", imgs);
                        startActivity(intent);
                    }
                }
                //Toast.makeText(getContext(), "item="+index, 0).show();
            }
        });
    }

    /**
     * 段子详情
     * @param joke
     */
    @Override
    public void onSuccess(Joke joke) {
        setView(joke);
    }
    /**
     * 评论列表
     * @param data
     */
    @Override
    public void onSuccess(BaseList2Response<Comment> data) {
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
    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }
    @Override
    public void onClick(View v) {
        Integer[] ids={R.id.like, R.id.favorite, R.id.comment, R.id.follow};
        if(Arrays.asList(ids).contains(v.getId()) && user_id==0){
            Alert1Dialog alert1Dialog=new Alert1Dialog();
            alert1Dialog.show(getSupportFragmentManager(), "1");
        }else {
            switch (v.getId()) {
                case R.id.like:
                    Log.e("click", "click like");
                    LikeModel likeModel=new LikeModel();
                    if (field.is_like==1){
                        likeModel.doLike(user_id, joke_id, false, this);
                        like.setBackgroundResource(R.mipmap.zan);
                    }else{
                        likeModel.doLike(user_id, joke_id, true, this);
                        like.setBackgroundResource(R.mipmap.zan2);
                    }
                    like.setEnabled(false);
                    break;
                case R.id.favorite:
                    Log.e("click", "click favorite");
                    FavoriteModel favoriteModel=new FavoriteModel();
                    if (field.is_favorite==1){
                        favoriteModel.doFavorite(user_id, joke_id, false, this);
                        favorite.setBackgroundResource(R.mipmap.shoucang);
                    }else{
                        favoriteModel.doFavorite(user_id, joke_id, true, this);
                        favorite.setBackgroundResource(R.mipmap.shoucang2);
                    }
                    favorite.setEnabled(false);
                    break;
                case R.id.comment:
                    Context context=this;
                    commentDialog = new InputTextMsgDialog(this, R.style.dialog_center);
                    //commentDialog.show();
                    commentDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                        @Override
                        public void onTextSend(String content) {
                            //点击发送按钮后，回调此方法，msg为输入的值
                            Log.d("content", content);
                            //comment.setText(msg);
                            HashMap<String, String> map=new HashMap<String, String>();
                            map.put("content", content);
                            CommentModel commentModel=new CommentModel();
                            commentModel.create(user_id, joke_id, map, context);
                            //refreshLayout.autoRefresh();//刷新当前页面
                            TokenModel cache=new TokenModel(context);
                            Comment newComment=Comment.create(cache.getAvatar(), content, cache.getNickname());
                            fields.add(0, newComment);
                            //通知listView重新加载数据
                            adapter.notifyDataSetChanged();
                        }
                    });
                    commentDialog.show();
                    break;
                case R.id.follow:
                    FollowModel followModel=new FollowModel();
                    if (field.is_follow==1){
                        followModel.doFollow(user_id, field.getUser_id(), false, this);
                        follow.setText("关注");
                        follow.setTextColor(0xffff8100);//橙色
                        follow.setBackgroundResource(R.drawable.btn_follow_highlighted);
                    }else{
                        followModel.doFollow(user_id, field.getUser_id(), true, this);
                        follow.setText("已关注");
                        follow.setTextColor(0x67000000);//灰色
                        follow.setBackgroundResource(R.drawable.btn_follow_normal);
                    }
                    follow.setEnabled(false);
                    break;
                case R.id.avatar:
                    Intent intent=new Intent(DetailActivity.this, SettingActivity.class);
                    intent.setAction("author");
                    intent.putExtra("author_id", field.user_id);
                    startActivity(intent);
                    break;
            }
        }
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
        CommentModel model = new CommentModel();
        model.list(joke_id, map, this, this);
    }
}