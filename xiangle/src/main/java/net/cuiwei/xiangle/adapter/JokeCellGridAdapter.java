package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.cuiwei.xiangle.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 段子cell里的三个按钮：点赞", "评论", "收藏"
 */
public class JokeCellGridAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> Datas;
    private Context mContext;

    public JokeCellGridAdapter(ArrayList<HashMap<String, Object>> datas, Context mContext) {
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
        JokeCellGridAdapter.ViewHolder holder;
        if (convertView==null){
            holder= new JokeCellGridAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.joke_item_grid_item, parent,false);
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.textView = convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else holder=(JokeCellGridAdapter.ViewHolder) convertView.getTag();
        //value
        Integer image=(Integer) Datas.get(position).get("icon");
        String content=(String) Datas.get(position).get("title");

        holder.imageView.setImageResource(image);
        holder.textView.setText(content);
        return convertView;
    }
    static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
