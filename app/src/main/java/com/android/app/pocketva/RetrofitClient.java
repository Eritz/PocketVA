package com.android.app.pocketva;


import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** The RetrofitClient serves as a singleton, and will allow requests to be completed in the
 *  background thread. It will make use of the AniList interface to perform all REST actions.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    private RetrofitClient() {}

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {

            CustomAuthenticator customAuthenticator = new CustomAuthenticator(context);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .authenticator(customAuthenticator)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.base_url))
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
        return retrofit;
    }

}
