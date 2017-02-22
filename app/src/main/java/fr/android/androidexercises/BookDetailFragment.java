package fr.android.androidexercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import timber.log.Timber;

public class BookDetailFragment extends Fragment {
    private ImageView imageView;
    private TextView titleView;
    private TextView priceView;
    private TextView synopsisView;
    private View view;
    private Book book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.book_detail, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        titleView = (TextView) view.findViewById(R.id.nameTextView);
        priceView = (TextView) view.findViewById(R.id.priceTextView);
        synopsisView = (TextView) view.findViewById(R.id.synopsisTextView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(book != null) {
            titleView.setText(book.getTitle());
            priceView.setText(String.valueOf(book.getPrice()));
            String synopsis = "";
            for(String s : book.getSynopsis()) {
                synopsis += s;
            }
            synopsisView.setText(synopsis);
            Glide.with(imageView.getContext()).load(book.getCover()).into(imageView);
        }
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
