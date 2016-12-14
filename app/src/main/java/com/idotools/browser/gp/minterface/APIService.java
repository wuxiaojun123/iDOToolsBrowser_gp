package com.idotools.browser.gp.minterface;

import com.idotools.browser.gp.bean.BannerResp;
import com.idotools.browser.gp.bean.DmzjBean;
import com.idotools.browser.gp.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wuxiaojun on 16-11-9.
 */
public interface APIService {

    //请求banner页的数据
    @GET(Constant.PATH_BANNER)
    Call<BannerResp> getBannerBeanList(
            @Query("packageName") String packageName,
            @Query("wzCode") String wzCode
    );


    //请求动漫之家的数据
    @GET(Constant.PATH_WORDPRESS_BASE)
    Call<DmzjBean> getDmzjBeanList(
            @Query("json") String json,
            @Query("slug") String slug,
            @Query("page") int page,
            @Query("count") int count
    );

}
