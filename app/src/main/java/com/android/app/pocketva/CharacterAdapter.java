package com.android.app.pocketva; 


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/** This is the RecyclerView Adapter for displaying retrieved characters. */
public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private List<Shows> showsList;
    private Context context;

    public CharacterAdapter(List<Shows> showsList, Context context) {
        this.showsList = showsList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView showCharacterImage;
        private TextView showCharacterName;
        private TextView showTitle;

        public ViewHolder(View view) {
            super(view);

            showCharacterImage = view.findViewById(R.id.show_character_image);
            showCharacterName = view.findViewById(R.id.show_character_name);
            showTitle = view.findViewById(R.id.show_title);

        }

    }

    public CharacterAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.character_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterAdapter.ViewHolder viewHolder, int i) {
        int sizeOfCharacterList = showsList.get(i).getCharacters().size();
        for (int charList=0; charList < sizeOfCharacterList; charList++) {
            String url = showsList.get(i).getCharacters().get(charList).getImageUrlMed();
            String role = showsList.get(i).getCharacters().get(charList).getRole();
            String charName = showsList.get(i).getCharacters().get(charList).getCombinedName();

            Picasso.with(context).load(url).into(viewHolder.showCharacterImage);
            viewHolder.showTitle.setText(showsList.get(i).getTitleRomaji());
            viewHolder.showCharacterName.setText(charName + "\n[" + role + "]");

        }
    }

    public int getItemCount() {return showsList.size();}
}
