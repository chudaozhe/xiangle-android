package net.cuiwei.xiangle.service;

import net.cuiwei.xiangle.bean.Division;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface DivisionService {
    //行政区规划
    @GET("/user/division")
    Call<ArrayList<Division>> list();
}
