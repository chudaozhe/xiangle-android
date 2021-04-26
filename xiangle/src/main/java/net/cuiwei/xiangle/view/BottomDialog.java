package net.cuiwei.xiangle.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import net.cuiwei.xiangle.PublishActivity;
import net.cuiwei.xiangle.R;

import java.util.Arrays;

public class BottomDialog extends DialogFragment implements View.OnClickListener {

    private Window window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.publish_dialog_bottom, null);
        view.findViewById(R.id.publish_text).setOnClickListener(this);
        view.findViewById(R.id.publish_picture).setOnClickListener(this);
        view.findViewById(R.id.publish_video).setOnClickListener(this);
        view.findViewById(R.id.publish_cancel).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 下面这些设置必须在此方法(onStart())中才有效

        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        Integer[] ids={R.id.publish_text, R.id.publish_picture, R.id.publish_video};
        if (Arrays.asList(ids).contains(v.getId())){
            Intent intent = new Intent(getActivity(), PublishActivity.class);
            String action="text";
            if (v.getId()==R.id.publish_picture) action="picture";
            if (v.getId()==R.id.publish_video) action="video";
            intent.setAction(action);
            startActivity(intent);
            dismiss();
        }else {
            //隐藏dialog
            dismiss();
        }
    }
}