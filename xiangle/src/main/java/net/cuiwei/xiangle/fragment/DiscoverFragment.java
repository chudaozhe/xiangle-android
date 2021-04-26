package net.cuiwei.xiangle.fragment;

import android.os.Bundle;

import android.util.Log;
import android.widget.*;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.bean.BaseList2Response;
import net.cuiwei.xiangle.bean.Category;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.TopicService;
import net.cuiwei.xiangle.utility.DisplayUtil;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;


public class DiscoverFragment extends Fragment implements OnListListener<Topic>, View.OnClickListener {
    LinearLayout mLinearLayout;//左侧
    private ScrollView mScrollView;
    //装装ScrollView的item的TextView的数组
    private TextView[] textViewArray;
    //装ScrollView的item的数组
    private View[] views;
    //page list
    ArrayList<Category> data;
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_discover, container, false);

        ImageView flashView=view.findViewById(R.id.flashImage);
        mScrollView= view.findViewById(R.id.scrollview);
        mLinearLayout= view.findViewById(R.id.linearlayout);
        Glide.with(this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/flash/1.jpg").into(flashView);
//         Log.e("screen-flashHeight", String.valueOf(getFlashViewHeight(flashView)));

        //话题列表
        TopicService service = Http.getInstance(getContext()).create(TopicService.class);
        Call<BaseDetailResponse<ArrayList<Category>>> call = service.listByCategory(new HashMap<>());
        call.enqueue(new Callback<BaseDetailResponse<ArrayList<Category>>>() {
            @Override
            public void onResponse(Call<BaseDetailResponse<ArrayList<Category>>> call, Response<BaseDetailResponse<ArrayList<Category>>> response) {
                BaseDetailResponse<ArrayList<Category>> result=response.body();
                Log.e("categorys", result.data.toString());
                data=result.data;
                Log.e("data", data.toString());

                textViewArray=new TextView[data.size()];
                views=new View[data.size()];
                addLeftView();
                changeTextColor(0);
                views[0].performClick();//默认点击
            }

            @Override
            public void onFailure(Call<BaseDetailResponse<ArrayList<Category>>> call, Throwable t) {
                Log.e("error", "com in");
            }
        });
        return view;
    }
    public void addLeftView(){
        View view;
        for(int x=0;x<data.size();x++){
            Category item = data.get(x);
            view = View.inflate(getContext(), R.layout.item_scrollview, null);
            view.setId(x);
            view.setOnClickListener(this);
            TextView tv= (TextView) view.findViewById(R.id.textview);
            String title = item.getName();
            tv.setText(title);
            mLinearLayout.addView(view);

            textViewArray[x]=tv;
            views[x]=view;
        }
    }
    public void setView(ArrayList<Topic> list){

    }
    @Override
    public void onSuccess(BaseList2Response<Topic> data) {
        setView(data.list);
    }
    @Override
    public void onError() {
        Toast.makeText(getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        changeTextColor(v.getId());
        changeTextLocation(v.getId());

        Fragment fragment=new DiscoverTopicFragment();
        Bundle args = new Bundle();
        Category item = data.get(v.getId());
        args.putLong("category_id", item.getId());
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
    }
    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < textViewArray.length; i++) {
            if(i!=id){
                textViewArray[i].setBackgroundResource(android.R.color.transparent);
                textViewArray[i].setTextColor(0xff000000);
            }else {
                textViewArray[id].setBackgroundResource(R.drawable.tvbar);
                textViewArray[id].setTextColor(0xffff8100);
            }
        }

    }

    /**
     * 改变栏目位置
     */
    private void changeTextLocation(int index) {
        //views[clickPosition].getTop()针对其父视图的顶部相对位置
        int x = (views[index].getTop() - mScrollView.getHeight() / 2);
        mScrollView.smoothScrollTo(0, x);
    }
   private int getFlashViewHeight(ImageView flashView){
       int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
       int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
       flashView.measure(w, h);
       return DisplayUtil.dip2px(getContext(), flashView.getMeasuredHeight());
   }
}