package net.cuiwei.xiangle.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import net.cuiwei.xiangle.R;

public class FullScreenDialog extends DialogFragment {

    public VideoView videoView;
    private Window window;
    private String url;
    public static FullScreenDialog newInstance(String url) {
        FullScreenDialog fragment = new FullScreenDialog();
        fragment.url=url;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.full_screen_dialog_video, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        videoView=view.findViewById(R.id.videoView);
        //android低版本会出现黑屏的问题：有声音但没画面setZOrderOnTop(true)可以解决
        //因为VideoView 继承于SurfaceView，而SurfaceView会出现黑屏或者透明的问题。这个方法是将SurfaceView挪到上层。这样就能简单的避免透明的发生。
        videoView.setZOrderOnTop(true);

        //String packageName=getActivity().getPackageName();
        //videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.f123));
        //Log.e("video1", packageName);
        Log.e("video1-url", url);
        videoView.setVideoPath(url);

        videoView.seekTo(200);//设置播放位置,毫秒
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //播放结束
                videoView.seekTo(200);
                Log.e("video1", "play done");
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 下面这些设置必须在此方法(onStart())中才有效

        window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        //window.setDimAmount(0.0f);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.dimAmount=1.0f;//遮照颜色 全黑；0.0f和1.0f
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);

    }
}