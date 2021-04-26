package net.cuiwei.xiangle.model;

import android.content.Context;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.service.FeedbackService;
import net.cuiwei.xiangle.utility.Http;

import java.util.HashMap;

public class FeedbackModel {
    /**
     * 发布
     * @param user_id
     * @param content
     * @param contact
     * @param mlistener
     */
    public void create(long user_id, String content, String contact, Context context, final OnDetailListener<HashMap<String, Object>> mlistener) {
        FeedbackService service = Http.getInstance(context).create(FeedbackService.class);
        service.create(user_id, content, contact)
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
