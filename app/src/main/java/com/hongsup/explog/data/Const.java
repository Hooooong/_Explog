package com.hongsup.explog.data;

/**
 * Created by Android Hong on 2017-11-29.
 */

public class Const {
    public static final String SERVER_URL = "http://explog-shz.ap-northeast-2.elasticbeanstalk.com";

    public static final int REQ_GALLERY = 101;
    public static final int REQ_TEXT = 102;
    public static final int REQ_PATH = 103;

    public static final int VIEW_TYPE_INIT = 0;
    public static final int VIEW_TYPE_TEXT = 1;
    public static final int VIEW_TYPE_PHOTO = 2;
    public static final int VIEW_TYPE_PATH = 3;
    public static final int VIEW_TYPE_FOOTER = 4;

    public static final String CONTENT_TYPE_INIT = "init";
    public static final String CONTENT_TYPE_TEXT = "txt";
    public static final String CONTENT_TYPE_PHOTO = "img";
    public static final String CONTENT_TYPE_PATH = "path";
    public static final String CONTENT_TYPE_FOOTER = "footer";

    public static final String CONTENT_TEXT_TYPE_BASIC = "b";
    public static final String CONTENT_TEXT_TYPE_HIGHLIGHT = "h";

    public static final String INTENT_EXTRA_COVER = "COVER";
    public static final String INTENT_EXTRA_TEXT = "TEXT";
    public static final String INTENT_EXTRA_TYPE = "TYPE";
    public static final String INTENT_EXTRA_PHOTO = "PHOTO";
    public static final String INTENT_EXTRA_DATE ="DATE";


}
