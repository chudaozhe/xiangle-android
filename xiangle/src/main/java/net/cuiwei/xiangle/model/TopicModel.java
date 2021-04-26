package net.cuiwei.xiangle.model;

import android.content.Context;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.TopicService;
import net.cuiwei.xiangle.utility.Http;

import java.util.Map;

public class TopicModel {
    /**
     * 详情
     * @param user_id
     * @param topic_id
     * @param mlistener
     */
    public void get(long user_id, long topic_id, Context context, final OnDetailListener<Topic> mlistener) {
        TopicService service = Http.getInstance(context).create(TopicService.class);
        service.get(user_id, topic_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseDetailResponse<Topic>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseDetailResponse<Topic> re) {
                        if (re.err==0){
                            Topic field=re.data;
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
    //分类列表
//    public void listByCategory(Map<String, String> map, Context context) {
//
//    }
    /**
     * 话题列表
     * @param mlistener
     */
    public void list(Long user_id, long category_id, Map<String, String> map, Context context, OnListListener<Topic> mlistener) {
        TopicService service = Http.getInstance(context).create(TopicService.class);
        service.list(user_id, category_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Topic>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Topic> re) {
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
}