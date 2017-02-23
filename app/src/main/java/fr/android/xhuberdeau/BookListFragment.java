package fr.android.xhuberdeau;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BookListFragment extends Fragment {
    private RecyclerView listView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.book_list, container, false);
        listView = (RecyclerView) view.findViewById(R.id.recyclerView);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        List<Book> books = getArguments().getParcelableArrayList(LibraryActivity.BOOKS_KEY);
        listView.setAdapter(new BookRecyclerAdapter(LayoutInflater.from(view.getContext()), books));
        return view;
    }
}