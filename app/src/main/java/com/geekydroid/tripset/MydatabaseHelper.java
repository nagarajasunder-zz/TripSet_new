package com.geekydroid.tripset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class MydatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tripset.db";
    public static final int DATABASE_VERSION = 8;
    public static final String TABLE_NAME1 = "trip";
    public static final String TABLE_NAME2 = "members";
    public static final String TABLE_NAME3 = "spent_history";
    public static final String TABLE_NAME4 = "spent";
    public static final String TABLE_NAME5 = "trip_description";
    public static final String TABLE_NAME6 = "category";
    public static final String TABLE_NAME7 = "places_to_visit";
    public static final String TABLE_NAME8 = "def_currency";
    public static final String T1C1 = "T_ID";
    public static final String T1C2 = "T_NAME";
    public static final String T1C3 = "DATE";
    public static final String T1C4 = "TOTAL_AMT";
    public static final String T1C5 = "GROUP_SIZE";
    public static final String T1C6 = "TRIP_DESC";
    public static final String T1C7 = "CURRENCY";
    public static final String T2C1 = "M_ID";
    public static final String T2C2 = "T_ID";
    public static final String T2C3 = "NAME";
    public static final String T2C4 = "SPENT";
    public static final String T2C5 = "DUE";
    public static final String T3C1 = "S_ID";
    public static final String T3C2 = "T_ID";
    public static final String T3C3 = "M_ID";
    public static final String T3C4 = "AMT";
    public static final String T3C5 = "DATE";
    public static final String T3C6 = "CATEGORY";
    public static final String T3C7 = "DESCRIPTION";
    public static final String T3C8 = "M_NAME";
    public static final String T3C9 = "SHARE_BY";
    public static final String T4C1 = "SH_ID";
    public static final String T4C2 = "T_ID";
    public static final String T4C3 = "SPENT_BY";
    public static final String T4C4 = "SPENT_ON";
    public static final String T4C5 = "AMT";
    public static final String T4C6 = "SESSION_ID";
    public static final String T5C1 = "T_ID";
    public static final String T5C2 = "DESCRIPTION";
    public static final String T6C1 = "C_ID";
    public static final String T6C2 = "CATEFORY_NAMES";
    public static final String T7C1 = "P_ID";
    public static final String T7C2 = "T_ID";
    public static final String T7C3 = "PLACES";
    public static final String T8C1 = "C_ID";
    public static final String T8C2 = "CURRENCY";
    private static final String TAG = "MydatabaseHelper";
    private Context context;
    private double spent_amt = 0;
    private ArrayList<String> p_id;


    public MydatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("my_helper", "onCreate: called");
        String query1 = "CREATE TABLE " + TABLE_NAME1 + " (" +
                T1C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T1C2 + " TEXT, " +
                T1C3 + " TEXT, " +
                T1C4 + " REAL, " +
                T1C5 + " INTEGER, " +
                T1C6 + " TEXT, " +
                T1C7 + " TEXT);";

        String query2 = "CREATE TABLE " + TABLE_NAME2 + " (" +
                T2C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T2C2 + " INTEGER, " +
                T2C3 + " TEXT, " +
                T2C4 + " REAL, " +
                T2C5 + " REAL, " +
                "FOREIGN KEY (" + T2C2 + ") REFERENCES " + TABLE_NAME1 + "(" + T1C1 + "));";

        String query3 = "CREATE TABLE " + TABLE_NAME3 + " ("
                + T3C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T3C2 + " INTEGER, "
                + T3C3 + " INTEGER, "
                + T3C4 + " REAL, "
                + T3C5 + " TEXT, "
                + T3C6 + " TEXT, "
                + T3C7 + " TEXT, "
                + T3C8 + " TEXT, "
                + T3C9 + " TEXT, "
                + "FOREIGN KEY (" + T3C8 + ") REFERENCES " + TABLE_NAME2 + "(" + T2C3 + "), "
                + "FOREIGN KEY (" + T3C3 + ") REFERENCES " + TABLE_NAME2 + "(" + T2C1 + "), "
                + "FOREIGN KEY (" + T3C2 + ") REFERENCES " + TABLE_NAME1 + "(" + T1C1 + "));";

        String query4 = "CREATE TABLE " + TABLE_NAME4 + " ("
                + T4C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T4C6 + " INTEGER, "
                + T4C2 + " INTEGER, "
                + T4C3 + " INTEGER, "
                + T4C4 + " INTEGER, "
                + T4C5 + " REAL, "
                + "FOREIGN KEY (" + T4C2 + ") REFERENCES " + TABLE_NAME1 + "(" + T1C1 + "), "
                + "FOREIGN KEY (" + T4C3 + ") REFERENCES " + TABLE_NAME2 + "(" + T2C1 + "), "
                + "FOREIGN KEY (" + T4C4 + ") REFERENCES " + TABLE_NAME2 + "(" + T2C1 + "));";

        String query5 = "CREATE TABLE " + TABLE_NAME5 + " ("
                + T5C1 + " INTEGER, "
                + T5C2 + " TEXT, "
                + "FOREIGN KEY (" + T1C1 + ") REFERENCES " + TABLE_NAME1 + "(" + T1C1 + "));";

        String query6 = "CREATE TABLE " + TABLE_NAME6 + " ("
                + T6C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T6C2 + " TEXT);";

        String query7 = "CREATE TABLE " + TABLE_NAME7 + " ("
                + T7C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T7C2 + " INTEGER, "
                + T7C3 + " TEXT, "
                + "FOREIGN KEY(" + T7C2 + ") REFERENCES " + TABLE_NAME1 + "(" + T1C1 + "));";

        String query8 = "CREATE TABLE " + TABLE_NAME8 + " ("
                + T8C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T8C2 + " TEXT);";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        db.execSQL(query8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            create_new_table(db);
            return;
        }
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String query4 = "DROP TABLE IF EXISTS " + TABLE_NAME4;
        String query5 = "DROP TABLE IF EXISTS " + TABLE_NAME5;
        String query6 = "DROP TABLE IF EXISTS " + TABLE_NAME6;
        String query7 = "DROP TABLE IF EXISTS " + TABLE_NAME7;
        String query8 = "DROP TABLE IF EXISTS " + TABLE_NAME8;
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        db.execSQL(query8);

        onCreate(db);
    }

    private void create_new_table(SQLiteDatabase db) {
        String query8 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME8 + " ("
                + T8C1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + T8C2 + " TEXT);";
        db.execSQL(query8);
        try {
            db.execSQL("ALTER TABLE " + TABLE_NAME1 + " ADD COLUMN " + T1C7 + " TEXT");
        } catch (SQLException e) {
            Log.d("my_helper", "Exception caught: ");
            e.printStackTrace();
        }
    }

    public long create_new_trip(String T_name, String T_desc, int size, int total_amt, String date, String currency) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T1C2, T_name);
        cv.put(T1C3, date);
        cv.put(T1C4, total_amt);
        cv.put(T1C5, size);
        cv.put(T1C6, T_desc);
        cv.put(T1C7, currency);

        long result = database.insert(TABLE_NAME1, null, cv);
        return result;
    }

    public long add_mem_in_trip(String name, double spent, double due, int t_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T2C2, t_id);
        cv.put(T2C3, name);
        cv.put(T2C4, spent);
        cv.put(T2C5, due);

        long result = database.insert(TABLE_NAME2, null, cv);
        return result;
    }

    public Cursor get_trip_id() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT " + T1C1 + " FROM " + TABLE_NAME1 + " ORDER BY " + T1C1 + " DESC LIMIT 1";
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor fetch_trip_details() {
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor fetch_all_members(int t_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + T1C1 + "=" + t_id + ";";
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public long add_new_expense(double spent, double due, String m_id, String t_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T2C4, spent);
        cv.put(T2C5, due);
        long result = database.update(TABLE_NAME2, cv, T2C1 + " = ? AND " + T2C2 + " = ?", new String[]{m_id, t_id});
        return result;
    }

    public double get_trip_total_amt(String t_id) {
        double total_amt = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT SUM(" + T4C5 + ") FROM " + TABLE_NAME4 + " WHERE " + T4C2 + "=" + t_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(0) != null) {
                        total_amt = Double.parseDouble(cursor.getString(0));
                    }
                }
            }
        }
        return total_amt;
    }

    public long update_total_amt(String t_id, double tot_amt) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T1C4, tot_amt);
        long result = database.update(TABLE_NAME1, cv, T1C1 + " = ?", new String[]{t_id});
        return result;
    }

    public long add_spent_history(String t_id, String m_id, String m_name, double amt, String date, String category, String description, String share_by) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T3C2, t_id);
        cv.put(T3C3, m_id);
        cv.put(T3C4, amt);
        cv.put(T3C5, date);
        cv.put(T3C6, category);
        cv.put(T3C7, description);
        cv.put(T3C8, m_name);
        cv.put(T3C9, share_by);

        long result = database.insert(TABLE_NAME3, null, cv);
        return result;
    }

    public Cursor get_trip_expense_history(String t_id) {
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=" + t_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor get_category_expense(String t_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT " + T3C6 + ",SUM(" + T3C4 + ") FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=" + t_id + " GROUP BY " + T3C6;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public long add_share(String session_id, String t_id, String spent_on, String spent_by, double amt) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T4C2, t_id);
        cv.put(T4C3, spent_by);
        cv.put(T4C4, spent_on);
        cv.put(T4C5, amt);
        cv.put(T4C6, session_id);

        long result = database.insert(TABLE_NAME4, null, cv);
        return result;
    }

    public double get_due_amt(String t_id, String m_id) {
        double spent_by = 0, spent_for = 0, due;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor1 = null, cursor2 = null;

        String spent_by_him = "SELECT SUM(" + T4C5 + ") FROM " + TABLE_NAME4 + " WHERE " + T4C2 + "=" + t_id + " AND " + T4C3 + "=" + m_id + " AND " + T4C4 + "!=" + m_id;
        String spent_for_him = "SELECT SUM(" + T4C5 + ") FROM " + TABLE_NAME4 + " WHERE " + T4C2 + "=" + t_id + " AND " + T4C3 + "!=" + m_id + " AND " + T4C4 + "=" + m_id;

        if (database != null) {
            cursor1 = database.rawQuery(spent_by_him, null);
            cursor2 = database.rawQuery(spent_for_him, null);

            if (cursor1.getCount() > 0) {
                while (cursor1.moveToNext() && cursor1.getString(0) != null) {
                    spent_by = Double.parseDouble(cursor1.getString(0));

                }
            }

            if (cursor2.getCount() > 0) {
                while (cursor2.moveToNext() && cursor2.getString(0) != null) {
                    spent_for = Double.parseDouble(cursor2.getString(0));
                }
            }
        }
        due = spent_by - spent_for;
        return due;
    }

    public int get_session_id() {
        int session_id = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT MAX(" + T3C1 + ") FROM " + TABLE_NAME3;
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    session_id = Integer.parseInt(cursor.getString(0));
                }
            }
        }
        return session_id;
    }

    public void delete_spent_history(String s_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (database != null) {
            long result = database.delete(TABLE_NAME3, T3C1 + "=?", new String[]{s_id});
            Log.d("result", String.valueOf(result));

            String query = "DELETE FROM " + TABLE_NAME4 + " WHERE " + T4C6 + "=" + s_id + ";";
            database.execSQL(query);
        }
    }

    public double get_spent_amt(String t_id, String m_id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT SUM(" + T4C5 + ") FROM " + TABLE_NAME4 + " WHERE " + T4C2 + "=" + t_id + " AND " + T4C3 + "=" + m_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(0) != null) {
                        spent_amt = Double.parseDouble(cursor.getString(0));
                    } else {
                        spent_amt = 0;
                    }
                }
            }
        }
        return spent_amt;
    }

    public void add_trip_desc(String t_id, String desc) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T5C1, t_id);
        cv.put(T5C2, desc);
        database.insert(TABLE_NAME5, null, cv);
    }

    public ArrayList<String> get_trip_members_names(String t_id) {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + T2C2 + "=" + t_id;

        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    names.add(cursor.getString(2));
                }
            }
        }

        return names;
    }

    public long add_a_category(String category) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T6C2, category);

        long result = database.insert(TABLE_NAME6, null, cv);

        return result;
    }

    public ArrayList<String> get_all_category() {
        ArrayList<String> category = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_NAME6;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    category.add(cursor.getString(1));
                }
            }
        }

        return category;
    }

    public String get_trip_desc(String t_id) {
        String desc = "";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_NAME5 + " WHERE " + T5C1 + "=" + t_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(1) != null) {
                        desc = cursor.getString(1);
                    } else {
                        desc = "";
                    }
                }
            }
        }
        return desc;
    }

    public long update_trip_desc(String t_id, String desc) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T5C2, desc);
        long result = database.update(TABLE_NAME5, cv, T5C1 + "=?", new String[]{t_id});
        return result;
    }

    public long delete_trip_desc(String t_id) {
        long result = 0;
        SQLiteDatabase database = this.getWritableDatabase();

        if (database != null) {
            result = database.delete(TABLE_NAME5, T5C1 + "=?", new String[]{t_id});
        }

        return result;
    }

    public long add_places_to_visit(String t_id, String place) {
        long result;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T7C2, t_id);
        cv.put(T7C3, place);
        result = database.insert(TABLE_NAME7, null, cv);
        return result;
    }

    public ArrayList<String> get_places_to_visit(String t_id) {


        ArrayList<String> places = new ArrayList<>();
        p_id = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        if (database != null) {
            String query = "SELECT * FROM " + TABLE_NAME7 + " WHERE " + T7C2 + "=" + t_id;
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    p_id.add(cursor.getString(0));
                    places.add(cursor.getString(2));
                }
            }
        }

        return places;
    }

    public ArrayList<String> get_p_id() {
        return p_id;
    }

    public long delete_a_place(String p_id) {
        long result = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        if (database != null) {
            database.delete(TABLE_NAME7, T7C1 + "=?", new String[]{p_id});
        }
        return result;
    }

    public void delete_a_trip(String t_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (database != null) {
            database.delete(TABLE_NAME1, T1C1 + "=?", new String[]{t_id});
            database.delete(TABLE_NAME2, T2C2 + "=?", new String[]{t_id});
            database.delete(TABLE_NAME3, T3C2 + "=?", new String[]{t_id});
            database.delete(TABLE_NAME4, T4C2 + "=?", new String[]{t_id});
            database.delete(TABLE_NAME5, T5C1 + "=?", new String[]{t_id});
            database.delete(TABLE_NAME7, T7C2 + "=?", new String[]{t_id});
        }
    }

    public long update_spent_history(String s_id, String t_id, String m_id, String m_name, String Amt, String Category, String Desc, String Share_By) {
        long result = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (database != null) {
            cv.put(T3C2, t_id);
            cv.put(T3C3, m_id);
            cv.put(T3C4, Amt);
            cv.put(T3C6, Category);
            cv.put(T3C7, Desc);
            cv.put(T3C8, m_name);
            cv.put(T3C9, Share_By);
            result = database.update(TABLE_NAME3, cv, T3C1 + "=?", new String[]{s_id});
        }
        return result;
    }

    public long delete_spent_instances(String s_id) {
        long result = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        if (database != null) {
            result = database.delete(TABLE_NAME4, T4C6 + "=?", new String[]{s_id});
        }
        return result;
    }


    public void delete_a_person(String t_id, String m_id, String count) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T1C5, Integer.parseInt(count) - 1);
        if (database != null) {
            database.delete(TABLE_NAME2, T2C1 + "=? AND " + T2C2 + "=?", new String[]{m_id, t_id});
            database.update(TABLE_NAME1, cv, T1C1 + "=?", new String[]{t_id});
        }
    }

    public void set_default_currency(String currency) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T8C2, currency);
        database.insert(TABLE_NAME8, null, cv);
    }

    public String get_currency() {
        String currency = "";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_NAME8;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    currency = cursor.getString(1);
                    return currency;
                }
            } else {
                return currency;
            }
        }

        return currency;
    }

    public void update_default_currency(String currency) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T8C2, currency);
        if (database != null) {
            database.update(TABLE_NAME8, cv, T8C1 + "=?", new String[]{"1"});
        }
    }

    public String get_trip_currency(String t_id) {
        String currency = "";
        Cursor cursor;
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT " + T1C7 + " FROM " + TABLE_NAME1 + " WHERE " + T1C1 + "=" + t_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    currency = cursor.getString(0);
                }
            }
        }

        return currency;
    }

    //    public ArrayList<Expense_by_person> get_person_spent_history(String t_id, String m_id) {
//        ArrayList<Expense_by_person> list = new ArrayList<>();
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = null;
//        String query = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=" + t_id + " AND " + T3C3 + "=" + m_id;
//        if (database != null) {
//            cursor = database.rawQuery(query, null);
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    list.add(new Expense_by_person(cursor.getString(2), cursor.getString(7), cursor.getString(3), cursor.getString(5), cursor.getString(4), cursor.getString(6), cursor.getString(8)));
//                }
//            }
//        }
//        return list;
//    }
//
    public ArrayList<String> get_all_trip_items(String t_id, String column) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT DISTINCT(" + column + ") FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=" + t_id;
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(0));
                }
            }
        }
        return list;
    }

    public ArrayList<Expense_by_person> get_column_filter_data(String t_id, String column_name, String column_value) {
        ArrayList<Expense_by_person> list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        String query;
        if (database != null) {
            if (column_name.equals("DATE")) {
                column_value = column_value.substring(0, 10);
                query = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=? AND " + column_name + " LIKE ?";
                cursor = database.rawQuery(query, new String[]{t_id, column_value + "%"});
            } else {
                query = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + T3C2 + "=? AND " + column_name + " = ?";
                cursor = database.rawQuery(query, new String[]{t_id, column_value});
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(new Expense_by_person(cursor.getString(2), cursor.getString(7), cursor.getString(3), cursor.getString(5), cursor.getString(4), cursor.getString(6), cursor.getString(8)));
                }
            }
        }
        Log.d("TAG", "get_column_filter_data: " + list.size());
        return list;
    }

    public ArrayList<Trip> order_by_amount(String order_by, String type) {
        ArrayList<Trip> list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        String query;
        if (type.equals("asc")) {
            query = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + order_by;
        } else {
            query = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + order_by + " DESC";
        }
        if (database != null) {
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(new Trip(cursor.getString(0), cursor.getString(1), cursor.getString(5), cursor.getString(3), cursor.getString(2), cursor.getString(4)));
                }
            }
        }

        return list;
    }

    public double get_column_spent_amt(String t_id, String column_name, String value) {
        double sum = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        String query = "SELECT SUM(" + T3C4 + ") FROM " + TABLE_NAME3 + " WHERE " + T3C2 + " = ? AND " + column_name + " = ?";
        if (database != null) {
            cursor = database.rawQuery(query, new String[]{t_id, value});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    sum += Double.parseDouble(cursor.getString(0));
                }
            }
        }

        return sum;
    }

    public double get_date_expense_sum(String t_id, String column_name, String value) {
        double sum = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor;
        String query = "SELECT SUM(" + T3C4 + ") FROM " + TABLE_NAME3 + " WHERE " + T3C2 + " = ? AND " + column_name + " LIKE ?";
        if (database != null) {
            cursor = database.rawQuery(query, new String[]{t_id, value + "%"});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    sum+= Double.parseDouble(cursor.getString(0));
                }
            }
        }
        Log.d(TAG, "get_date_expense_sum: "+sum);
        return sum;
    }


}
