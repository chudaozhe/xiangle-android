package net.cuiwei.xiangle.service;

import io.reactivex.rxjava3.core.Observable;
import net.cuiwei.xiangle.bean.BaseDetailResponse;
import net.cuiwei.xiangle.bean.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface UserService {
    //更新用户信息 gender, birthday, province, city
    @FormUrlEncoded
    @PUT("/user/{user_id}")
    Call<Void> update(@Path("user_id") long user_id, @FieldMap Map<String, String> map);

    //用户详情
    @GET("/user/{user_id}")
    Observable<BaseDetailResponse<User>> get(@Path("user_id") long user_id);

    /**
     * @deprecated
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<BaseDetailResponse<User>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/login/mobile")
    Observable<BaseDetailResponse<User>> mobileLogin(@Field("mobile") String mobile, @FieldMap Map<String, String> map);

    /**
     * @deprecated
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/user/register")
    Observable<BaseDetailResponse<User>> register(@Field("username") String username, @Field("password") String password);

    //发送验证码
    @POST("/user/mobile/{mobile}/captcha")
    Call<BaseDetailResponse<String>> sendCaptcha(@Path("mobile") String mobile);
}
