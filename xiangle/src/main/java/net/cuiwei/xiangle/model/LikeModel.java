package net.cuiwei.xiangle.model;

import android.content.Context;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Like;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.LikeService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

/**
 * 我的点赞
 */
public class LikeModel {
    /**
     * 列表
     *
     * @param mlistener
     */
    public void list(long user_id, Map<String, String> map, Context context, OnListListener<Like> mlistener) {
        LikeService service = Http.getInstance(context).create(LikeService.class);
        service.list(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Like>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Like> re) {
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
     * 收藏/取消点赞
     * @param user_id
     * @param joke_id
     * @param isLike
     * @param context
     */
    public void doLike(long user_id, long joke_id, boolean isLike, Context context) {
        LikeService service = Http.getInstance(context).create(LikeService.class);
        Call<Void> call;
        if (isLike){
            call = service.like(user_id, joke_id);
        }else call = service.unLike(user_id, joke_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(context, "点赞/取消 成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "点赞/取消 失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}