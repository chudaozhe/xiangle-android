package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.bean.BaseListResponse;
import net.cuiwei.xiangle.bean.Category;
import net.cuiwei.xiangle.bean.Topic;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.ArrayList;
import java.util.Map;

public interface TopicService {
    //话题详情
    @GET("/user/{user_id}/topic/{topic_id}")
    Observable<BaseDetailResponse<Topic>> get(@Path("user_id") long user_id, @Path("topic_id") long topic_id);

    //分类列表
    @GET("/user/category")
    Call<BaseDetailResponse<ArrayList<Category>>> listByCategory(@QueryMap Map<String, String> map);

    //话题列表
    @GET("/user/{user_id}/category/{category_id}/topic")
    Observable<BaseListResponse<Topic>> list(@Path("user_id") long user_id, @Path("category_id") long category_id, @QueryMap Map<String, String> map);
}
