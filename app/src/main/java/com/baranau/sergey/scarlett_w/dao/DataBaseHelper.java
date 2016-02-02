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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int BASE_VERSION = 4;

    private static final String DATABASE_NAME = "scarlet_2216.db";
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_AGE = "age";
    private static final String USER_COLUMN_GENDER = "gender";
    private static final String USER_COLUMN_HEIGHT = "height";
    private static final String USER_COLUMN_REG_DATE = "reg_date";

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
    private static SimpleDateFormat formatterForInt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static int todaySql = 0;

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BASE_VERSION);
        todaySql = Integer.parseInt(formatterForInt.format(new Date()));
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
            Cursor c = db.rawQuery("select " + USER_COLUMN_ID + "," + USER_COLUMN_NAME + " from " + USER_TABLE_NAME, null );
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
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
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
            return db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ? ", new String[]{String.valueOf(user.getId())}) == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  false;
    }

    public ParamsEntity getLastParams(long id) {
        ParamsEntity paramsEntity = new ParamsEntity();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME + " where "
                    + PARAMS_COLUMN_USEID + " = " + id + " order by " + PARAMS_COLUMN_DATE
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
            } else {
                paramsEntity.setUserId(id);
                paramsEntity.setBmi(0);
                paramsEntity.setBones(0);
                paramsEntity.setDate(0);
                paramsEntity.setFat(0);
                paramsEntity.setKcal(0);
                paramsEntity.setMuscle(0);
                paramsEntity.setTdw(0);
                paramsEntity.setWeight(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paramsEntity;
    }

    public ParamsEntity getPreviousParams(long id, int date) {
        ParamsEntity paramsEntity = new ParamsEntity();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor params = db.rawQuery("select * from " + PARAMS_TABLE_NAME + " where "
                    + PARAMS_COLUMN_USEID + " = " + id + " order by " + PARAMS_COLUMN_DATE
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
            } else {
                paramsEntity.setUserId(id);
                paramsEntity.setBmi(0);
                paramsEntity.setBones(0);
                paramsEntity.setDate(0);
                paramsEntity.setFat(0);
                paramsEntity.setKcal(0);
                paramsEntity.setMuscle(0);
                paramsEntity.setTdw(0);
                paramsEntity.setWeight(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paramsEntity;
    }


    public boolean addParams(ParamsEntity paramsEntity) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PARAMS_COLUMN_BMI, paramsEntity.getBmi());
            contentValues.put(PARAMS_COLUMN_BONES, paramsEntity.getBones());
            contentValues.put(PARAMS_COLUMN_DATE, todaySql);
            contentValues.put(PARAMS_COLUMN_FAT, paramsEntity.getFat());
            contentValues.put(PARAMS_COLUMN_KCAL, paramsEntity.getKcal());
            contentValues.put(PARAMS_COLUMN_MISHCY, paramsEntity.getMuscle());
            contentValues.put(PARAMS_COLUMN_USEID, paramsEntity.getUserId());
            contentValues.put(PARAMS_COLUMN_WEIGHT, paramsEntity.getWeight());
            contentValues.put(PARAMS_COLUMN_TDW, paramsEntity.getTdw());
            long id = db.insert(PARAMS_TABLE_NAME, null, contentValues);
            return  id > 0;
        } catch (Exception ex) {
            Log.e("Insert Params", "", ex);
            return false;
        }
    }

/*
    public int updateUser(String user, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_PASSWORD, password);
            contentValues.put(SC_COLUMN_USER, user);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int newUser(String user, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_PASSWORD, password);
            contentValues.put(SC_COLUMN_USER, user);
            contentValues.put(SC_COLUMN_USER_FILMS, "");
            contentValues.put(SC_COLUMN_NEWFILM, "");
            contentValues.put(SC_COLUMN_CUSTOMER_ID, "");
            contentValues.put(SC_COLUMN_CUSTOMER_CARD, "");
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int updateUserFilms(String films) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_USER_FILMS, films);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int updateNewFilm(String film) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_NEWFILM, film);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }
    public int updateToken(String token) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_TOKEN, token);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int updateStoriedId(String storied_id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_STORE_ID, storied_id);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int updateCustomerCard(String card) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_CUSTOMER_CARD, card);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int updateCustomer_Id(String customer_id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_TOKEN, customer_id);
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int setAutoLogin() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_AUTOLOGIN, "1");
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }

    public int cancelAutoLogin() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SC_COLUMN_AUTOLOGIN, "0");
            return db.update(SC_TABLE_NAME, contentValues, SC_COLUMN_ID + " = ? ", new String[]{String.valueOf("1")});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  0;
    }




    public String getUserName() {
        String user = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_USER +  " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    user = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return user==null?"":user;

    }
    public String getNewFilm() {
        String film = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_NEWFILM + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    film = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return film==null?"":film;

    }

    public String getUserFilms() {
        String films = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_USER_FILMS + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    films = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return films==null?"":films;

    }

    public String getToken() {
        String token = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_TOKEN + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    token = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return token==null?"":token;

    }

    public String getCustomerId() {
        String customerId = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_CUSTOMER_ID + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    customerId = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return customerId==null?"":customerId;

    }

    public boolean getAutoLogin() {
        String autologin = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_AUTOLOGIN + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    autologin = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return false;
        }
        return "1".equals(autologin.trim());
    }

    public String getStoriedId() {
        String storiedId = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_STORE_ID + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    storiedId = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return storiedId==null?"":storiedId;

    }

    public String getCustomerCard() {
        String card = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select " + SC_COLUMN_CUSTOMER_CARD + " from " + SC_TABLE_NAME + " where " + SC_COLUMN_ID + " = 1", null );
            if (c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    card = c.getString(0);
                    c.moveToNext();
                }
                c.close();
            }
        } catch (Exception ex) {
            return "";
        }
        return card==null?"":card;

    }*/

}

