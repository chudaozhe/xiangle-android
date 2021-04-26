package net.cuiwei.xiangle.service;

import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Menus;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface MenuService {
    //'我的'菜单
    @GET("/user/my/menu")
    Observable<BaseListResponse<Menus>> myList();

    //'设置'菜单
    @GET("/user/settings/menu")
    Observable<BaseListResponse<Menus>> settingsList();
}
