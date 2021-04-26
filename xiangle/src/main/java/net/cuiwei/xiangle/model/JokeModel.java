package net.cuiwei.xiangle.model;

import android.content.Context;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Joke;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.JokeService;
import net.cuiwei.xiangle.utility.Http;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JokeModel {
    /**
     * 列表
     * @param topic_id
     * @param mlistener
     */
    public void list(long user_id, long topic_id, Map<String, String> map, Context context, OnListListener<Joke> mlistener) {
        JokeService service = Http.getInstance(context).create(JokeService.class);
        service.list(user_id, topic_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Joke>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseListResponse<Joke> re) {
                        if (re.err==0){
                            mlistener.onSuccess(re.data);
                        }else {
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
     * 详情
     * @param user_id
     * @param id
     * @param mlistener
     */
    public void get(long user_id, long id, Context context, final OnDetailListener<Joke> mlistener) {
        JokeService service = Http.getInstance(context).create(JokeService.class);
        service.get(user_id, id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<BaseDetailResponse<Joke>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(BaseDetailResponse<Joke> re) {
                Joke field=re.data;
                if (re.err==0){
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
     * 发布
     * @param user_id
     * @param mlistener
     */
    public void create(long user_id, Map<String, String> map, Context context, final OnDetailListener<HashMap<String, Object>> mlistener) {
        JokeService service = Http.getInstance(context).create(JokeService.class);
        service.create(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<HashMap<String, Object>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<HashMap<String, Object>> re) {
                        HashMap<String, Object> field=re.data;
                        if (re.err==0){
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
}
