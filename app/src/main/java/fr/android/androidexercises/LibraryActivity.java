package fr.android.androidexercises;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class LibraryActivity extends AppCompatActivity implements BookRecyclerAdapter.OnClickBookListener {
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        loadBooks(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("book", currentBook);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentBook = (Book) savedInstanceState.getParcelable("book");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClickBook(Book book) {
        currentBook = book;
        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.setBook(currentBook);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(isInPortrait()) {
            fragmentTransaction
                    .replace(R.id.mainFrame, bookDetailFragment, BookDetailFragment.class.getSimpleName())
                    .addToBackStack(BookDetailFragment.class.getSimpleName());
        } else {
            fragmentTransaction
                    .replace(R.id.secondaryFrame, bookDetailFragment, BookDetailFragment.class.getSimpleName());
        }
        fragmentTransaction.commit();
    }

    private boolean isInPortrait(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void loadBooks(final Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://henri-potier.xebia.fr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HenriPotierService service = retrofit.create(HenriPotierService.class);
        Call<List<Book>> books = service.listBooks();
        books.enqueue(new Callback<List<Book>>(){

            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                Timber.i("Books loaded");
                loadTemplates(response.body(), savedInstanceState);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Timber.i(t.getMessage());
            }
        });
    }

    private void loadTemplates(List<Book> books, Bundle savedInstanceState) {
        BookListFragment bookListFragment = new BookListFragment();
        bookListFragment.setBooks(books);
        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(savedInstanceState != null) {
            currentBook = (Book) savedInstanceState.getParcelable("book");
            bookDetailFragment.setBook(currentBook);
        }
        setContentView(R.layout.activity_library);
        if(isInPortrait()) {
            fragmentTransaction
                    .replace(R.id.mainFrame, bookListFragment, BookListFragment.class.getSimpleName())
                    .commit();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if(currentBook != null) {
                fragmentTransaction
                        .replace(R.id.mainFrame, bookDetailFragment, BookDetailFragment.class.getSimpleName())
                        .addToBackStack(BookDetailFragment.class.getSimpleName());
            }
        } else {
            fragmentTransaction
                    .replace(R.id.mainFrame, bookListFragment, BookListFragment.class.getSimpleName())
                    .replace(R.id.secondaryFrame, bookDetailFragment, BookListFragment.class.getSimpleName());
        }
        fragmentTransaction.commit();
    }
}
