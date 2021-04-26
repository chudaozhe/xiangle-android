package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.cuiwei.xiangle.MainActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.SettingActivity;
import net.cuiwei.xiangle.bean.User;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.UserModel;


public class RegisterFragment extends Fragment implements OnDetailListener<User> {
    OnDetailListener<User> t;
    Button register;
    EditText usernameView;
    EditText passwordView;
    EditText password2View;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("注册");
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        usernameView = view.findViewById(R.id.username);
        passwordView = view.findViewById(R.id.password);
        password2View = view.findViewById(R.id.password2);
        register = view.findViewById(R.id.register);
        register.setEnabled(false);
        register.setBackgroundResource(R.color.orange2);

        t = this;

        usernameView.addTextChangedListener(new TextWatcher());
        passwordView.addTextChangedListener(new TextWatcher());
        password2View.addTextChangedListener(new TextWatcher());
        Button login= view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SettingActivity.class);
                String action="my_login";
                intent.setAction(action);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onSuccess(User field) {
        //todo 注册成功后的处理
        TokenModel model=new TokenModel(getContext());
        model.setCache(field.getId(), field.getNickname(), field.getAvatar(), field.getToken());
        //Toast.makeText(getContext(), "username:"+field.getUname()+" token:"+field.getToken(), Toast.LENGTH_SHORT).show();
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

    class TextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.e("textChange", "com in-beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.e("textChange", "com in-onTextChanged");
            String username = usernameView.getText().toString().trim();
            String password = passwordView.getText().toString().trim();
            String password2 = password2View.getText().toString().trim();
            if (!username.equals("") && !password.equals("") && !password2.equals("")) {
                register.setEnabled(true);
                register.setBackgroundResource(R.drawable.button_selector);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (password.equals(password2)){
                            UserModel model=new UserModel();
                            model.register(username, password, getContext(), t);
                        }else Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                register.setEnabled(false);
                register.setBackgroundResource(R.color.orange2);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.e("textChange", "com in-afterTextChanged");
        }

    }
}