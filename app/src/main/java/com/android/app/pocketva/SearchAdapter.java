package com.android.app.pocketva;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/** This is the RecyclerView adapter for SearchActivity's search results */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private List<SearchItem> searchItems;
    private Context context;

    public SearchAdapter(List<SearchItem> searchItems, Context context) {
        this.searchItems = searchItems;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView searchItemsImage;
        private TextView searchItemsText;

        private static final String EXTRA_ACTOR_FULLNAME = "Clicked Actor Full Name";
        private static final String EXTRA_ACTOR_ID = "Clicked Actor Id";
        private static final String EXTRA_ACTOR_IMAGE = "Clicked Actor Image";
        private static final String EXTRA_ACTOR_INFO = "Clicked Actor Info";

        public ViewHolder(View view) {
            super(view);

            searchItemsImage = view.findViewById(R.id.actor_face);
            searchItemsText = view.findViewById(R.id.actor_name);

            view.setOnClickListener(this);

        }

        /** Set the Intent inside here. Retrieve the actor ID, actor name, image url, and
         *  description to be sent to the SearchClickedActivity to quickly generate a display.
         *  Need to use the actor ID to force a new search for the shows and character information.
         */
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                int actorID = searchItems.get(position).getActorId();
                searchItems.get(position).setActorId(actorID);

                Intent i = new Intent(context, SearchClickedActivity.class);
                Bundle extras = new Bundle();
                extras.putInt(EXTRA_ACTOR_ID, searchItems.get(position).getActorId());
                extras.putString(EXTRA_ACTOR_FULLNAME, searchItems.get(position).getCombinedName());
                extras.putString(EXTRA_ACTOR_INFO, searchItems.get(position).getInfo());
                extras.putString(EXTRA_ACTOR_IMAGE, searchItems.get(position).getUrlImageMed());
                i.putExtras(extras);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }

    }

    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.search_screen_results, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {
        String url = searchItems.get(i).getUrlImageMed();
        Picasso.with(context).load(url).into(viewHolder.searchItemsImage);

        viewHolder.searchItemsText.setText(searchItems.get(i).getCombinedName());
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }
}
