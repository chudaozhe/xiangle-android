package net.cuiwei.xiangle.model;

import android.content.Context;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Favorite;
import net.cuiwei.xiangle.bean.UserFollow;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.FavoriteService;
import net.cuiwei.xiangle.service.FollowService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

/**
 * 关注作者/话题
 */
public class FollowModel {
    /**
     * 粉丝列表
     *
     * @param mlistener
     */
    public void listByFans(long user_id, Map<String, String> map, Context context, OnListListener<UserFollow> mlistener) {
        FollowService service = Http.getInstance(context).create(FollowService.class);
        service.listByFans(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<UserFollow>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<UserFollow> re) {
                        if (re.err == 0) {
                            mlistener.onSuccess(re.data);
                        } else {
                            //mlistener.onError();
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
     * 关注列表
     *
     * @param mlistener
     */
    public void list(long user_id, Map<String, String> map, Context context, OnListListener<UserFollow> mlistener) {
        FollowService service = Http.getInstance(context).create(FollowService.class);
        service.list(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<UserFollow>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<UserFollow> re) {
                        if (re.err==0) {
                            mlistener.onSuccess(re.data);
                        } else {
                            //mlistener.onError();
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
     * 关注/取消作者
     * @param user_id
     * @param to_user_id
     * @param isFollow
     * @param context
     */
    public void doFollow(long user_id, long to_user_id, boolean isFollow, Context context) {
        FollowService service = Http.getInstance(context).create(FollowService.class);
        Call<Void> call;
        if (isFollow){
            call = service.follow(user_id, to_user_id);
        }else call = service.unFollow(user_id, to_user_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(context, "关注/取消 成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "关注/取消 失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 关注/取消话题
     * @param user_id
     * @param topic_id
     * @param isFollow
     * @param context
     */
    public void doFollow2(long user_id, long topic_id, boolean isFollow, Context context) {
        FollowService service = Http.getInstance(context).create(FollowService.class);
        Call<Void> call;
        if (isFollow){
            call = service.follow2(user_id, topic_id);
        }else call = service.unFollow2(user_id, topic_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(context, "关注/取消 成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "关注/取消 失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}