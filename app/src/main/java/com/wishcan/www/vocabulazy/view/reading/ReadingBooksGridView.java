package com.wishcan.www.vocabulazy.view.reading;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;

/**
 * Created by swallow on 2015/12/3.
 */
public class ReadingBooksGridView extends BooksGridView {

    private static final int DEFAULT_ACTIONBAR_TITLE_RES_ID = R.string.reading_book_title;

    private String mActionBarTitle;

    private Context mContext;

    public ReadingBooksGridView(Context context) {
        super(context);
        mContext = context;

        mActionBarTitle = mContext.getString(DEFAULT_ACTIONBAR_TITLE_RES_ID);
    }

    @Override
    public void setActionBarTitle() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true)
                    ((MainActivity) mContext).switchActionBarTitle(mActionBarTitle);
            }
        });
    }

    @Override
    public void setOnItemClickListener() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < getBooksCount())
                    ((MainActivity) getContext()).goReadingLessonFragment(position);

            }
        });
    }
}
