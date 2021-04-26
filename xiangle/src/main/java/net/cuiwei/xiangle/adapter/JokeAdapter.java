package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.DetailActivity;
import net.cuiwei.xiangle.ImagePreviewActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.SettingActivity;
import net.cuiwei.xiangle.bean.Joke;
import net.cuiwei.xiangle.model.FavoriteModel;
import net.cuiwei.xiangle.model.LikeModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.utility.DisplayUtil;
import net.cuiwei.xiangle.view.Alert1Dialog;
import net.cuiwei.xiangle.view.FullScreenDialog;
import net.cuiwei.xiangle.view.Grid9Layout;

import java.util.ArrayList;
import java.util.HashMap;

/*
① 创建一个继承RecyclerView.Adapter<VH>的Adapter类
② 创建一个继承RecyclerView.ViewHolder的静态内部类
③ 在Adapter中实现3个方法：
   onCreateViewHolder()
   onBindViewHolder()
   getItemCount()
*/
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Joke> list;
    private View inflater;
    private String[] imgs={};//{"flash/1.jpg"}
    private FragmentManager mFragmentManager;
    private boolean isTopicFragment;
    //当前登录的用户id
    private long user_id;
    //私有属性
    private OnItemClickListener onItemClickListener = null;   //构造方法，传入数据
    public JokeAdapter(Context context, FragmentManager fragmentManager, ArrayList<Joke> list){
        isTopicFragment=false;
        //同下
        this.context = context;
        this.mFragmentManager=fragmentManager;
        this.list = list;
        TokenModel cache=new TokenModel(context);
        user_id=cache.getUserId();
        //同下
    }
    public JokeAdapter(Context context, FragmentManager fragmentManager, ArrayList<Joke> list, boolean isTopic){
        isTopicFragment=isTopic;
        //同上
        this.context = context;
        this.mFragmentManager=fragmentManager;
        this.list = list;
        TokenModel cache=new TokenModel(context);
        user_id=cache.getUserId();
        //同上
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.joke_item_1, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Long joke_id=list.get(position).getId();
        String content=list.get(position).getContent();
        String image=list.get(position).getImage();
        String images=list.get(position).getImages();
        String video=list.get(position).getVideo();
        int type=list.get(position).getType();
        String avatar=list.get(position).getAvatar();
        String nickname=list.get(position).getNickname();
        String topicName=list.get(position).getTopic_name();
        long topicId=list.get(position).getTopic_id();
        int isLike=list.get(position).getIs_like();
        int isFavorite=list.get(position).getIs_favorite();

        //将数据和控件绑定
        //头像
        Glide.with(context).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+avatar+"?x-oss-process=image/circle,r_100/format,png").into(holder.avatarView);
        holder.nicknameView.setText(nickname);
        holder.textView.setText(content);
        //grid9Layout 传值
        holder.grid9Layout.setVisibility(View.VISIBLE);
        if (type==1){
            holder.grid9Layout.setVisibility(View.GONE);
        }else if(type==2){
            if (!imgs.equals("")) imgs=images.split(",");
        }else if (type==3) {
            //视频
            String[] temp=new String[1];
            temp[0]=image;
            imgs=temp;
        }
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) holder.grid9Layout.getLayoutParams();
        params.width=RelativeLayout.LayoutParams.MATCH_PARENT;

        int rows= (int) Math.ceil((float)imgs.length/3);//行数, 向上取整(注：其中一个转为浮点数
        int gridH = (DisplayUtil.getScreenWidth(context)-DisplayUtil.dip2px(context,32) - 10 * (3 - 1)) / 3;// 格子宽度, 32:左右两边各16dp
        params.height=rows*gridH+(rows-1)*10;//单位px,非dp
        holder.grid9Layout.setLayoutParams(params);//动态改变grid9Layout的高度
        holder.grid9Layout.setGridAdapter(new Grid9Layout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = LayoutInflater.from(context).inflate(R.layout.publish_grid_item, null);
                ImageView imageView = view.findViewById(R.id.iv);
                imageView.setScaleType(ImageView.ScaleType.CENTER );
                ImageView play = view.findViewById(R.id.play);
                if (type!=3) play.setVisibility(View.GONE);
                //imageView.setBackgroundResource(R.mipmap.ic_launcher);//srcs[index]
                Glide.with(context).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+imgs[index]+"?x-oss-process=image/resize,w_300,h_300,m_fill").into(imageView);
                return view;
            }
            @Override
            public int getCount() {
                return imgs.length;
            }
        });
        holder.grid9Layout.setOnItemClickListener(new Grid9Layout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                System.out.println("item="+index);
                if (type==3){
                    FullScreenDialog fullScreenDialog= FullScreenDialog.newInstance("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+video);
                    fullScreenDialog.show(mFragmentManager, "video");
                }else if (type==2){
                    //图片预览
                    String images=list.get(position).getImages();//重新取一下
                    if (!imgs.equals("")) imgs=images.split(",");
                    if (imgs.length>0) {
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putExtra("index", index);
                        intent.putExtra("images", imgs);
                        context.startActivity(intent);
                    }
                }
                //Toast.makeText(getContext(), "item="+index, 0).show();
            }
        });
        //话题名称
        if (!TextUtils.isEmpty(topicName)){
            holder.topicName.setText("#"+topicName+"#");
            holder.topicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , SettingActivity.class);
                    String action="discover_topic";
                    intent.setAction(action);
                    intent.putExtra("id", topicId);
                    //intent.putExtra("topic", field.setData(field));//传递对象
                    context.startActivity(intent);
                }
            });
            if (isTopicFragment) holder.topicName.setVisibility(View.GONE);
        }else holder.topicName.setVisibility(View.GONE);
        //cell底部
        String[] gridTitles={"点赞", "评论", "收藏"};
        int zanIcon=R.mipmap.zan;
        int shoucangIcon=R.mipmap.shoucang;
        if (isLike==1) zanIcon=R.mipmap.zan2;
        if (isFavorite==1) shoucangIcon=R.mipmap.shoucang2;
        Integer[] gridIcons={zanIcon, R.mipmap.pinglun, shoucangIcon};
        ArrayList<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < gridTitles.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", gridTitles[i]);
            map.put("icon", gridIcons[i]);
            menuList.add(map);
        }
        JokeCellGridAdapter jokeCellGridAdapter = new JokeCellGridAdapter(menuList, context);
        //添加Item到网格中
        holder.gridView.setAdapter(jokeCellGridAdapter);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                System.out.println("click index:" + arg2);
                switch (arg2){
                    case 0:
                        if (user_id>0){
                            LikeModel likeModel=new LikeModel();
                            if (list.get(position).getIs_like()==0){
                                menuList.set(0, buildMap("点赞", R.mipmap.zan2));
                                likeModel.doLike(user_id, joke_id, true, context);
                                //同步修改列表数据
                                list.get(position).setIs_like(1);
                            }else {
                                menuList.set(0, buildMap("点赞", R.mipmap.zan));
                                likeModel.doLike(user_id, joke_id, false, context);
                                //同步修改列表数据
                                list.get(position).setIs_like(0);
                            }
                        }else {
                            //提示登录
                            Alert1Dialog alert1Dialog=new Alert1Dialog();
                            alert1Dialog.show(mFragmentManager, "1");
                        }
                        break;
                    case 1:
                        //menuList.set(1, buildMap("评论", R.mipmap.pinglun2));
                        //跳详情
                        Intent intent = new Intent(context, DetailActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("id", joke_id);
                        intent.putExtras(data);
                        context.startActivity(intent);
                        break;
                    case 2:
                        menuList.set(2, buildMap("收藏", R.mipmap.shoucang2));
                        if (user_id>0){
                            FavoriteModel favoriteModel=new FavoriteModel();
                            if (list.get(position).getIs_favorite()==0){
                                menuList.set(2, buildMap("收藏", R.mipmap.shoucang2));
                                favoriteModel.doFavorite(user_id, joke_id, true, context);
                                //同步修改列表数据
                                list.get(position).setIs_favorite(1);
                            }else {
                                menuList.set(2, buildMap("收藏", R.mipmap.shoucang));
                                favoriteModel.doFavorite(user_id, joke_id, false, context);
                                //同步修改列表数据
                                list.get(position).setIs_favorite(0);
                            }
                        }else {
                            //提示登录
                            Alert1Dialog alert1Dialog=new Alert1Dialog();
                            alert1Dialog.show(mFragmentManager, "1");
                        }
                        break;
                }
                jokeCellGridAdapter.notifyDataSetChanged();
            }
        });
//        holder.itemView.setTag(list.get(position).getId());//点击时获取id
        //实现点击效果
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, list.get(position), position);
                }
            }
        });

    }

    public HashMap<String, Object> buildMap(String title, Integer icon){
        HashMap<String, Object> hashMap=new HashMap<String, Object>();
        hashMap.put("title", title);
        hashMap.put("icon", icon);
        return hashMap;
    }
    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarView;
        TextView nicknameView;
        TextView textView;
        Grid9Layout grid9Layout;
        Button topicName;
        GridView gridView;
        public MyViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatar);
            nicknameView = itemView.findViewById(R.id.nickname);
            textView = itemView.findViewById(R.id.content);
            grid9Layout = itemView.findViewById(R.id.images);
            topicName = itemView.findViewById(R.id.topicName);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }
    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, Joke joke, int position);
    }
}
