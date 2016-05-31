package com.wishcan.www.vocabulazy.utility;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by swallow on 2016/1/28.
 */
public class Utility {

    public static final String TAG = Utility.class.getSimpleName();

    public static ArrayList<View> findViewRecursive(ViewGroup viewGroup, Class targetClass) {
        int index = 0;
        int count;
        ArrayList<View> ansList = new ArrayList<>();

//        Logger.d(TAG, "findViewRecursive Searching");
        while((count = viewGroup.getChildCount()) > 0){
            View v = viewGroup.getChildAt(index);
            if(v instanceof ViewGroup) {
                ArrayList<View> returnList = findViewRecursive((ViewGroup) v, targetClass);
                for(View view : returnList)
                    ansList.add(view);
            }
            else if(targetClass.isInstance(v)) {
//                Logger.d(TAG, "findViewRecursive Found");
                ansList.add(v);
            }
            if(++index >= count)
                break;
        }
        return ansList;
    }
}
