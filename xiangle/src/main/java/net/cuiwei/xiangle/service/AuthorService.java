package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface AuthorService {
    //编辑详情
    @GET("/user/{user_id}/author/{author_id}")
    Observable<BaseDetailResponse<Author>> get(@Path("user_id") long user_id, @Path("author_id") long author_id);

    //作者发表的段子列表
    @GET("/user/{user_id}/author/{author_id}/joke")
    Observable<BaseListResponse<Joke>> list(@Path("user_id") long user_id, @Path("author_id") long author_id, @QueryMap Map<String, String> map);

    //拉黑作者
    @POST("/user/{user_id}/user/{to_user_id}/shield")
    Call<Void> shield(@Path("user_id") long user_id, @Path("to_user_id") long to_user_id);
}
