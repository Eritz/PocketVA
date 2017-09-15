package com.android.app.pocketva;



import com.google.gson.annotations.SerializedName;

/** SearchAccess will be the Model for the current token in usage. The RetrofitClient will
 *  make a POST request, and store the results here.
 */
public class SearchAccess {

    private static SearchAccess searchAccess = null;

    @SerializedName("access_token")
    private String token;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires")
    private int expires;
    @SerializedName("expires_in")
    private int expiresIn;

    private SearchAccess() {}

    public static SearchAccess getInstance() {
        if (searchAccess == null) {
            searchAccess = new SearchAccess();
        }
        return searchAccess;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
