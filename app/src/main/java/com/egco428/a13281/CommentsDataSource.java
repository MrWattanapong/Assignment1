package com.egco428.a13281;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommentsDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbhelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT,MySQLiteHelper.COLUMN_IMG,MySQLiteHelper.COLUMN_DATE};

    public CommentsDataSource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        dbhelper.close();
    }

    public Comment createComment(String comment,String img,String date){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_IMG, img);
        values.put(MySQLiteHelper.COLUMN_DATE, date);
        long insertID = database.insert(MySQLiteHelper.TABLE_COMMENTS,null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,allColumns, MySQLiteHelper.COLUMN_ID+ " = " +insertID,null,null,null,null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID+ " = "+id,null);
    }

    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        return comments;
    }

    public Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        comment.setImg(cursor.getString(2));
        comment.setDate(cursor.getString(3));
        return comment;
    }
}
