package com.example.abcrentals;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with Cards.
 */
public class CardContentFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    public static ArrayList<Searches> searches ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        searches = new ArrayList<>();

       final ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(),searches);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));

        firebaseFirestore = firebaseFirestore.getInstance();
        firebaseFirestore.collection("Searches").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    List<DocumentSnapshot> listObj = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot snapshot : listObj)
                    {
                        Searches s = snapshot.toObject(Searches.class);
                        searches.add(s);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_card, parent, false));


            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Searches searchObj = searches.get(getAdapterPosition());
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("search",searchObj);
                    context.startActivity(intent);

                }
            });

            // Adding Snackbar to Action Button inside card

        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private Context context;
        private List<Searches> searches;

        public ContentAdapter(Context context,List<Searches> searches) {
            this.context = context;
            this.searches = searches;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Searches search = searches.get(position);

            Picasso.get().load(search.getImage()).into(holder.picture);

            holder.name.setText(search.getName());
            holder.description.setText(search.getDescription());

        }

        @Override
        public int getItemCount() {
            return searches.size();
        }
    }
}