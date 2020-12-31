package com.magicmusic.playbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.internal.ContextUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//
//import static com.magicmusic.playbooks.R.id.image;
//import static com.magicmusic.playbooks.R.id.thumbnail;

public class PlayBooksAdapter extends ArrayAdapter<PlayBooks> {

    static ImageView imageView;

    public PlayBooksAdapter(@NonNull Context context, ArrayList<PlayBooks> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookView = convertView;
        if(bookView == null){
            bookView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }

        PlayBooks books = getItem(position);
        TextView name = bookView.findViewById(R.id.name_of_book);
        name.setText(books.getName());
        TextView bio = bookView.findViewById(R.id.author);
        bio.setText(books.getBio());
        TextView language = (TextView)bookView.findViewById(R.id.language);
        language.setText(books.getLanguage());
        TextView date = bookView.findViewById(R.id.ratings);
        date.setText( books.getDate());
//        imageView = bookView.findViewById(thumbnail);


        return bookView;
    }
}
