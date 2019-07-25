package com.example.abcrentals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with List.
 */

//Listview class segment. Code to assign values to a single listview content
public class ListContentFragment extends Fragment {
    public static FirebaseFirestore firebaseFirestore;
    DocumentReference mref;
    public static RecyclerView recyclerView;
    public static ArrayList<InterestsClass> interests;
    public static ContentAdapter adapter;

    @Override

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(


                R.layout.recycler_view, container, false);


        interests = new ArrayList<>();

        adapter = new ContentAdapter(recyclerView.getContext(), interests);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseFirestore = firebaseFirestore.getInstance();
        firebaseFirestore.collection("Interests").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listObj = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : listObj) {
                        InterestsClass i = snapshot.toObject(InterestsClass.class);
                        interests.add(i);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));


            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InterestsClass intObj = interests.get(getAdapterPosition());
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("interest", intObj);
                    context.startActivity(intent);
                }


            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.


        private Context context;
        private List<InterestsClass> interests;

        public ContentAdapter(Context context, List<InterestsClass> interests) {
            this.context = context;
            this.interests = interests;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            InterestsClass interest = interests.get(position);

            Picasso.get().load(interest.getImageUri()).into(holder.avator);

            holder.name.setText(interest.getName());
            holder.description.setText(interest.getDescription());
        }

        @Override
        public int getItemCount() {
            return interests.size();
        }
    }
}