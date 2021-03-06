package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import db.entry.Book;
import util.config.FindTarget;
import util.Logger;

/**
 * Created by HanyLuv on 2015-10-24.
 */
public class CartoonDAOImpl implements ICartoonDAO {
    public static CartoonDAOImpl instance;
    private SQLiteDatabase database;
    private CartoonDBHelper dbHelper;
    private boolean isDBopen = false;

    public static CartoonDAOImpl getInstance() {
        if (instance == null) {
            instance = new CartoonDAOImpl();
        }
        return instance;
    }

    public void connectDB(Context context) {
        if (dbHelper == null) {
            dbHelper = new CartoonDBHelper(context);
        }
        try {
            if (database == null) {
                database = dbHelper.getWritableDatabase();
                isDBopen = true;
            }
        } catch (SQLiteException e) {
            Logger.d(e.getMessage());
        }
    }


    /**디비의 타이틀, 작가, 회사를 가져온다.*/
    public ArrayList<String> getDataAll() {
        ArrayList<String> findData = new ArrayList<>();

        String query = "SELECT DISTINCT "+TITLE+" FROM " + TABLE_NAME  ;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do { findData.add(cursor.getString(cursor.getColumnIndex(TITLE))); } while (cursor.moveToNext());
        }
        query = "SELECT DISTINCT "+AUTHOR+" FROM " + TABLE_NAME  ;
        cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do { findData.add(cursor.getString(cursor.getColumnIndex(AUTHOR))); } while (cursor.moveToNext());
        }

        query = "SELECT DISTINCT "+COMPANY+" FROM " + TABLE_NAME  ;
        cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do { findData.add(cursor.getString(cursor.getColumnIndex(COMPANY))); } while (cursor.moveToNext());
        }

        return findData;

    }

    /**디비에 데이터를 찾는다.
     * @param findTarget 가져올 필드명
     * @param findVale 찾을 값
     * */
    public ArrayList<Book> findData(FindTarget findTarget, String findVale) {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT DISTINCT * FROM " + TABLE_NAME + " WHERE " + findTarget.getKey() + " LIKE '%" + findVale + "%' ORDER BY " + findTarget.getKey() + " ASC";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                book.setBid(cursor.getString(cursor.getColumnIndex(BID)));
                book.setInternationalBookNumber(cursor.getString(cursor.getColumnIndex(IBOOK_NUMBER)));
                book.setPublished(cursor.getString(cursor.getColumnIndex(PUBLISHED)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                book.setCompany(cursor.getString(cursor.getColumnIndex(COMPANY)));

                books.add(book);
            } while (cursor.moveToNext());
        }
        return books;
    }


    public ArrayList<Book> findData(FindTarget findTarget, String findVale,int maxValue) {
        ArrayList<Book> books = new ArrayList<>();
        if (findVale.contains("'")) {
            findVale = findVale.split("'")[1];
        }


        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + findTarget.getKey() + " LIKE '%" + findVale + "%' ORDER BY " + findTarget.getKey() + " ASC";
//        Cursor cursor =  database.query(TABLE_NAME,new String[]{TITLE,AUTHOR,COMPANY,PUBLISHED,IBOOK_NUMBER,BID}, findTarget.getKey()+" LIKE '%"+ findVale+ "%'",null,null,null,"ASC");
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                book.setBid(cursor.getString(cursor.getColumnIndex(BID)));
                book.setInternationalBookNumber(cursor.getString(cursor.getColumnIndex(IBOOK_NUMBER)));
                book.setPublished(cursor.getString(cursor.getColumnIndex(PUBLISHED)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                book.setCompany(cursor.getString(cursor.getColumnIndex(COMPANY)));

                books.add(book);
            } while (cursor.moveToNext());
        }
        return books;
    }

    public void closeDB() {
        if (isOpen()) {
            database.close();
            database = null;
            dbHelper.close();
            dbHelper = null;
        }

    }

    public boolean isOpen() {
        return isDBopen;
    }
}
