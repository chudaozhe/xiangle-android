package net.cuiwei.xiangle.service;

import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Joke;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.Map;

public interface JokeService {
    //段子列表,type,keyword,page,max
    @GET("/user/{user_id}/topic/{topic_id}/joke")
    Observable<BaseListResponse<Joke>> list(@Path("user_id") long user_id, @Path("topic_id") long topic_id, @QueryMap Map<String, String> map);

    //段子详情
    @GET("/user/{user_id}/joke/{id}")
    Observable<BaseDetailResponse<Joke>> get(@Path("user_id") long user_id, @Path("id") long id);

    //发表段子,type,topic_id,content,image,images,video,
    @FormUrlEncoded
    @POST("/user/{user_id}/joke")
    Observable<BaseDetailResponse<HashMap<String, Object>>> create(@Path("user_id") long user_id, @FieldMap Map<String, String> map);
}
