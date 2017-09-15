package com.android.app.pocketva;



import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Used in the CustomAuthenticator to generate a token if necessary*/
public class TokenHandler {

    private static TokenHandler tokenHandler = null;
    private SearchAccess searchAccess = SearchAccess.getInstance();

    private TokenHandler() {}

    public static TokenHandler getInstance() {
        if (tokenHandler == null) {
            tokenHandler = new TokenHandler();
            return tokenHandler;
        }
        return tokenHandler;
    }

    public void getToken(Context context) {

        AniList apiService = RetrofitClient.getInstance(context).create(AniList.class);
        Call<SearchAccess> call = apiService.getAccessToken(AniList.GRANT_TYPE,
                AniList.CLIENT_ID, AniList.CLIENT_SECRET);
        call.enqueue(new Callback<SearchAccess>() {
            @Override
            public void onResponse(Call<SearchAccess> call, Response<SearchAccess> response) {
                String token = response.body().getToken();
                searchAccess.setToken(token);
            }

            @Override
            public void onFailure(Call<SearchAccess> call, Throwable t) {
            }
        });
    }



    public String getTokenString() {
        return searchAccess.getToken();
    }

}
