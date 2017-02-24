package fr.android.xhuberdeau;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
    private ArrayList<Book> books;
    private static final String BOOK_LIST   = "bookList";
    private static final String BOOK_DETAIL   = "bookDetail";
    public static final String BOOK_KEY = "book";
    public static final String BOOKS_KEY = "books";
    private static final String BOOKS_URL = "http://henri-potier.xebia.fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_library);
        if(savedInstanceState != null) {
            currentBook = (Book) savedInstanceState.getParcelable(BOOK_KEY);
            books = savedInstanceState.getParcelableArrayList(BOOKS_KEY);
        }

        loadBooks(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BOOK_KEY, currentBook);
        outState.putParcelableArrayList(BOOKS_KEY, books);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentBook = (Book) savedInstanceState.getParcelable(BOOK_KEY);
        books = savedInstanceState.getParcelableArrayList(BOOKS_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Save the selected book when user click on a book in the list of books and update detail book view
     * @param book
     */
    @Override
    public void onClickBook(Book book) {
        currentBook = book;
        loadDetailView();
    }

    /**
     * Load the list fragment
     */
    private void loadListView() {
        BookListFragment bookListFragment = new BookListFragment();
        Bundle bundle = new Bundle();
        // add books as a bundle so that they can be restored
        bundle.putParcelableArrayList(BOOKS_KEY, books);
        bookListFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        // delete previous stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .replace(R.id.mainFrame, bookListFragment, BOOK_LIST)
                .commit();
    }

    /**
     * Load the detail fragment
     */
    private void loadDetailView(){
        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        Bundle bundle = new Bundle();
        // add current book as a bundle so that it can be restored
        bundle.putParcelable(BOOK_KEY, currentBook);
        bookDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isInPortrait()) {
            fragmentTransaction
                    .replace(R.id.mainFrame, bookDetailFragment, BOOK_DETAIL)
                    .addToBackStack(BOOK_LIST);
        } else {
            fragmentTransaction
                    .replace(R.id.secondaryFrame, bookDetailFragment, BOOK_DETAIL);
        }
        fragmentTransaction.commit();
    }

    /**
     * Check if phone is in portrait orientation
     * @return boolean
     */
    private boolean isInPortrait(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Load books, whether from instance state or with retrofit
     * @param savedInstanceState
     */
    private void loadBooks(final Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(BOOKS_KEY);
            loadTemplates();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BOOKS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            HenriPotierService service = retrofit.create(HenriPotierService.class);
            Call<List<Book>> booksCall = service.listBooks();
            booksCall.enqueue(new Callback<List<Book>>(){
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    Timber.i("Books loaded");
                    books = (ArrayList<Book>) response.body();
                    loadTemplates();
                }

                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {
                    Toast.makeText(LibraryActivity.this, "Error while loading books", Toast.LENGTH_SHORT).show();
                    Timber.i(t.getMessage());
                }
            });
        }
    }

    private void loadTemplates() {
        if(currentBook != null || !isInPortrait()) {
            // load detail view if we're in landscape or if we selected a book
            loadDetailView();
        }
        if(!isInPortrait() || currentBook == null) {
            // load list view if we're in landscape or if didn't select a book
            loadListView();
        }
    }
}
