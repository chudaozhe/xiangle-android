package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.UserFollow;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;


public interface FollowService {
    //粉丝列表
    @GET("/user/{user_id}/fans")
    Observable<BaseListResponse<UserFollow>> listByFans(@Path("user_id") long user_id, @QueryMap Map<String, String> map);

    //关注列表
    @GET("/user/{user_id}/follow")
    Observable<BaseListResponse<UserFollow>> list(@Path("user_id") long user_id, @QueryMap Map<String, String> map);

    //关注作者
    @PUT("/user/{user_id}/user/{to_user_id}/follow")
    Call<Void> follow(@Path("user_id") long user_id, @Path("to_user_id") long to_user_id);
    //关注话题
    @PUT("/user/{user_id}/topic/{topic_id}/follow")
    Call<Void> follow2(@Path("user_id") long user_id, @Path("topic_id") long topic_id);

    //取消关注作者
    @DELETE("/user/{user_id}/user/{to_user_id}/follow")
    Call<Void> unFollow(@Path("user_id") long user_id, @Path("to_user_id") long to_user_id);
    //取消关注话题
    @DELETE("/user/{user_id}/topic/{topic_id}/follow")
    Call<Void> unFollow2(@Path("user_id") long user_id, @Path("topic_id") long topic_id);
}
