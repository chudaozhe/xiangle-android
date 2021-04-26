package net.cuiwei.xiangle.model;

import android.content.Context;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Menus;
import net.cuiwei.xiangle.listener.OnListListener;
import net.cuiwei.xiangle.service.MenuService;
import net.cuiwei.xiangle.utility.Http;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MenuModel {
    /**
     * 我的-菜单
     * @param mlistener
     */
    public void myList(Context context, OnListListener<Menus> mlistener) {
        MenuService service = Http.getInstance(context).create(MenuService.class);
        service.myList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Menus>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseListResponse<Menus> re) {
                        if (re.err==0){
                            mlistener.onSuccess(re.data);
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
     * 设置-菜单
     * @param mlistener
     */
    public void settingsList(Context context, OnListListener<Menus> mlistener) {
        MenuService service = Http.getInstance(context).create(MenuService.class);
        service.settingsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BaseListResponse<Menus>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(BaseListResponse<Menus> re) {
                        if (re.err==0){
                            mlistener.onSuccess(re.data);
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
