package com.android.app.pocketva;



import android.content.Context;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Route;
/** The authenticator for the OKHTTP client. */
public class CustomAuthenticator implements okhttp3.Authenticator {

    private Context authContext;

    public CustomAuthenticator(Context context) {
        authContext = context;
    }

    @Override
    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
        TokenHandler tokenHandlerInstance = TokenHandler.getInstance();

        if (response.request().header("Authorization") != null) {
            return null;
        }

        tokenHandlerInstance.getToken(authContext);
        return response.request().newBuilder()
                .header("Authorization", tokenHandlerInstance.getTokenString())
                .build();
    }

}
