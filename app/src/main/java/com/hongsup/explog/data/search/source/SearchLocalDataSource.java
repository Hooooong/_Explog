package com.hongsup.explog.data.search.source;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hongsup.explog.util.DBHelperUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hong on 2017-12-21.
 */

public class SearchLocalDataSource implements SearchSource.Local {

    private DBHelperUtil dbHelper;

    private static SearchLocalDataSource instance;

    private SearchLocalDataSource(Context context) {
        dbHelper = DBHelperUtil.getInstance(context);
    }

    public static SearchLocalDataSource getInstance(Context context) {
        if (instance == null)
            return instance = new SearchLocalDataSource(context);
        return instance;
    }

    @Override
    public List<String> loadRecentSearchWord() {
        String query = " SELECT id, word" +
                       " FROM history";
        List<String> wordList = new ArrayList<>();

        SQLiteDatabase connection = dbHelper.getReadableDatabase();

        Cursor cursor = connection.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String word = cursor.getString(1);
            wordList.add(0, word);
        }
        cursor.close();
        connection.close();
        close();

        return wordList;
    }

    @Override
    public void insertSearchWord(String word) {
        String query = " INSERT INTO history(word)" + "" +
                       " values('" + word + "')";
        SQLiteDatabase connection = dbHelper.getReadableDatabase();
        connection.execSQL(query);
        connection.close();
        close();
    }

    @Override
    public void deleteSearchWord(String word) {
        String query = " DELETE FROm history " +
                       " WHERE word = '" + word + "'";
        SQLiteDatabase connection = dbHelper.getReadableDatabase();
        connection.execSQL(query);
        connection.close();
        close();
    }


    private void close() {
        dbHelper.close();
    }
}
