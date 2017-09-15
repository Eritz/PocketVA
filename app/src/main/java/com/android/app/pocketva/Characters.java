package com.android.app.pocketva;


import com.google.gson.annotations.SerializedName;

/** This will be the model that saves the Character information from the Shows. This is because
 *  the Characters in the JSON is a nested object within Shows.
 */
public class Characters {

    @SerializedName("id")
    private int id;
    @SerializedName("name_first")
    private String nameFirst;
    @SerializedName("name_last")
    private String nameLast;
    @SerializedName("image_url_lge")
    private String imageUrlLge;
    @SerializedName("image_url_med")
    private String imageUrlMed;
    @SerializedName("role")
    private String role;
    @SerializedName("link_id")
    private int linkId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getImageUrlLge() {
        return imageUrlLge;
    }

    public void setImageUrlLge(String imageUrlLge) {
        this.imageUrlLge = imageUrlLge;
    }

    public String getImageUrlMed() {
        return imageUrlMed;
    }

    public void setImageUrlMed(String imageUrlMed) {
        this.imageUrlMed = imageUrlMed;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public String getCombinedName() {
        if (getNameLast() != null && getNameFirst() != null) {
            return getNameLast() + " " + getNameFirst();
        } else if (getNameLast() == null) {
            return getNameFirst();
        }
        return getNameFirst();

    }

}
