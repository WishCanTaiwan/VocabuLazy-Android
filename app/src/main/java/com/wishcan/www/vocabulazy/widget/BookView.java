package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wishcan.www.vocabulazy.widget.AdapterView;
import com.wishcan.www.vocabulazy.R;

import java.util.LinkedList;


/**
 * Created by swallow on 2015/6/25.
 */
public class BookView extends GridView implements AdapterView<String>{

    @Override
    public void refreshView(int count, LinkedList<String> linkedList) {
        refreshBookView(count, linkedList);
    }

    public interface BookItemOnClickListener{
        void bookItemOnClick();
        void newItemOnClick();
    }

    private static final int DEFAULT_NUM_COLUMNS = 2;
    private static final int DEFAULT_NUM_BOOKS = 1;
    private static final int DEFAULT_BOOKS_GRID_VERTICAL_SPACING_RES_ID = R.dimen.books_grid_vertical_spacing_dimension;
    private static final int CIRCLE_BOOK_LAYOUT_RES_ID = R.layout.circle_book_layout;
    private static final int CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_book_green_ripple;
    private static final int CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_book_yellow_ripple;
    private static final int CIRCLE_GREEN_DRAWABLE_RES_ID = R.drawable.circle_book_green_shadow;
    private static final int CIRCLE_YELLOW_DRAWABLE_RES_ID = R.drawable.circle_book_yellow_shadow;

    ImageAdapter mAdapter;
    ViewGroup.LayoutParams layoutParams;
    private Context mContext;
    private int mVerticalSpacing;
    private int mBookCount;
    private LinkedList<String> mBookNames;
    private BookItemOnClickListener mBookItemOnClickListener;

    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mVerticalSpacing = (int) getResources().getDimension(DEFAULT_BOOKS_GRID_VERTICAL_SPACING_RES_ID);
        layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        setLayoutParams(layoutParams);
        setBackgroundColor(Color.WHITE);
        setNumColumns(DEFAULT_NUM_COLUMNS);
        setGravity(Gravity.CENTER);
        setVerticalSpacing(mVerticalSpacing);
        setHorizontalSpacing(0);
        setSmoothScrollbarEnabled(true);
        setScrollbarFadingEnabled(true);

        init();
        refreshBookView();
    }

    private void init(){
        mBookCount = DEFAULT_NUM_BOOKS;
        mBookNames = new LinkedList<>();
        for(int i=1; i <= mBookCount; i++)
            mBookNames.add("Book"+i);
    }



    private void refreshBookView() {
        refreshBookView(mBookCount, mBookNames);
    }

    private void refreshBookView(int count){
        refreshBookView(count, null);
    }

    private void refreshBookView(int count, LinkedList<String> booksTitleLL){
        mBookCount = count;
        mBookNames = booksTitleLL;
        mAdapter = new ImageAdapter(mContext, mBookNames);
        mAdapter.setCount(count + 1);
        setAdapter(mAdapter);
    }

    public int getBooksCount(){
        return mBookCount;
    }

    public void setBookItemOnClickListener(BookItemOnClickListener listener){
        mBookItemOnClickListener = listener;
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

            if(mImgStrLL == null)
                return bookParentView;

            if(position < mImgStrLL.size()) {
                textView.setText(mImgStrLL.get(position));
                bookParentView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBookItemOnClickListener != null)
                            mBookItemOnClickListener.bookItemOnClick();
                    }
                });
            }
            else{
                textView.setVisibility(GONE);
                crossViewParent.setVisibility(VISIBLE);
                bookParentView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mBookItemOnClickListener != null)
                            mBookItemOnClickListener.newItemOnClick();
                    }
                });
            }

            return bookParentView;
        }
    }


}
