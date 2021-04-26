package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.bean.UserFollow;

import java.util.ArrayList;

/**
 * 我关注的用户
 */
public class MyUserAdapter extends BaseAdapter {
    private ArrayList<UserFollow> Datas;
    private Context mContext;

    public MyUserAdapter(ArrayList<UserFollow> datas, Context mContext) {
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
        if (convertView==null){
            holder= new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_follow_item, parent,false);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.textView = convertView.findViewById(R.id.content);
            holder.describe = convertView.findViewById(R.id.describe);
            convertView.setTag(holder);
        }else holder=(ViewHolder) convertView.getTag();
        //value
        String image=Datas.get(position).getAvatar();
        String content=Datas.get(position).getNickname();
        String describe=Datas.get(position).getQuotes();
        if (TextUtils.isEmpty(describe)) describe="这家伙什么也没留下";

        holder.imageView.setVisibility(View.VISIBLE);//正常显示
        if (TextUtils.isEmpty(image)){
            holder.imageView.setVisibility(View.GONE);//隐藏
        }else{
            Glide.with(mContext).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+image+"?x-oss-process=image/circle,r_100/format,png").into(holder.imageView);
        }
        holder.textView.setText(content);
        holder.describe.setText(describe);
        return convertView;
    }
    static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private TextView describe;
    }
}
