package com.example.acholyte.itnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by acholyte on 2015-09-16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "MyITDictionary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table dictionary (num integer, subject text, content text, regdate text, ip text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table dictionary;"); // DB 버전이 변경되면 삭제하고 다시 생성
        onCreate(db);
    }

    // 로컬 DB의 최신 레코드의 등록 시간을 읽어오는 함수
    public String getLatestRecordTime() {
        SQLiteDatabase db;
        Cursor cursor;

        String latest = null;

        db = this.getReadableDatabase();

         cursor = db.rawQuery("select regdate from dictionary order by regdate desc", null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();

            latest = cursor.getString(0);
        }
        return latest;
    }

    // 로컬 DB의 전체 레코드를 읽어오는 함수
    public HashMap dbRead() {
        SQLiteDatabase db;
        Cursor cursor;

        HashMap map = new HashMap();
        db = this.getReadableDatabase();

        cursor = db.rawQuery("select * from dictionary", null);
        for (int i = 0; cursor.moveToNext(); i++) {
            String[] data = new String[5];
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            map.put(i, data);
        }

        return map;
    }

    // 매개변수로 받은 컬럼명과 그 값을 이용하여 로컬 DB의 레코드를 읽어오는 함수
    public HashMap dbRead(String selector, String keyword) {
        SQLiteDatabase db;
        Cursor cursor;

        HashMap map = new HashMap();
        db = this.getReadableDatabase();

        cursor = db.rawQuery("select * from dictionary where " + selector + " like ?", new String[] {"%" + keyword + "%"});

        for (int i = 0; cursor.moveToNext(); i++) {
            String[] data = new String[5];
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(2);
            data[3] = cursor.getString(3);
            data[4] = cursor.getString(4);
            map.put(i, data);
        }

        return map;
    }

    // 로컬 DB에 새로 쓰는 함수
    public void dbWrite(HashMap map) {
        SQLiteDatabase db;

        db = this.getWritableDatabase();
        db.execSQL("delete from dictionary;"); // temp

        for (int i = 0; i < map.size(); i++) {
            String[] data = (String[]) map.get(i);

            db.execSQL("insert into dictionary values(" +
                    "'" + data[0] + "', " + "'" + data[1] + "', " + "'" + data[2] + "', " + "'" + data[3] + "', " + "'" + data[4] + "');");
        }
    }
}
