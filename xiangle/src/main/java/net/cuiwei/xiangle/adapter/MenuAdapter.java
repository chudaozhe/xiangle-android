package net.cuiwei.xiangle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.bean.Menus;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int SITES = 0;//标题 跨一列 也就是合并两列
    public static final int SITE = 1;//不跨列
    //所有数据的集合，将标题和数据项，全部装在到这个集合中，在适配器中利用viewtype来区分，并显示不同的布局
    private List<Object> items = new ArrayList<>();
    private Context context;

    public MenuAdapter(Context context, List<Object> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);//获取mInflater对象
        switch (viewType) {//根据viewtyupe来区分，是标题还是数据项
            case SITES://标题，加载显示标题的item布局，就一个textview显示文本，这里我们自顶一个标题的viewholder->MenusHolder
                final MenusHolder MenusHolder = new MenusHolder(mInflater.inflate(R.layout.menus_item, parent, false));
                //点击事件
                MenusHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onItemClickListener != null){
                            onItemClickListener.onClick(MenusHolder.itemView , MenusHolder.getLayoutPosition());
                        }
                    }
                });
                return MenusHolder;
            case SITE://数据项，雷同不赘述了，标题和数据项的item布局和veiwholder都不会相互影响的
                final MenuHolder MenuHolder = new MenuHolder(mInflater.inflate(R.layout.menu_item, parent, false));
                MenuHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onItemClickListener != null){
                            onItemClickListener.onClick(MenuHolder.itemView , MenuHolder.getLayoutPosition());
                        }
                    }
                });
                return MenuHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        //这个方法很重要，这里根据position取出items集合中的对象，用instanceof判断他是标题还是数据项，来返回对应的标识
        if (items.get(position) instanceof Menus) {//根据items数据类型的不同来判断他是标题还是数据项
            return SITES;//标题
        } else if (items.get(position) instanceof Menus.Menu) {
            return SITE;//数据项
        } else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //根据getItemViewType绑定view进行赋值显示
        switch (holder.getItemViewType()) {
            case SITES://标题
                //MenusHolder MenusHolder = (MenusHolder) holder;
                //MenusHolder.title.setText(((Menus) items.get(position)).getTitle());
                break;
            case SITE://数据项
                MenuHolder MenuHolder = (MenuHolder) holder;
                MenuHolder.title.setText(((Menus.Menu) items.get(position)).getTitle());
                //右箭头
                Drawable rightArrow = context.getResources().getDrawable(R.mipmap.jiantou);
                rightArrow.setBounds(0, 0, 60, 60);//设置边界 right-left = drawable的宽，top - bottom = drawable的高
                //左箭头
                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable leftArrow, Transition<? super Drawable> transition) {
                        leftArrow.setBounds(0, 0, 80, 80);//设置边界 right-left = drawable的宽，top - bottom = drawable的高
                        MenuHolder.title.setCompoundDrawables(leftArrow,null, rightArrow,null);//画在右边
                    }
                };
                Glide.with(context).load("https://xiangle.cuiwei.net/"+((Menus.Menu) items.get(position)).getIcon()).into(simpleTarget);
                break;
        }
    }

    /**
     * 公布点击事件出去
     */
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View itemview, int position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 数据项的viewholder  一个文本textview一个cion  imageview
     */
    private class MenuHolder extends RecyclerView.ViewHolder {

        TextView title;

        MenuHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    /**
     * 标题的viewholder  只有一个textview
     */
    private class MenusHolder extends RecyclerView.ViewHolder {

        TextView title;

        MenusHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}