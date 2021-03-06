package com.idotools.browser.gp.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.idotools.browser.gp.bean.RecordsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxiaojun on 16-12-1.
 */

public class RecordsSqliteManager {

    public static final String INSERT = "insert into " + SqliteOpenHelper.TABLE_NAME_RECORDS + " (title,img,url) values (?,?,?)";
    public static final String SELECT_ALL = "select * from " + SqliteOpenHelper.TABLE_NAME_RECORDS + " order by _id desc";
    public static final String SELECT_BY_TITLE = "select * from " + SqliteOpenHelper.TABLE_NAME_RECORDS + " where url = ?";
    public static final String DELETE = "delete from " + SqliteOpenHelper.TABLE_NAME_RECORDS + " where url=?";
    public static final String DELETE_ALL = "delete from " + SqliteOpenHelper.TABLE_NAME_RECORDS;

    private SqliteOpenHelper sqliteOpenHelper;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writeableDatabase;

    public RecordsSqliteManager(Context context) {
        sqliteOpenHelper = new SqliteOpenHelper(context);
        readableDatabase = sqliteOpenHelper.getReadableDatabase();
        writeableDatabase = sqliteOpenHelper.getWritableDatabase();
    }

    /***
     * 插入一条数据
     * 先判断是否存在数据库
     *
     * @param bean
     */
    public void insert(RecordsBean bean) {
        if (bean != null) {
            boolean result = selectByUrl(bean.url);
            if (!result) {
                writeableDatabase.execSQL(INSERT, new Object[]{bean.title, bean.imgUrl, bean.url});
            }
        }
    }

    /***
     * 查找所有数据
     *
     * @return
     */
    public List<RecordsBean> selectAll() {
        List<RecordsBean> list = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery(SELECT_ALL, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
//            byte[] bytes = cursor.getBlob(cursor.getColumnIndex("img"));
            String imgUrl = cursor.getString(cursor.getColumnIndex("img"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            list.add(new RecordsBean(title, imgUrl, url));
        }
        cursor.close();
        return list;
    }

    /***
     * 根据url查找数据库是否存在这条数据
     *
     * @param url
     * @return 数据库中最新章节, 判断是否需要更新数据
     */
    public boolean selectByUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            Cursor cursor = readableDatabase.rawQuery(SELECT_BY_TITLE, new String[]{url});
            if (cursor.moveToNext()) {
                return true;
            }
        }
        return false;
    }

    /***
     * 删除记录
     *
     * @param url
     */
    public void delete(String url) {
        if (!TextUtils.isEmpty(url)) {
            writeableDatabase.execSQL(DELETE, new Object[]{url});
        }
    }

    /***
     * 删除所有
     */
    public void deleteAll() {
        writeableDatabase.execSQL(DELETE_ALL, new Object[]{});
    }

    /**
     * 释放资源
     */
    public void closeData() {
        readableDatabase.close();
        writeableDatabase.close();
    }

}
