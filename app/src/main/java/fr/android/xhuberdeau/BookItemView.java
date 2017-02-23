package fr.android.xhuberdeau;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookItemView extends LinearLayout {

    private TextView nameView;
    private TextView priceView;
    private ImageView imageView;
    public BookItemView(Context context) {
        this(context, null);
    }

    public BookItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameView = (TextView) findViewById(R.id.nameTextView);
        priceView = (TextView) findViewById(R.id.priceTextView);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void bindView(Book book) {
        nameView.setText(book.getTitle());
        priceView.setText(String.valueOf(book.getPrice()));
        Glide.with(imageView.getContext()).load(book.getCover()).into(imageView);
    }
}
