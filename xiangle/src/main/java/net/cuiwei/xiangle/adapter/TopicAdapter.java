package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.model.TokenModel;

import java.util.ArrayList;

public class TopicAdapter extends BaseAdapter{
    public static ArrayList<Topic> Datas;
    public Context mContext;
    //禁止点击列表
    public static ArrayList<Integer> clicks;
    public static OnItemButtonClickListener mButtonClickListener;

    public TopicAdapter(ArrayList<Topic> datas, Context mContext) {
        Datas = datas;
        clicks=new ArrayList<>();
        this.mContext = mContext;
    }
    /**
     * 自定义接口，用于回调关注按钮点击事件到Fragment/Activity
     */
    public interface OnItemButtonClickListener {
        public void followButtonClick(int position);
    }

    /**
     * 提供公共的方法,初始化接口
     * @param buttonClickListener
     */
    public void setButtonClickListener(OnItemButtonClickListener buttonClickListener){
        mButtonClickListener=buttonClickListener;
    }

    @Override
    public int getCount() {
        return Datas.size();
    }

    @Override
    public Object getItem(int position) {
        return Datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("clicks", "clicks: "+clicks);
        ViewHolder holder;
        //value
        String image=Datas.get(position).getImage();
        String content=Datas.get(position).getName();
        int is_follow=Datas.get(position).getIs_follow();
        int sum=Datas.get(position).getSum();
        //convertView为null的情况仅第一屏（大概）,后面都是公用第一屏的；这就导致：明明只改变了第一屏第一个的按钮，但第二屏第一个也跟着变了
        if (convertView==null){
            holder= new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.discover_topic_item, parent, false);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.textView = convertView.findViewById(R.id.content);
            holder.sumView = convertView.findViewById(R.id.sum);
            holder.followView = convertView.findViewById(R.id.follow);
            convertView.setTag(holder);
        }else holder=(ViewHolder) convertView.getTag();

        //关注按钮
        holder.followView.setText("关注");
        if (is_follow==1){
            holder.followView.setText("已关注");
            holder.followView.setTextColor(0x67000000);//灰色
            holder.followView.setBackgroundResource(R.drawable.btn_follow_normal);
        }else{
            holder.followView.setTextColor(0xffff8100);//橙色
            holder.followView.setBackgroundResource(R.drawable.btn_follow_highlighted);
        }
        holder.followView.setTag(position);
        holder.followView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) ((Button) v).getTag();
                int id = v.getId();
                if(id == R.id.follow){
                    TokenModel cache=new TokenModel(mContext);
                    long user_id=cache.getUserId();
                    if (user_id>0){
                        mButtonClickListener.followButtonClick(position);
                        clicks.add(position);

                        if (Datas.get(position).getIs_follow()==1){
                            Datas.get(position).setIs_follow(0);
                            ((Button) v).setText("关注");
                            ((Button) v).setTextColor(0xffff8100);//橙色
                            ((Button) v).setBackgroundResource(R.drawable.btn_follow_highlighted);
                        }else {
                            Datas.get(position).setIs_follow(1);
                            ((Button) v).setText("已关注");
                            ((Button) v).setTextColor(0x67000000);//灰色
                            ((Button) v).setBackgroundResource(R.drawable.btn_follow_normal);
                        }
                        v.setEnabled(false);//避免重复点击
                    }else {
                        Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //渲染每个条目时验证是否已点击过
        if(clicks.contains(position)) {
            holder.followView.setEnabled(false);
        }else holder.followView.setEnabled(true);

        //Log.e("comin-position", ""+position);
        //Log.e("comin-convertView-position", (int) convertView.findViewById(R.id.follow).getTag()+"");
        //Log.e("comin-convertView", convertView+"");
        //Log.e("comin-convertView-button", convertView.findViewById(R.id.follow).toString());

        //图标
        holder.imageView.setVisibility(View.VISIBLE);//正常显示
        if (TextUtils.isEmpty(image)){
            holder.imageView.setVisibility(View.GONE);//隐藏
        }else{
            Glide.with(mContext).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+image+"?x-oss-process=image/resize,w_202").into(holder.imageView);
        }
        holder.textView.setText(content);
        String text =mContext.getString(R.string.discover_topic_sum, sum);
        holder.sumView.setText(text);
        return convertView;
    }
    static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private TextView sumView;
        private Button followView;
    }
}
