package com.wishcan.www.vocabulazy.view.books;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Book;
import com.wishcan.www.vocabulazy.storage.Database;


/**
 * Created by swallow on 2015/6/25.
 */
public class BooksGridView extends GridView {

    private static final String TAG = BooksGridView.class.getSimpleName();

    private static final int DEFAULT_ACTIONBAR_TITLE_RES_ID = R.string.book_title;

    private static final int DEFAULT_NUM_COLUMNS = 2;

    private static final int DEFAULT_BOOKS_GRID_VERTICAL_SPACING_RES_ID = R.dimen.books_grid_vertical_spacing_dimension;

    private static final int CIRCLE_BOOK_LAYOUT_RES_ID = R.layout.circle_book_layout;

    private static final int CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_book_green_ripple;

    private static final int CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_book_yellow_ripple;

    private static final int CIRCLE_GREEN_DRAWABLE_RES_ID = R.drawable.circle_book_green_shadow;

    private static final int CIRCLE_YELLOW_DRAWABLE_RES_ID = R.drawable.circle_book_yellow_shadow;

    ImageAdapter mAdapter;

    ViewGroup.LayoutParams layoutParams;

    private int mBooksCount = 9;

    private Context mContext;

    private int mVerticalSpacing;

    private int mNumColumns;

    private String mActionBarTitle;

    private Database mDatabase;

    private ArrayList<Book> mBooks;

    private LinkedList<String> mBookNames;

    public BooksGridView(Context context) {
        this(context, null, 0);
    }

    public BooksGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooksGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mActionBarTitle = mContext.getString(DEFAULT_ACTIONBAR_TITLE_RES_ID);

        mVerticalSpacing = (int) getResources().getDimension(DEFAULT_BOOKS_GRID_VERTICAL_SPACING_RES_ID);

        mNumColumns = DEFAULT_NUM_COLUMNS;

        layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setBackgroundColor(Color.WHITE);

        setNumColumns(mNumColumns);
        setGravity(Gravity.CENTER);
        setVerticalSpacing(mVerticalSpacing);
        setHorizontalSpacing(0);

        setSmoothScrollbarEnabled(true);
        setScrollbarFadingEnabled(true);

        mDatabase = ((MainActivity) context).getDatabase();


        refreshBooksView();


    }

    private void loadBooks() {
        mBooks = new ArrayList<>();
        mBooks = mDatabase.getBooks();
        mBooksCount = mBooks.size();
    }

    private void loadBookNames() {
        mBookNames = new LinkedList<>();
        for (int index = 0; index < mBooks.size(); index++) {
            Book book = mBooks.get(index);
            mBookNames.add(book.getName());
        }
    }

    public void refreshDatabase() {
        mDatabase = ((MainActivity) mContext).getDatabase();
    }

    public void refreshBooksView() {
        loadBooks();
        loadBookNames();

        refreshBooksView(mBooksCount, mBookNames);
    }

    public void refreshBooksView(int count){
        refreshBooksView(count, null);
    }

    public void refreshBooksView(int count, LinkedList<String> booksTitleLL){

        mAdapter = new ImageAdapter(mContext, booksTitleLL);

        mBooksCount = count;

        mAdapter.setCount(count + 1);

        setAdapter(mAdapter);

//        setActionBarTitle();

    }

    public int getBooksCount(){
        return mBooksCount;
    }

    public void setActionBarTitle(){

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    ((MainActivity) mContext).switchActionBarTitle(mActionBarTitle);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private int mCount;
        private LinkedList<String> mImgStrLL;

        public ImageAdapter(Context c, LinkedList<String> imageStrLL) {
            mContext = c;
            mImgStrLL = imageStrLL;
        }

        public int getCount() {
            return mCount;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public void setCount(int count){
            mCount = count;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup bookParentView = (ViewGroup) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(CIRCLE_BOOK_LAYOUT_RES_ID, null);
            bookParentView.setPadding(5,5,5,5);

            ImageView imageView = (ImageView) bookParentView.getChildAt(0);
            TextView textView = (TextView) bookParentView.getChildAt(1);
            RelativeLayout crossViewParent = (RelativeLayout) bookParentView.getChildAt(2);

            if(position % 4 < 2) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageResource(CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID);
                else
                    imageView.setImageResource(CIRCLE_GREEN_DRAWABLE_RES_ID);
                if(position < 2) {
//                    bookParentView.setPadding(5, mVerticalSpacing, 5, 5);
                    imageView.setPadding(0, mVerticalSpacing, 0, 0);
                    textView.setPadding(0, mVerticalSpacing, 0, 0);
                    crossViewParent.setPadding(0, mVerticalSpacing, 0, 0);
                }

            }
            else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    imageView.setImageResource(CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID);
                else
                    imageView.setImageResource(CIRCLE_YELLOW_DRAWABLE_RES_ID);
            }

            if(mImgStrLL == null) {
                textView.setText("7000字\nLevel " + (position + 1));
                return bookParentView;
            }

            if(position < mImgStrLL.size())
                textView.setText(mImgStrLL.get(position));
            else{
                textView.setVisibility(GONE);
                crossViewParent.setVisibility(VISIBLE);
            }

            return bookParentView;
        }
    }


}
