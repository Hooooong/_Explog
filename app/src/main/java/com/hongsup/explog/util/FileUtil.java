package com.hongsup.explog.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

/**
 * Created by Android Hong on 2017-12-22.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static File resizeFile(String fileName){

        File file  = new File(fileName);

        if(file.length() > 5e+6){
            Log.e(TAG, "resizeFile: " + file.length() +" 가 5MB 보다 크다.");

            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap , 100,100,true);
            return null;
        }else{
           return file;
        }
    }

}
