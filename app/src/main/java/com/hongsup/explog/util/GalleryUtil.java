package com.hongsup.explog.util;

/**
 * Created by 정인섭 on 2017-11-30.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import com.hongsup.explog.data.photo.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-11-17.
 */

public class GalleryUtil {

    /**
     * Gallery 이미지 반환
     *
     * @param context
     * @return
     */
    public static List<Photo> getAllPhotoPathList(final Context context) {
        ArrayList<Photo> photoList = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED
        };

        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                projection[1] + " DESC");
        while (cursor.moveToNext()) {
            Photo photo = new Photo();
            int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            photo.setImagePath(cursor.getString(index));
            photoList.add(photo);
        }
        cursor.close();

        return photoList;
    }
}
