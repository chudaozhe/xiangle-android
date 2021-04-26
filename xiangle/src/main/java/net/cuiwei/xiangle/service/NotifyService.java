package net.cuiwei.xiangle.service;

import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Notify;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface NotifyService {
    //消息列表
    @GET("/user/{user_id}/notify")
    Observable<BaseListResponse<Notify>> list(@Path("user_id") long user_id, @QueryMap Map<String, String> map);
}
