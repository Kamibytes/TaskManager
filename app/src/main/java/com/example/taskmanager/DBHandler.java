package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myDB1";
    private static final String TASK_TABLE = "taskDetails";
    private static final String TASK_ID = "id";
    private static final String TASK_TITLE = "title";
    private static final String TASK_DESCRIPTION = "description";
    private static final String DUE_DATE = "date";
    private static final String TASK_TIME = "time";
    private static final String TASK_STATUS = "status";
    private static final String TASK_PRIORITY = "priority";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE="CREATE TABLE "+TASK_TABLE+"("
                + TASK_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_TITLE + " TEXT,"
                + TASK_DESCRIPTION + " TEXT,"
                + DUE_DATE+ " TEXT,"
                + TASK_TIME+ " TEXT,"
                + TASK_STATUS+ " TEXT,"
                + TASK_PRIORITY+ " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }
    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_TITLE, task.getTitle());
        values.put(TASK_DESCRIPTION, task.getDescription());
        values.put(DUE_DATE, task.getDueDate());
        values.put(TASK_TIME, task.getTime());
        values.put(TASK_PRIORITY, task.getPriority());
        values.put(TASK_STATUS, task.getStatus());
        db.insert(TASK_TABLE, null, values);
        db.close();
    }
    @SuppressLint("Range")
    public Task getTask(int taskId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TASK_TABLE, null, TASK_ID + "=?", new String[]{String.valueOf(taskId)}, null, null, null);
        Task task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                    cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                    cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                    cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                    cursor.getString(cursor.getColumnIndex(TASK_STATUS))
            );
            cursor.close();
        }
        db.close();
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TASK_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                        cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                        cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                        cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                        cursor.getString(cursor.getColumnIndex(TASK_STATUS))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return taskList;
    }
    public String getTodayDate() {
        LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        return today.toString();
    }
//    public List<Task> getTodayTasks() {
//        List<Task> taskList = new ArrayList<>();
////        String currentDate=getTodayDate();
//        SQLiteDatabase db = getReadableDatabase();
//        String[] selectionArgs = new String[] { "now" };
//        String selection = DUE_DATE + " = date(?)";
//        Cursor cursor = db.query(TASK_TABLE, null, selection, selectionArgs, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") Task task = new Task(
//                        cursor.getInt(cursor.getColumnIndex(TASK_ID)),
//                        cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
//                        cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
//                        cursor.getString(cursor.getColumnIndex(DUE_DATE)),
//                        cursor.getString(cursor.getColumnIndex(TASK_TIME)),
//                        cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
//                        cursor.getString(cursor.getColumnIndex(TASK_STATUS))
//                );
//                taskList.add(task);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//        db.close();
//        return taskList;
//    }
public List<Task> getTodayTasks() {
    List<Task> taskList = new ArrayList<>();
        String currentDate=getTodayDate();
    SQLiteDatabase db = getReadableDatabase();
    String[] selectionArgs = new String[] {currentDate };
    String selection = DUE_DATE + " =?";
    Cursor cursor = db.query(TASK_TABLE, null, selection, selectionArgs, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
        do {
            @SuppressLint("Range") Task task = new Task(
                    cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                    cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                    cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                    cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                    cursor.getString(cursor.getColumnIndex(TASK_STATUS))
            );
            taskList.add(task);
        } while (cursor.moveToNext());
        cursor.close();
    }
    db.close();
    return taskList;
}
    public List<Task> getDueDateTasks() {
        List<Task> dueDateTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String orderBy = DUE_DATE + " ASC";
        Cursor cursor = db.query(TASK_TABLE, null, null, null, null, null, orderBy);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                        cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                        cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                        cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                        cursor.getString(cursor.getColumnIndex(TASK_STATUS))
                );
                dueDateTasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return dueDateTasks;
    }
        public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_TITLE, task.getTitle());
        values.put(TASK_DESCRIPTION, task.getDescription());
        values.put(DUE_DATE, task.getDueDate());
        values.put(TASK_PRIORITY, task.getPriority());
        values.put(TASK_TIME,task.getTime());
        values.put(TASK_STATUS, task.getStatus());
        db.update(TASK_TABLE, values, TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TASK_TABLE, TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }


    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = TASK_STATUS + " = ? AND "+DUE_DATE+"  = date(?)";
        String[] selectionArgs = {"COMPLETED","now"};
        Cursor cursor = db.query(TASK_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                        cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                        cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                        cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                        cursor.getString(cursor.getColumnIndex(TASK_STATUS))
                );
                completedTasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return completedTasks;
    }


    public List<Task> getPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = TASK_STATUS + " = ? AND "+DUE_DATE+"  = date(?)";
        String[] selectionArgs = {"PENDING","now"};
        Cursor cursor = db.query(TASK_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getInt(cursor.getColumnIndex(TASK_ID)),
                        cursor.getString(cursor.getColumnIndex(TASK_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(DUE_DATE)),
                        cursor.getString(cursor.getColumnIndex(TASK_TIME)),
                        cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)),
                        cursor.getString(cursor.getColumnIndex(TASK_STATUS))
                );
                pendingTasks.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return pendingTasks;
    }

}
