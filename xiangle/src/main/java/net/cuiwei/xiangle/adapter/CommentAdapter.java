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
import net.cuiwei.xiangle.bean.Comment;

import java.util.ArrayList;

/**
 * 段子的评论列表
 */
public class CommentAdapter extends BaseAdapter {
    private ArrayList<Comment> Datas;
    private Context mContext;

    public CommentAdapter(ArrayList<Comment> datas, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.joke_comment_item, parent,false);
            holder.imageView = convertView.findViewById(R.id.avatar);
            holder.content = convertView.findViewById(R.id.content);
            holder.nickname = convertView.findViewById(R.id.nickname);
            convertView.setTag(holder);
        }else holder=(ViewHolder) convertView.getTag();
        //value
        String image=Datas.get(position).getAvatar();
        String content=Datas.get(position).getContent();
        String nickname=Datas.get(position).getNickname();

        holder.imageView.setVisibility(View.VISIBLE);//正常显示
        if (TextUtils.isEmpty(image)){
            holder.imageView.setVisibility(View.GONE);//隐藏
        }else{
            Glide.with(mContext).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+image+"?x-oss-process=image/circle,r_100/format,png").into(holder.imageView);
        }
        holder.content.setText(content);
        holder.nickname.setText(nickname);
        return convertView;
    }
    static class ViewHolder {
        private ImageView imageView;
        private TextView content;
        private TextView nickname;
    }
}
