package fr.android.androidexercises;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
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
    private BookListFragment bookListFragment;
    private BookDetailFragment bookDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("Create activity");
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        if(isInPortrait()) {
            setContentView(R.layout.portrait_template);
        } else {
            setContentView(R.layout.landscape_template);
        }
        bookListFragment = new BookListFragment();
        bookDetailFragment = new BookDetailFragment();
        //bookDetailFragment.setBook(currentBook);
        /*if(isInPortrait()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, bookListFragment, BookListFragment.class.getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bookListFrame, bookListFragment, BookListFragment.class.getSimpleName())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bookDetailFrame, bookDetailFragment, BookListFragment.class.getSimpleName())
                    .commit();
        }*/


        if(savedInstanceState == null) {
            if(isInPortrait()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, bookListFragment, BookListFragment.class.getSimpleName())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bookListFrame, bookListFragment, BookListFragment.class.getSimpleName())
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bookDetailFrame, bookDetailFragment, BookListFragment.class.getSimpleName())
                        .commit();
            }
        } else {
            if(isInPortrait()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, bookListFragment, BookListFragment.class.getSimpleName())
                        .commit();
            } else {
                currentBook = (Book) savedInstanceState.getParcelable("book");
                Timber.i(currentBook.getTitle());
                bookDetailFragment.setBook(currentBook);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bookListFrame, bookListFragment, BookListFragment.class.getSimpleName())
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bookDetailFrame, bookDetailFragment, BookListFragment.class.getSimpleName())
                        .commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Timber.i("Save activity");
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("book", currentBook);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Timber.i("Restore activity");
        super.onRestoreInstanceState(savedInstanceState);
        currentBook = (Book) savedInstanceState.getParcelable("book");
    }

    @Override
    public void onClickBook(Book book) {
        currentBook = book;
        //bookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.setBook(book);
        /*if(isInPortrait()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, bookDetailFragment, BookDetailFragment.class.getSimpleName())
                    .addToBackStack(BookDetailFragment.class.getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bookDetailFrame, bookDetailFragment, BookDetailFragment.class.getSimpleName())
                    .commit();
        }*/
    }

    private boolean isInPortrait(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
