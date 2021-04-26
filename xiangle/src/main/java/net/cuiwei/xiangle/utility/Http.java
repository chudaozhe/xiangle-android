package net.cuiwei.xiangle.utility;

import android.content.Context;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import net.cuiwei.xiangle.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.cuiwei.xiangle.model.TokenModel;
import okhttp3.*;
import okhttp3.internal.platform.Platform;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {
    private static Context mContext;
    private Retrofit retrofit;
    public static String baseUrl = "https://xiangle.cuiwei.net/";

    private Http(Context context) {
        retrofit=new Retrofit.Builder()
                .client(getClient(context).build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        Http h=new Http(context);
        return h.retrofit;
    }

    private OkHttpClient.Builder getClient(Context context){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        TokenModel cache=new TokenModel(mContext);

        Interceptor headerInterceptor= new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("token", cache.getToken())
                        .build();
                return chain.proceed(request);
            }
        };

        //add log record
        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("请求")
                    .response("响应")
                    .build();
            httpClientBuilder.addInterceptor(headerInterceptor);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }
        return httpClientBuilder;
    }
}
