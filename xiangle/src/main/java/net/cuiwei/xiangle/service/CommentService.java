package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Comment;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface CommentService {
    //段子评论列表
    @GET("/user/joke/{id}/comment")
    Observable<BaseListResponse<Comment>> list(@Path("id") long id, @QueryMap Map<String, String> map);

    //我的评论列表
    @GET("/user/{user_id}/comment")
    Observable<BaseListResponse<Comment>> listMy(@Path("user_id") long user_id, @QueryMap Map<String, String> map);

    //发表评论,content
    @FormUrlEncoded
    @POST("/user/{user_id}/joke/{id}/comment")
    Call<Void> create(@Path("user_id") long user_id, @Path("id") long id, @FieldMap Map<String, String> map);
}
