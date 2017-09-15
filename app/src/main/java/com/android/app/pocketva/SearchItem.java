package com.android.app.pocketva;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** This is the Model to store the search results for the actors in SearchActivity. It will
 *  implement the Comparable interface to automatically sort the results in ascending order.
 */
public class SearchItem implements Comparable<SearchItem>{

    @SerializedName("id")
    private int actorId;
    @SerializedName("name_first")
    private String actorFirstName;
    @SerializedName("name_last")
    private String actorLastName;
    @SerializedName("image_url_lge")
    private String urlImageLarge;
    @SerializedName("image_url_med")
    private String urlImageMed;
    @SerializedName("language")
    private String language;
    @SerializedName("info")
    private String info;
    @SerializedName("role")
    private List<String> roles;
    @SerializedName("anime")
    private List<Shows> showsList;

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getActorFirstName() {
        return actorFirstName;
    }

    public void setActorFirstName(String actorFirstName) {
        this.actorFirstName = actorFirstName;
    }

    public String getActorLastName() {
        return actorLastName;
    }

    public void setActorLastName(String actorLastName) {
        this.actorLastName = actorLastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUrlImageLarge() {
        return urlImageLarge;
    }

    public void setUrlImageLarge(String urlImageLarge) {
        this.urlImageLarge = urlImageLarge;
    }

    public String getUrlImageMed() {
        return urlImageMed;
    }

    public void setUrlImageMed(String urlImageMed) {
        this.urlImageMed = urlImageMed;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Shows> getShowsList() {
        return showsList;
    }

    public void setShowsList(List<Shows> showsList) {
        this.showsList = showsList;
    }

    public String getCombinedName() {
        return getActorFirstName() + " " + getActorLastName();
    }

    public int compareTo(SearchItem searchItem) {
        return this.getActorFirstName().compareTo(searchItem.getActorFirstName());
    }

}

