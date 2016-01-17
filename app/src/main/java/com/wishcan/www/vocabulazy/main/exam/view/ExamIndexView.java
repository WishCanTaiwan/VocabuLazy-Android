package com.wishcan.www.vocabulazy.main.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2016/1/13.
 */
public class ExamIndexView extends FrameLayout {

    public interface OnExamItemClickListener{
        void onExamUnitBookClick();
        void onExamUnitNoteClick();
    }

    private static final int VIEW_RES_ID = R.layout.view_exam_index;
    private static final int VIEW_UNIT_BOOK_RES_ID = R.id.exam_unit_book;
    private static final int VIEW_UNIT_NOTE_RES_ID = R.id.exam_unit_note;

    private OnExamItemClickListener mOnExamItemClickListener;

    public ExamIndexView(Context context) {
        super(context);

        ViewGroup childView;
        childView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        childView.findViewById(VIEW_UNIT_BOOK_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnExamItemClickListener != null)
                    mOnExamItemClickListener.onExamUnitBookClick();
            }
        });
        childView.findViewById(VIEW_UNIT_NOTE_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnExamItemClickListener != null)
                    mOnExamItemClickListener.onExamUnitNoteClick();
            }
        });
        addView(childView);
    }

    public void setOnExamItemClickListener(OnExamItemClickListener listener){
        mOnExamItemClickListener = listener;
    }
}
