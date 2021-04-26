package net.cuiwei.xiangle.model;

import android.content.Context;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Favorite;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.FavoriteService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

/**
 * 我的收藏
 */
public class FavoriteModel {
    /**
     * 列表
     *
     * @param mlistener
     */
    public void list(long user_id, Map<String, String> map, Context context, OnListListener<Favorite> mlistener) {
        FavoriteService service = Http.getInstance(context).create(FavoriteService.class);
        service.list(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Favorite>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Favorite> re) {
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
     * 收藏/取消收藏
     * @param user_id
     * @param joke_id
     * @param isFavorite
     * @param context
     */
    public void doFavorite(long user_id, long joke_id, boolean isFavorite, Context context) {
        FavoriteService service = Http.getInstance(context).create(FavoriteService.class);
        Call<Void> call;
        if (isFavorite){
            call = service.favorite(user_id, joke_id);
        }else call = service.unFavorite(user_id, joke_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(context, "收藏/取消 成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "收藏/取消 失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}