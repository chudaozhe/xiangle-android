package net.cuiwei.xiangle.model;

import android.content.Context;
import android.widget.Toast;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Comment;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.CommentService;
import net.cuiwei.xiangle.utility.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

/**
 * 评论
 */
public class CommentModel {
    /**
     * 一条段子的评论列表
     *
     * @param mlistener
     */
    public void list(long joke_id, Map<String, String> map, Context context, OnListListener<Comment> mlistener) {
        CommentService service = Http.getInstance(context).create(CommentService.class);
        service.list(joke_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Comment> re) {
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
     * 我的评论列表
     *
     * @param mlistener
     */
    public void listMy(long user_id, Map<String, String> map, Context context, OnListListener<Comment> mlistener) {
        CommentService service = Http.getInstance(context).create(CommentService.class);
        service.listMy(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Comment> re) {
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
     * 发表评论
     * @param user_id
     * @param joke_id
     * @param map
     * @param context
     */
    public void create(long user_id, long joke_id, Map<String, String> map, Context context) {
        CommentService service = Http.getInstance(context).create(CommentService.class);
        Call<Void> call = service.create(user_id, joke_id, map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}