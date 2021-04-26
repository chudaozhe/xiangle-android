package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.bean.Topic;

import java.util.ArrayList;

/**
 * 没有 关注/取消操作
 */
public class TopicAdapter2 extends BaseAdapter{
    public static ArrayList<Topic> Datas;
    public Context mContext;

    public TopicAdapter2(ArrayList<Topic> datas, Context mContext) {
        Datas = datas;
        this.mContext = mContext;
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
        ViewHolder holder;
        //value
        String image=Datas.get(position).getImage();
        String content=Datas.get(position).getName();
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
        //隐藏按钮
        holder.followView.setVisibility(View.GONE);
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
