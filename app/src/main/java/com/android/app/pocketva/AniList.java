package com.android.app.pocketva;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Uses the AniList API to fetch staff names.
 *  In particular, it will use Grant: Client Credentials.
 *  Example of output: https://anilist.co/api/staff/95185/page?access_token=q6bXYJXXkU5FH4p5voS7Kql9XYghLmcpvNXTorNF
 */

public interface AniList {

    String GRANT_TYPE = "client_credentials";
    String CLIENT_ID = "your-client-id-here";
    String CLIENT_SECRET = "your-client-secret-here";

    /** Get an AccessToken key from the POST 'auth/access_token'.
     *  The Client ID and the Client Secret are obtained from the Dev website.
     *  Token will expire in 1 hour.
     */
    @POST("auth/access_token")
    Call<SearchAccess> getAccessToken(
            @Query("grant_type") String clientCred,
            @Query("client_id") String clientID,
            @Query("client_secret") String clientSecret
    );

    /** Gets all actors that match the query from the GET 'actor/search/{query}'.
     *  There is a bug when using the access token as a header. Set as a parameter instead.
     *  Substitutes the {query} with the variable set in the API endpoint.
     *  The return will be "small staff models"
     */
    @GET("staff/search/{query}")
    Call<List<SearchItem>> getActorList(
            @Path("query") String search,
            @Query("access_token") String accessToken
    );

    /** Uses the Actor ID obtained from the getActorList method to invoke the
     *  GET 'staff/{id}/page'. There is a bug when using the access token as a header. Set as a
     *  parameter instead. Substitute the {id} with the actor's id. The above GET, does not
     *  display roles, so have to use it to obtain the ID.
     *  The information of interest is the 'anime' tag, NOT the 'anime_staff' tag
     */
    @GET("staff/{id}/page")
    Call<SearchItem> getActorRoles(
            @Path("id") int actorID,
            @Query("access_token") String accessToken
    );



}
