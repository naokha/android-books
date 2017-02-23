package fr.android.xhuberdeau;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookDetailFragment extends Fragment {
    private BookDetailView view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (BookDetailView) inflater.inflate(R.layout.book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Book book = getArguments().getParcelable(LibraryActivity.BOOK_KEY);
        this.view.bindView(book);
    }
}
