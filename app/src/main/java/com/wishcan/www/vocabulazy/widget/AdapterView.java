package com.wishcan.www.vocabulazy.widget;

import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/11.
 */
public interface AdapterView<k> {

    void refreshView(int count, LinkedList<k> linkedList);

}
