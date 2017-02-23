package fr.android.xhuberdeau;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDetailView extends LinearLayout{
    private TextView nameView;
    private TextView priceView;
    private TextView synopsisView;
    private ImageView imageView;

    public BookDetailView(Context context) {
        this(context, null);
    }

    public BookDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameView = (TextView) findViewById(R.id.nameTextView);
        priceView = (TextView) findViewById(R.id.priceTextView);
        imageView = (ImageView) findViewById(R.id.imageView);
        synopsisView = (TextView) findViewById(R.id.synopsisTextView);
    }

    public void bindView(Book book) {
        if(book != null) {
            nameView.setText(book.getTitle());
            priceView.setText(String.valueOf(book.getPrice()));
            String synopsis = "";
            for(String s : book.getSynopsis()) {
                synopsis += s;
            }
            synopsisView.setText(synopsis);
            Glide.with(imageView.getContext()).load(book.getCover()).into(imageView);
        }
    }
}
