package net.cuiwei.xiangle.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.*;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.service.UserService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class UserModel {
    /**
     * 更新用户信息
     * @param user_id
     * @param map
     * @param context
     */
    public void update(long user_id, Map<String, String> map, Context context) {
        UserService service = Http.getInstance(context).create(UserService.class);
        Call<Void> call = service.update(user_id, map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 详情
     * @param user_id
     * @param mlistener
     */
    public void get(long user_id, Context context, final OnDetailListener<User> mlistener) {
        UserService service = Http.getInstance(context).create(UserService.class);
        service.get(user_id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<BaseDetailResponse<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(BaseDetailResponse<User> re) {
                if (re.err==0){
                    User field=re.data;
                    mlistener.onSuccess(field);
                }else {
                    mlistener.onError();
                }
            }
            @Override
            public void onError(Throwable e) {
                mlistener.onError();
            }
            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 登录
     * @deprecated
     * @param username
     * @param password
     * @param mlistener
     */
    public void login(String username, String password, Context context, final OnDetailListener<User> mlistener) {
        UserService service = Http.getInstance(context).create(UserService.class);
        service.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<User> re) {
                        if (re.err==0){
                            User field=re.data;
                            mlistener.onSuccess(field);
                        }else {
                            mlistener.onError();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("model-err", e.toString());
                        mlistener.onError();
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * 手机号登录
     * @param mobile
     * @param map
     * @param mlistener
     */
    public void mobileLogin(String mobile, Map<String, String> map, Context context, final OnDetailListener<User> mlistener) {
        UserService service = Http.getInstance(context).create(UserService.class);
        service.mobileLogin(mobile, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<User> re) {
                        if (re.err==0){
                            User field=re.data;
                            mlistener.onSuccess(field);
                        }else {
                            mlistener.onError();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("model-err", e.toString());
                        mlistener.onError();
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * 发送验证码
     * @param mobile
     * @param context
     */
    public void sendCaptcha(String mobile, Context context) {
        UserService service = Http.getInstance(context).create(UserService.class);
        service.sendCaptcha(mobile).enqueue(new Callback<BaseDetailResponse<String>>() {
            @Override
            public void onResponse(Call<BaseDetailResponse<String>> call, Response<BaseDetailResponse<String>> response) {
                BaseDetailResponse<String> re=response.body();
                Log.e("captcha", String.valueOf(re.err));
//                Log.e("captcha", re.data);//re.msg
                String text="";
                if (re.err==0){
                    text="已发送";
                }else if(re.err==405){
                    text="手机号已存在";
                }
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseDetailResponse<String>> call, Throwable t) {
                Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 注册
     * @deprecated
     * @param username
     * @param password
     * @param mlistener
     */
    public void register(String username, String password, Context context, final OnDetailListener<User> mlistener) {
        UserService service = Http.getInstance(context).create(UserService.class);
        service.register(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<User> re) {
                        if (re.err==0){
                            User field=re.data;
                            mlistener.onSuccess(field);
                        }else {
                            mlistener.onError();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("model-err", e.toString());
                        mlistener.onError();
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
}
