package net.cuiwei.xiangle.model;

import android.content.Context;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Notify;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.NotifyService;
import net.cuiwei.xiangle.utility.Http;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Map;

public class NotifyModel {
    /**
     * 列表
     *
     * @param mlistener
     */
    public void list(long user_id, Map<String, String> map, Context context, OnListListener<Notify> mlistener) {
        NotifyService service = Http.getInstance(context).create(NotifyService.class);
        service.list(user_id, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Notify>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseListResponse<Notify> re) {
                        if (re.err==0) {
                            mlistener.onSuccess(re.data);
                        } else {
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