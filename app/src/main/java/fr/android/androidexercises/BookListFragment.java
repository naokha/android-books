package fr.android.androidexercises;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class BookListFragment extends Fragment {

    private static final String step0 = "This is step 0";

    private RecyclerView listView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.book_list, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://henri-potier.xebia.fr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HenriPotierService service = retrofit.create(HenriPotierService.class);
        Call<List<Book>> books = service.listBooks();
        books.enqueue(new Callback<List<Book>>(){

            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                listView = (RecyclerView) view.findViewById(R.id.recyclerView);
                listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                listView.setAdapter(new BookRecyclerAdapter(LayoutInflater.from(view.getContext()), response.body()));
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Timber.i(t.getMessage());
            }
        });
        return view;
    }
}