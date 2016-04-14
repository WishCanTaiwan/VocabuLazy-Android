package com.wishcan.www.vocabulazy.storage;

import com.wishcan.www.vocabulazy.service.AudioService;

/**
 * Created by allencheng07 on 2016/4/14.
 *
 * The object is for storing the volatile arguments and parameters, which
 * should be separated from the @Database object.
 */
public class Preferences {
    public int wBookIndex = 1359;
    public int wLessonIndex = 1359;
    public int wItemIndex = 0;
    public int wSentenceIndex = -1;

    public String wPlayerStatus = AudioService.STATUS_IDLE;
}
