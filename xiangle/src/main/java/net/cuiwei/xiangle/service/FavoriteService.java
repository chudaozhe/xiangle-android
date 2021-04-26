package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Favorite;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface FavoriteService {
    //收藏列表
    @GET("/user/{user_id}/favorite")
    Observable<BaseListResponse<Favorite>> list(@Path("user_id") long user_id, @QueryMap Map<String, String> map);

    //收藏
    @PUT("/user/{user_id}/joke/{joke_id}/favorite")
    Call<Void> favorite(@Path("user_id") long user_id, @Path("joke_id") long joke_id);

    //取消收藏
    @DELETE("/user/{user_id}/joke/{joke_id}/favorite")
    Call<Void> unFavorite(@Path("user_id") long user_id, @Path("joke_id") long joke_id);
}
