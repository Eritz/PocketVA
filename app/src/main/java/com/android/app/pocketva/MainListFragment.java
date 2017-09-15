package com.android.app.pocketva;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainListFragment extends Fragment implements MainListAdapter.CallbackInterface {

    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_FAVORITE = "Favorite";
    public static final String EXTRA_POSITION = "Position";
    public static final String EXTRA_ACTOR_ID = "ActorID";
    public static final String EXTRA_ACTOR_IMAGE = "ActorImage";
    public static final String EXTRA_ACTOR_INFO = "ActorInfo";

    private static final int REQUEST_PICKED_ITEM = 1;

    private ArrayList<Actor> currentActorList;
    private MainListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar progressBar;


    public MainListFragment() {
        ActorList actorListInstance = ActorList.getActorListInstance();
        this.currentActorList = actorListInstance.getActorSavedList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /** Set up the view for the fragment. Set the recycler view with a swipe left/right to
     *  delete items.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        progressBar = v.findViewById(R.id.main_list_progressBar);

        recyclerView = v.findViewById(R.id.list_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MainListAdapter(currentActorList, getActivity(), this);
        recyclerView.setAdapter(adapter);
        emptyView = v.findViewById(android.R.id.empty);

        checkIfEmpty();

        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (!currentActorList.isEmpty()) {
                    adapter.removeItem(position);
                }
                if (currentActorList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return v;
    }

    /** The MainListFragment has an interface called CallbackInterface.
     *  This is the overridden method that starts an intent to display the Actor's
     *  information. To make the loading quicker, use information already stored about
     *  the Actor and send it to the ActorActivity.
     *  startActivityForResult is to check if the favorite's setting changed.
     * @param position - the data set's position that was clicked
     * @param actorClicked - the data set's position's actor that was clicked
     */
    @Override
    public void onHandleSelection(int position, Actor actorClicked) {

        Intent i = new Intent(getActivity(), ActorActivity.class);
        i.putExtra(EXTRA_NAME, actorClicked.getName());
        i.putExtra(EXTRA_FAVORITE, actorClicked.isFavorite());
        i.putExtra(EXTRA_POSITION, position);
        i.putExtra(EXTRA_ACTOR_ID, actorClicked.getmActorId());
        i.putExtra(EXTRA_ACTOR_IMAGE, actorClicked.getmImageUrl());
        i.putExtra(EXTRA_ACTOR_INFO, actorClicked.getmInfo());
        startActivityForResult(i, REQUEST_PICKED_ITEM);

    }


    /** In the overflow menu, there will be a delete all option,
     *  a sort by ascending order option, and a sort by descending order option.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Up navigation
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.menu_delete_all_action:
                currentActorList.clear();
                adapter.notifyDataSetChanged();
                emptyView.setVisibility(View.VISIBLE);
                return true;
            case R.id.menu_sort_ascend:
                Collections.sort(currentActorList);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.menu_sort_descend:
                Collections.reverse(currentActorList);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Check when resuming, the status of the list after a search or during creation.
     *  Set any changes on the adapter.
     */
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    /** Since the MainActivity was not receiving the changes on the adapter's current list,
     *  Will override the onPause() to save the adapter's list as the newest list for the
     *  MainActivity to save and recall later on.
     */
    @Override
    public void onPause() {
        super.onPause();
        ActorList.getActorListInstance().restoreSavedList(currentActorList);
    }

    /** Get data back from ActorActivity.
     *  If the item is set as a favorite, then get the information here to update the data set.
     *  If the item is set as a favorite, and then removed. The try/catch will handle any out of
     *  bounds exception, and will ensure that the previous item in the list does not receive the
     *  passed down favorite setting.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        int itemPressed = data.getIntExtra(ActorActivity.PASSED_POSITION,-1);
        int lastItemId = data.getIntExtra(ActorActivity.PASSED_ID,-1);

        try {
            if (currentActorList.get(itemPressed).getmActorId() == lastItemId) {
                boolean favoriteChange = data.getBooleanExtra(ActorActivity.EXTRA_CHANGED_FAVORITE, false);
                Actor actorClicked = currentActorList.get(itemPressed);
                actorClicked.setFavorite(favoriteChange);
            }
        } catch (IndexOutOfBoundsException ex) {}
        adapter.notifyDataSetChanged();
    }

    /** At the start and onResume(), check the status of the list.
     *  If it's empty, set the emptyView to display. Else, set the recycler view
     *  to be visible and populate the list.
     */
    public void checkIfEmpty() {
        if (!currentActorList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


}
