package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cuiwei.xiangle.MainActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.SettingActivity;
import net.cuiwei.xiangle.bean.User;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.UserModel;
import net.cuiwei.xiangle.view.CountDownButton;

import java.util.HashMap;

public class LoginFragment extends Fragment implements OnDetailListener<User>, View.OnClickListener {
    OnDetailListener<User> t;
    Button login;
    EditText mobileView;
    EditText passwordView;
    CountDownButton countDownButton;
    TextView toggle;//切换登录方式
    Boolean isCaptcha=true;//当前登录方式，默认验证码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("登录");
        SettingActivity activity=(SettingActivity)getActivity();
        activity.getToolbar().setVisibility(View.GONE);//隐藏toolbar

        View view = inflater.inflate(R.layout.fragment_login2, container, false);
        mobileView = view.findViewById(R.id.mobile);
        passwordView = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        login.setEnabled(false);
        login.setBackgroundResource(R.color.orange2);

        t = this;

        mobileView.addTextChangedListener(new TextWatcher());
        passwordView.addTextChangedListener(new TextWatcher());

        view.findViewById(R.id.close).setOnClickListener(this);
        countDownButton =view.findViewById(R.id.count_down);
        countDownButton.setOnClickListener(this);
        toggle=view.findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
        return view;
    }

    @Override
    public void onSuccess(User field) {
        //todo 登录成功后的处理
        TokenModel model=new TokenModel(getContext());
        model.setCache(field.getId(), field.getNickname(), field.getAvatar(), field.getToken());
        //Toast.makeText(getContext(), "mobile:"+field.getUname()+" token:"+field.getToken(), Toast.LENGTH_SHORT).show();
        getActivity().finish();
        //todo 通知myFragment刷新数据
        //简单粗暴,跳首页
        Intent intent=new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "出错了哦！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                getActivity().finish();
                countDownButton.cancel();
                break;
            case R.id.count_down:
                //todo 正则验证手机号
                String mobile = mobileView.getText().toString().trim();
                if (validatePhoneNumber(mobile)){
                    countDownButton.start();
                    UserModel model=new UserModel();
                    model.sendCaptcha(mobile, getContext());
                }else Toast.makeText(getContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toggle:
                if (isCaptcha){
                    //密码
                    isCaptcha=false;
                    toggle.setText("验证码登录");
                    passwordView.setHint("请输入密码");
                    passwordView.setText("");
                    passwordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    countDownButton.setVisibility(View.GONE);
                }else {
                    //验证码
                    isCaptcha=true;
                    toggle.setText("密码登录");
                    passwordView.setHint("请输入验证码");
                    passwordView.setText("");
                    passwordView.setInputType(InputType.TYPE_CLASS_TEXT);
                    countDownButton.setVisibility(View.VISIBLE);
                }

                break;
        }
    }
    /**
     * 验证手机号码是否合法
     */
    public static boolean validatePhoneNumber(String mobile) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobile) && mobile.matches(telRegex);
    }
    class TextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.e("textChange", "com in-beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.e("textChange", "com in-onTextChanged");
            String mobile = mobileView.getText().toString().trim();
            String password = passwordView.getText().toString().trim();
            if (!mobile.equals("") && !password.equals("")) {
                login.setEnabled(true);
                login.setBackgroundResource(R.drawable.button_selector);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserModel model=new UserModel();
                        if (isCaptcha){
                            HashMap<String, String> map=new HashMap<String, String>();
                            map.put("captcha", password);
                            map.put("type", "1");
                            model.mobileLogin(mobile, map, getContext(), t);
                            //Toast.makeText(getActivity(), "mobile:" + mobile + " code:" + password, Toast.LENGTH_LONG).show();
                        }else{
                            HashMap<String, String> map=new HashMap<String, String>();
                            map.put("password", password);
                            map.put("type", "0");
                            model.mobileLogin(mobile, map, getContext(), t);
                            //Toast.makeText(getActivity(), "mobile:" + mobile + " password:" + password, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                login.setEnabled(false);
                login.setBackgroundResource(R.color.orange2);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.e("textChange", "com in-afterTextChanged");
        }

    }
}