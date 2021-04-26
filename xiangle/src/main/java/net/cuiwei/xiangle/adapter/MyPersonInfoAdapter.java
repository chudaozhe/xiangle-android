package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.cuiwei.xiangle.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPersonInfoAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> Datas;
    private Context mContext;

    public MyPersonInfoAdapter(ArrayList<HashMap<String, String>> datas, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_person_info_item, parent,false);
            holder.label = convertView.findViewById(R.id.label);
            holder.value = convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        }else holder=(ViewHolder) convertView.getTag();
        //右箭头
        Drawable rightArrow = mContext.getResources().getDrawable(R.mipmap.jiantou);
        rightArrow.setBounds(0, 0, 60, 60);//设置边界 right-left = drawable的宽，top - bottom = drawable的高
        holder.value.setCompoundDrawables(null,null, rightArrow,null);//画在右边

        //value
        String label=Datas.get(position).get("label");
        String value=Datas.get(position).get("value");

        holder.label.setText(label);
        holder.value.setText(value);
        return convertView;
    }
    static class ViewHolder {
        private TextView label;
        private TextView value;
    }
}
