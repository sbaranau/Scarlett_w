package com.baranau.sergey.scarlett_w.dao;

/**
 * Created by sbaranau on 4/7/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baranau.sergey.scarlett_w.Entity.ParamsEntity;
import com.baranau.sergey.scarlett_w.Entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int BASE_VERSION = 7;

    private static final String DATABASE_NAME = "scarlet_2216.db";
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_AGE = "age";
    private static final String USER_COLUMN_GENDER = "gender";
    private static final String USER_COLUMN_HEIGHT = "height";
    private static final String USER_COLUMN_REG_DATE = "reg_date";
    private static final String USER_COLUMN_MISSING_REM = "missingrem";

    private static final String PARAMS_TABLE_NAME = "params";
    private static final String PARAMS_COLUMN_ID = "id";
    private static final String PARAMS_COLUMN_USEID = "iserid";
    private static final String PARAMS_COLUMN_DATE = "date";
    private static final String PARAMS_COLUMN_WEIGHT = "weight";
    private static final String PARAMS_COLUMN_FAT = "fat";
    private static final String PARAMS_COLUMN_TDW = "tdw";
    private static final String PARAMS_COLUMN_MISHCY = "mishcy";
    private static final String PARAMS_COLUMN_BONES = "bones";
    private static final String PARAMS_COLUMN_KCAL = "kcal";
    private static final String PARAMS_COLUMN_BMI = "bmi";




    private List<UserEntity> tempUserList = new ArrayList<>();
    private List<ParamsEntity> tempParamsList = new ArrayList<>();
    private static DataBaseHelper dataBaseHelper = null;
    private static SimpleDateFormat formatterForSql = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static int todaySql = 0;

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BASE_VERSION);
        todaySql = Integer.parseInt(formatterForSql.format(new Date()));
    }

    public static DataBaseHelper getDataBaseHelper(Context context) {
        if (dataBaseHelper == null) {
            return new DataBaseHelper(context);
        } else {
            return dataBaseHelper;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table " + USER_TABLE_NAME +
                            "(" + USER_COLUMN_ID + " integer primary key, "
                            + USER_COLUMN_NAME + " text, "
                            + USER_COLUMN_AGE + " integer, "
                            + USER_COLUMN_GENDER + " integer, " // 1 for man, 2 for women
                            + USER_COLUMN_MISSING_REM + " integer, " // for remind, 0 - don't remind
                            + USER_COLUMN_REG_DATE + " integer, "
                            + USER_COLUMN_HEIGHT + " integer) "
            );
            db.execSQL(
                    "create table " + PARAMS_TABLE_NAME +
                            "(" + PARAMS_COLUMN_ID + " integer primary key, "
                            + PARAMS_COLUMN_USEID + " integer, "
                            + PARAMS_COLUMN_DATE + " integer, "
                            + PARAMS_COLUMN_WEIGHT + " real, "
                            + PARAMS_COLUMN_BMI + " real, "
                            + PARAMS_COLUMN_BONES + " real, "
                            + PARAMS_COLUMN_FAT + " real, "
                            + PARAMS_COLUMN_KCAL + " real, "
                            + PARAMS_COLUMN_MISHCY + " real, "
                            + PARAMS_COLUMN_TDW + " real) "
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (tempUserList.size() > 0) {
            for (UserEntity userEntity: tempUserList) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(USER_COLUMN_AGE, userEntity.getAge());
                    contentValues.put(USER_COLUMN_GENDER, userEntity.getGender());
                    contentValues.put(USER_COLUMN_HEIGHT, userEntity.getHeight());
                    contentValues.put(USER_COLUMN_ID, userEntity.getId());
                    contentValues.put(USER_COLUMN_REG_DATE, userEntity.getId());
                    contentValues.put(USER_COLUMN_NAME, userEntity.getName());
                    contentValues.put(USER_COLUMN_MISSING_REM, userEntity.getMissingDateReminder());
                    db.insert(USER_TABLE_NAME, null, contentValues);
                } catch (Exception ex) {
                    Log.e("Insert UserEntity", "", ex);
                }
            }
        }

        if (tempParamsList.size() > 0) {
            for (ParamsEntity paramsEntity: tempParamsList) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PARAMS_COLUMN_BMI, paramsEntity.getBmi());
                    contentValues.put(PARAMS_COLUMN_BONES, paramsEntity.getBones());
                    contentValues.put(PARAMS_COLUMN_DATE, paramsEntity.getDate());
                    contentValues.put(PARAMS_COLUMN_FAT, paramsEntity.getFat());
                    contentValues.put(PARAMS_COLUMN_KCAL, paramsEntity.getKcal());
                    contentValues.put(PARAMS_COLUMN_MISHCY, paramsEntity.getMuscle());
                    contentValues.put(PARAMS_COLUMN_USEID, paramsEntity.getUserId());
                    contentValues.put(PARAMS_COLUMN_WEIGHT, paramsEntity.getWeight());
                    contentValues.put(PARAMS_COLUMN_TDW, paramsEntity.getTdw());
                    db.insert(PARAMS_TABLE_NAME, null, contentValues);
                } catch (Exception ex) {
                    Log.e("Insert Params", "", ex);
                }
            }
        }

    }

    public boolean deleteAllUsers() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.delete(USER_TABLE_NAME, null, null) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public boolean deleteAllParams() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.delete(PARAMS_TABLE_NAME, null, null) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteAll() {
        return deleteAllUsers() && deleteAllParams();
    }

    public boolean addTestParams(long userId) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Calendar cal = Calendar.getInstance();
            Date date;
            for (int i = 1; i <= 5; i++) {
                cal.add(Calendar.DAY_OF_YEAR, -i);
                date = cal.getTime();
                int dateVal = Integer.parseInt(formatterForSql.format(date));
                ParamsEntity temp = getParamsByDate(userId, dateVal);

                ContentValues contentValues = new ContentValues();
                contentValues.put(PARAMS_COLUMN_BMI, 7 - i * (((dateVal%2) == 0)?(-1):1));
                contentValues.put(PARAMS_COLUMN_BONES, 30 - i * (((dateVal%2) == 1)?(-1):1));
                contentValues.put(PARAMS_COLUMN_DATE, dateVal);
                contentValues.put(PARAMS_COLUMN_FAT, 15  - i * (((dateVal%2) == 0)?(-1):1));
                contentValues.put(PARAMS_COLUMN_KCAL, 1550 - i * (((dateVal%2) == 0)?(-1):1));
                contentValues.put(PARAMS_COLUMN_MISHCY, 48 - i * (((dateVal%2) == 0)?(-1):1));
                contentValues.put(PARAMS_COLUMN_USEID, userId);
                contentValues.put(PARAMS_COLUMN_WEIGHT, 73  - i * (((dateVal%2) == 0)?(-1):1));
                contentValues.put(PARAMS_COLUMN_TDW, 5 - i * (((dateVal%2) == 0)?(-1):1));
                if (temp.getDate() > 0) {
                    db.update(PARAMS_TABLE_NAME, contentValues,
                            PARAMS_COLUMN_USEID + " = ? AND " + PARAMS_COLUMN_DATE + " = ?",
                            new String[]{String.valueOf(temp.getUserId()), String.valueOf(temp.getDate())});

                } else {
                    db.insert(PARAMS_TABLE_NAME, null, contentValues);
                }
            }

            return  true;
        } catch (Exception ex) {
            Log.e("Insert Params", "", ex);
            return false;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            copyBase(db);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PARAMS_TABLE_NAME);
            onCreate(db);
    }

    private void copyBase(SQLiteDatabase db) {
        try {
            Cursor app = db.rawQuery("select * from " + USER_TABLE_NAME, null);
            if (app.getCount() > 0) {
                    app.moveToFirst();
                    while (!app.isAfterLast()) {
                        UserEntity appEntity = new UserEntity();
                        appEntity.setId(app.getLong(app.getColumnIndex(USER_COLUMN_ID)));
                        appEntity.setName(app.getString(app.getColumnIndex(USER_COLUMN_NAME)));
                        appEntity.setAge(app.getInt(app.getColumnIndex(USER_COLUMN_AGE)));
                        appEntity.setRegDate(app.getInt(app.getColumnIndex(USER_COLUMN_REG_DATE)));
                        appEntity.setGender(app.getInt(app.getColumnIndex(USER_COLUMN_GENDER)));
                        appEntity.setHeight(app.getInt(app.getColumnIndex(USER_COLUMN_HEIGHT)));
                        appEntity.setMissingDateReminder(app.getInt(app.getColumnIndex(USER_COLUMN_MISSING_REM)));
                        tempUserList.add(appEntity);
                        app.moveToNext();
                    }
                app.close();
            }

            Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME, null);
            if (params.getCount() > 0) {
                params.moveToFirst();
                while (!params.isAfterLast()) {
                    ParamsEntity paramsEntity = new ParamsEntity();
                    paramsEntity.setUserId(params.getLong(params.getColumnIndex(PARAMS_COLUMN_USEID)));
                    paramsEntity.setBmi(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BMI)));
                    paramsEntity.setBones(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BONES)));
                    paramsEntity.setDate(params.getInt(params.getColumnIndex(PARAMS_COLUMN_DATE)));
                    paramsEntity.setFat(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_FAT)));
                    paramsEntity.setKcal(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_KCAL)));
                    paramsEntity.setMuscle(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_MISHCY)));
                    paramsEntity.setTdw(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_TDW)));
                    paramsEntity.setWeight(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_WEIGHT)));
                    tempParamsList.add(paramsEntity);
                    params.moveToNext();
                }
                params.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<Integer, String> getUsersNames() {
        HashMap<Integer, String> user = new HashMap<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + USER_COLUMN_ID + "," + USER_COLUMN_NAME + " from " + USER_TABLE_NAME, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    user.put(c.getInt(0), c.getString(1));
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return user;
        }
        return user;
    }

    public long addUser(UserEntity user) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(USER_COLUMN_REG_DATE, todaySql);
            } catch (Exception ex) {
                contentValues.put(USER_COLUMN_AGE, 0);
            }
            contentValues.put(USER_COLUMN_AGE, user.getAge());
            contentValues.put(USER_COLUMN_HEIGHT, user.getHeight());
            contentValues.put(USER_COLUMN_GENDER, user.getGender());
            contentValues.put(USER_COLUMN_MISSING_REM, 1);
            contentValues.put(USER_COLUMN_NAME, user.getName());
            return db.insert(USER_TABLE_NAME, null, contentValues);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  -1;
    }

    public UserEntity getUser(long id) {
        UserEntity user = new UserEntity();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from " + USER_TABLE_NAME + " where " + USER_COLUMN_ID + " = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    user.setId(id);
                    user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
                    user.setAge(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_AGE)));
                    user.setGender(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_GENDER)));
                    user.setHeight(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_HEIGHT)));
                    user.setMissingDateReminder(cursor.getColumnIndex(USER_COLUMN_MISSING_REM));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public List<Integer> getEmptyDates(long id) {
        int firstDate = getFirstDate(id);
        List<Integer> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int day = firstDate%100;
        int month = (firstDate/100)%100;
        int year = firstDate/10000;
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        int date = firstDate;
        ParamsEntity paramsEntity;
        while (date != todaySql) {
            paramsEntity = getParamsByDate(id,date);
            if (paramsEntity.getDate() == 0) {
                dates.add(date);
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
            date = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 +
                    cal.get(Calendar.DAY_OF_MONTH);

        }
        return dates;
    }

    public boolean updateUser(UserEntity user) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            try {
                contentValues.put(USER_COLUMN_REG_DATE, Integer.parseInt(formatter.format(new Date())));
            } catch (Exception ex) {
                contentValues.put(USER_COLUMN_AGE, 0);
            }
            contentValues.put(USER_COLUMN_AGE, user.getAge());
            contentValues.put(USER_COLUMN_HEIGHT, user.getHeight());
            contentValues.put(USER_COLUMN_GENDER, user.getGender());
            contentValues.put(USER_COLUMN_NAME, user.getName());
            contentValues.put(USER_COLUMN_MISSING_REM, user.getMissingDateReminder());
            return db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(user.getId())}) == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  false;
    }

    public boolean updateParametersOnDate(ParamsEntity paramsEntity, long userId, int date) {

        return true;
    }
/*
      return previous parameters(ignore today)
 */
    public ParamsEntity getLastParams(long id) {
        ParamsEntity paramsEntity = new ParamsEntity();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME + " where "
                    + PARAMS_COLUMN_USEID + " = " + id + " and " + PARAMS_COLUMN_DATE + "<" + todaySql
                    + " order by " + PARAMS_COLUMN_DATE
                    + " DESC", null);
            if (params.getCount() > 0) {
                params.moveToFirst();
                if (!params.isAfterLast()) {
                    paramsEntity.setUserId(params.getLong(params.getColumnIndex(PARAMS_COLUMN_USEID)));
                    paramsEntity.setBmi(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BMI)));
                    paramsEntity.setBones(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BONES)));
                    paramsEntity.setDate(params.getInt(params.getColumnIndex(PARAMS_COLUMN_DATE)));
                    paramsEntity.setFat(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_FAT)));
                    paramsEntity.setKcal(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_KCAL)));
                    paramsEntity.setMuscle(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_MISHCY)));
                    paramsEntity.setTdw(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_TDW)));
                    paramsEntity.setWeight(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_WEIGHT)));
                }
                params.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paramsEntity;
    }

    public ParamsEntity getParamsByDate(long id, int date) {
        ParamsEntity paramsEntity = new ParamsEntity();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME + " where "
                    + PARAMS_COLUMN_USEID + " = " + id + " and "+ PARAMS_COLUMN_DATE + "="
                    + date + " order by " + PARAMS_COLUMN_DATE + " DESC", null);
            if (params.getCount() > 0) {
                params.moveToFirst();
                if (!params.isAfterLast()) {
                    paramsEntity.setUserId(params.getLong(params.getColumnIndex(PARAMS_COLUMN_USEID)));
                    paramsEntity.setBmi(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BMI)));
                    paramsEntity.setBones(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_BONES)));
                    paramsEntity.setDate(params.getInt(params.getColumnIndex(PARAMS_COLUMN_DATE)));
                    paramsEntity.setFat(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_FAT)));
                    paramsEntity.setKcal(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_KCAL)));
                    paramsEntity.setMuscle(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_MISHCY)));
                    paramsEntity.setTdw(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_TDW)));
                    paramsEntity.setWeight(params.getFloat(params.getColumnIndex(PARAMS_COLUMN_WEIGHT)));
                }
                params.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paramsEntity;
    }

    public int getFirstDate(long id) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME + " where "
                        + PARAMS_COLUMN_USEID + " = " + id + " and " + PARAMS_COLUMN_DATE
                        + " order by " + PARAMS_COLUMN_DATE
                        + " ASC", null);
                if (params.getCount() > 0) {
                    params.moveToFirst();
                    if (!params.isAfterLast()) {
                        return params.getInt(params.getColumnIndex(PARAMS_COLUMN_DATE));
                    }
                    params.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return 0;
    }

    public boolean addParams(ParamsEntity paramsEntity) {
        long result;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ParamsEntity temp = getParamsByDate(paramsEntity.getUserId(), paramsEntity.getDate());
            ContentValues contentValues = new ContentValues();
            contentValues.put(PARAMS_COLUMN_BMI, paramsEntity.getBmi());
            contentValues.put(PARAMS_COLUMN_BONES, paramsEntity.getBones());
            contentValues.put(PARAMS_COLUMN_DATE, paramsEntity.getDate());
            contentValues.put(PARAMS_COLUMN_FAT, paramsEntity.getFat());
            contentValues.put(PARAMS_COLUMN_KCAL, paramsEntity.getKcal());
            contentValues.put(PARAMS_COLUMN_MISHCY, paramsEntity.getMuscle());
            contentValues.put(PARAMS_COLUMN_USEID, paramsEntity.getUserId());
            contentValues.put(PARAMS_COLUMN_WEIGHT, paramsEntity.getWeight());
            contentValues.put(PARAMS_COLUMN_TDW, paramsEntity.getTdw());
            if (temp.getDate() > 0) {
                contentValues.put(PARAMS_COLUMN_DATE, temp.getDate());
                result = db.update(PARAMS_TABLE_NAME, contentValues,
                        PARAMS_COLUMN_USEID + " = ? AND " + PARAMS_COLUMN_DATE + " = ?",
                        new String[]{String.valueOf(temp.getUserId()), String.valueOf(temp.getDate())});
            } else {
                if (paramsEntity.getDate() == 0) {
                    contentValues.put(PARAMS_COLUMN_DATE, todaySql);
                }
                result = db.insert(PARAMS_TABLE_NAME, null, contentValues);
            }
        } catch (Exception ex) {
            Log.e("Insert Params", "", ex);
            return false;
        }
        return result >= 1;
    }

}

