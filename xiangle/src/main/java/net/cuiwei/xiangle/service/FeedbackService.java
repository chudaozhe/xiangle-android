package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import retrofit2.http.*;

import java.util.HashMap;

public interface FeedbackService {
    //意见反馈
    @FormUrlEncoded
    @POST("/user/{user_id}/feedback")
    Observable<BaseDetailResponse<HashMap<String, Object>>> create(@Path("user_id") long user_id, @Field("content") String content, @Field("contact") String contact);
}
