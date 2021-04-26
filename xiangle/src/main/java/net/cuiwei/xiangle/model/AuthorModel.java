package net.cuiwei.xiangle.model;

import android.content.Context;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.*;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.AuthorService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class AuthorModel {
    /**
     * 详情
     * @param user_id
     * @param author_id
     * @param mlistener
     */
    public void get(long user_id, long author_id, Context context, final OnDetailListener<Author> mlistener) {
        AuthorService service = Http.getInstance(context).create(AuthorService.class);
        service.get(user_id, author_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<Author>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<Author> re) {
                        if (re.err==0){
                            Author field=re.data;
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
     * 列表
     * @param author_id
     * @param mlistener
     */
    public void list(long user_id, long author_id, Map<String, String> map, Context context, OnListListener<Joke> mlistener) {
        AuthorService service = Http.getInstance(context).create(AuthorService.class);
        service.list(user_id, author_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Joke>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Joke> re) {
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
         * 拉黑
         * @param user_id
         * @param to_user_id
         * @param context
         */
        public void shield(long user_id, long to_user_id, Context context) {
            AuthorService service = Http.getInstance(context).create(AuthorService.class);
            Call<Void> call = service.shield(user_id, to_user_id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(context, "拉黑成功，您将看不到他的作品~", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "拉黑失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
}
