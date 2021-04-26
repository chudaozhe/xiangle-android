package net.cuiwei.xiangle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.cuiwei.xiangle.MainActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.WebViewActivity;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.utility.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MySettingFragment extends Fragment {
    private GroupListAdapter adapter = null;
    private ListView listView = null;
    private List<String> list = new ArrayList<String>();
    private List<String> listTag = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("设置");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_setting, container, false);
        setData();
        adapter = new GroupListAdapter(getContext(), list, listTag);
        listView = view.findViewById(R.id.group_list);
        //footerView
        View footerView = ((LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.my_setting_listview_footer, null, false);
        TextView textView=footerView.findViewById(R.id.tv_my_setting_footer);
        textView.setText("当前版本 "+ AppUtil.getVersionName(getContext()) +"("+AppUtil.getVersionCode(getContext())+")\nAll Rights Reserved By xiangle");
        listView.addFooterView(footerView);

        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //webview
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                Integer[] positions={1, 2};
                if(Arrays.asList(positions).contains(position)){
                    String title="用户协议";
                    String url="https://www.cuiwei.net/agreement.html";
                    if (position==2){
                        title="隐私政策";
                        url="https://www.cuiwei.net/private.html";
                    }
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                if (position==4){
                    TokenModel cache=new TokenModel(getContext());
                    cache.delToken();
                    //简单粗暴,跳首页
                    Intent intent2=new Intent(getContext(), MainActivity.class);
                    startActivity(intent2);
//                    Toast.makeText(getContext(), "退出", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
    public void setData(){
        list.add("");//组名为空
        listTag.add("");
        list.add("用户协议");
        list.add("隐私政策");

        TokenModel cache=new TokenModel(getContext());
        String token=cache.getToken();
        if (!TextUtils.isEmpty(token)){
            list.add("");//组名为空
            listTag.add("");
            list.add("退出");
        }

    }
    private static class GroupListAdapter extends ArrayAdapter<String> {

        private List<String> listTag = null;
        public GroupListAdapter(Context context, List<String> objects, List<String> tags) {
            super(context, 0, objects);
            this.listTag = tags;
        }

        @Override
        public boolean isEnabled(int position) {
            if(listTag.contains(getItem(position))){
                return false;
            }
            return super.isEnabled(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(listTag.contains(getItem(position))){
                view = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item_tag, null);
            }else{
                view = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.group_list_item_text);
            textView.setText(getItem(position));
            return view;
        }
    }
}