package com.android.app.pocketva;

import java.util.ArrayList;

public class ActorList {

    private static ActorList actorList = null;
    private static ArrayList<Actor> savedList;

    private ActorList() {
        savedList = new ArrayList<Actor>();
    }


    public static ActorList getActorListInstance() {

        if (actorList == null) {
            actorList = new ActorList();
        }

        return actorList;
    }

    public ArrayList<Actor> getActorSavedList() {
        return savedList;
    }

    public void restoreSavedList(ArrayList<Actor> restoreList) {
        if (restoreList !=null) {
            savedList = restoreList;
        }
    }


}
