package org.ctdworld.propath.database;

import java.time.Duration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper
{
    private static Retrofit mRetrofit;

    private RetrofitHelper() { }

    public static Retrofit getRetrofit()
    {
        if (mRetrofit == null)
        {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://ctdworld.co/")
                    .addConverterFactory(GsonConverterFactory.create());

            mRetrofit = builder.build();
        }

        return mRetrofit;
    }


    // returns
    public static RetrofitClient getClient()
    {
        if (mRetrofit == null)
            getRetrofit();

        return mRetrofit.create(RetrofitClient.class);
    }
}
