package org.ctdworld.propath.database;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import org.ctdworld.propath.model.NewsFeedDeleteResponse;
import org.ctdworld.propath.model.Response;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitClient
{

    @FormUrlEncoded
    @POST("/athlete/jsondata.php")
    Call<NewsFeedDeleteResponse>  deleteNewsFeed(
            @Field("delete_news_feed") String delete_news_feed,
            @Field("delete_news_feed_id") String delete_news_feed_id
    );
}
