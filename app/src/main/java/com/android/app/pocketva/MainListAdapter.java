package com.android.app.pocketva;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private ArrayList<Actor> mListOfActor;
    private Context mContext;
    private CallbackInterface mCallback;

    public interface CallbackInterface  {
        void onHandleSelection(int position, Actor passed);
    }

    public MainListAdapter(ArrayList<Actor> listOfActor, Context context, CallbackInterface c) {
        this.mListOfActor = listOfActor;
        this.mContext = context;
        this.mCallback = c;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView listFace;
        TextView listName;
        CheckBox listCheckBox;

        public ViewHolder(View view) {
            super(view);

            listFace = view.findViewById(R.id.actor_face);
            listName = view.findViewById(R.id.actor_name);
            listCheckBox = view.findViewById(R.id.actor_favorite);

            view.setOnClickListener(this);

        }

        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                    mCallback.onHandleSelection(position, mListOfActor.get(position));
            }
        }
    }

    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.fragment_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(MainListAdapter.ViewHolder viewHolder, final int i) {
        String url = mListOfActor.get(i).getmImageUrl();
        Picasso.with(mContext).load(url).into(viewHolder.listFace);

        viewHolder.listName.setText(mListOfActor.get(i).getName());

        viewHolder.listCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mListOfActor.get(i).setFavorite(isChecked);
            }
        });
        viewHolder.listCheckBox.setChecked(mListOfActor.get(i).isFavorite());
    }

    @Override
    public int getItemCount() {
        return mListOfActor.size();
    }

    public void removeItem(int position) {
        mListOfActor.remove(position);
        mListOfActor.trimToSize();
        notifyDataSetChanged();
    }


}
