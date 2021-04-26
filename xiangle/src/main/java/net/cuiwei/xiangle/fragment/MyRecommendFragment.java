package net.cuiwei.xiangle.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.cuiwei.xiangle.R;


public class MyRecommendFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("推荐给朋友");
        View view= inflater.inflate(R.layout.fragment_my_recommend, container, false);
        return view;
    }
}