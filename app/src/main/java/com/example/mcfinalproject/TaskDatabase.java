package com.example.mcfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "task_db.db";
    private static final int DB_VERSION = 1;

    public TaskDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE tasks (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "priority INTEGER," +
                "due_date TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    public long addTask(String title, String description, int priority, String dueDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("priority", priority);
        values.put("due_date", dueDate);
        long id = db.insert("tasks", null, values);
        db.close();
        return id;
    }

    public int updateTask(int id, String title, String description, int priority, String dueDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("priority", priority);
        values.put("due_date", dueDate);
        int rowsAffected = db.update("tasks", values, "task_id=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("tasks", "task_id=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tasks", null);
    }

    public Cursor getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tasks WHERE task_id=?", new String[]{String.valueOf(id)});
    }

    /*
    TODO (maybe):
    - Task difficulty
    - Auto-recommend task completion
     */
}

