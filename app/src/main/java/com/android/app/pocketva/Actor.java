package com.android.app.pocketva;

/** This will be the Model that saves the Actor information and will implement Comparable to allow
 *  sorting within a list.
 */
public class Actor implements Comparable<Actor> {

    private String mName;
    private boolean mFavorite;
    private String mInfo;
    private String mImageUrl;
    private int mActorId;

    public Actor(String name, boolean favorite, String info,
                 String imageUrl, int actorId) {
        this.mName = name;
        this.mFavorite = favorite;
        this.mInfo = info;
        this.mImageUrl = imageUrl;
        this.mActorId = actorId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.mFavorite = favorite;
    }

    public int getmActorId() {
        return mActorId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmInfo() {
        return mInfo;
    }

    public int compareTo(Actor actor) {
        return this.getName().compareTo(actor.getName());
    }


}
