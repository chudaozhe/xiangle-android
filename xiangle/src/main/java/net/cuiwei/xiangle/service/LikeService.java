package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Like;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface LikeService {
    //点赞列表
    @GET("/user/{user_id}/like")
    Observable<BaseListResponse<Like>> list(@Path("user_id") long user_id, @QueryMap Map<String, String> map);

    //点赞
    @PUT("/user/{user_id}/joke/{joke_id}/like")
    Call<Void> like(@Path("user_id") long user_id, @Path("joke_id") long joke_id);

    //取消点赞
    @DELETE("/user/{user_id}/joke/{joke_id}/like")
    Call<Void> unLike(@Path("user_id") long user_id, @Path("joke_id") long joke_id);
}
