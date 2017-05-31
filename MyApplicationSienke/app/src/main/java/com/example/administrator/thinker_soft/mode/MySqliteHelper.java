package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 对于抽象类SQLiteOpenHelper的继承，需要重写：1）constructor，2）onCreate()和onUpgrade()
 * Created by Administrator on 2017/3/20 0020.
 */
public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="SiEnKe.db";//数据库名称

    //用户表
    final String CREATE_TABLE_SQL_USER = "CREATE TABLE User" +
            "(user_id integer primary key AUTOINCREMENT,securityNumber varchar(200),userName varchar(200),meterNumber varchar(200),userPhone varchar(200)," +
            "securityType varchar(200),oldUserId varchar(200),newUserId varchar(200),userAddress varchar(200),taskId varchar(200),ifChecked varchar(200)," +
            "security_content varchar(200),newMeterNumber varchar(200),remarks varchar(200),security_hidden varchar(200),security_hidden_reason varchar(200)," +
            "photoNumber varchar(200),ifUpload varchar(200),currentTime varchar(200),ifPass varchar(200),loginName varchar(200),security_state varchar(200)," +
            "newUserPhone varchar(200),newUserAddress varchar(200))";

    //任务表
    final String CREATE_TABLE_SQL_TASK = "CREATE TABLE Task " +
            "(task_id integer primary key AUTOINCREMENT,taskName varchar(200),taskId varchar(200),securityType varchar(200),totalCount varchar(200),endTime varchar(200),loginName varchar(200),restCount varchar(200))";

    //安检图片表
    final String CREATE_TABLE_SQL_SECURITY_PHOTO_INFO = "CREATE TABLE security_photo " +
            "(id integer primary key AUTOINCREMENT,photoPath varchar(200),securityNumber varchar(200),loginName varchar(200))";

    //安检状态表（安检类型）
    final String CREATE_TABLE_SQL_SECURITY_STATE = "CREATE TABLE SecurityState " +
            "(id integer primary key AUTOINCREMENT,securityId varchar(200),securityName varchar(200))";

    //安全情况表
    final String CREATE_TABLE_SQL_SECURITY_CONTENT = "CREATE TABLE security_content " +
            "(id integer primary key AUTOINCREMENT,securityId varchar(200),securityName varchar(200))";

    //安全隐患类型表
    final String CREATE_TABLE_SQL_SECURITY_HIDDEEN = "CREATE TABLE security_hidden " +
            "(id integer primary key AUTOINCREMENT,n_safety_hidden_id varchar(200),n_safety_hidden_name varchar(200))";

    //安全隐患原因表
    final String CREATE_TABLE_SQL_SECURITY_HIDDEEN_REASON = "CREATE TABLE security_hidden_reason " +
            "(id integer primary key AUTOINCREMENT,n_safety_hidden_reason_id varchar(200),n_safety_hidden_id varchar(200),n_safety_hidden_reason_name varchar(200))";

    //安全信息与照片关联表
    final String CREATE_TABLE_SQL_SECURITY_INFO_PHOTO = "CREATE TABLE security_info_photo " +
            "(id integer primary key AUTOINCREMENT,name varchar(200),chengji varchar(200),loginName varchar(200))";


    //构造器
    public MySqliteHelper(Context context,int version){
        super(context, DATABASE_NAME, null, version);
    }

    public MySqliteHelper(Context context,//上下文
                              String name,//数据库的名字
                              SQLiteDatabase.CursorFactory factory,//游标对象
                              int version) {//版本号
        super(context, name, factory, version);
    }

    public MySqliteHelper(Context context,//上下文
                              String name,//数据库的名字
                              SQLiteDatabase.CursorFactory factory,//游标对象
                              int version,//版本号
                              DatabaseErrorHandler errorHandler) {//异常handler
        super(context, name, factory, version, errorHandler);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL_USER);                           //用户表
        db.execSQL(CREATE_TABLE_SQL_TASK);                           //任务表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_PHOTO_INFO);            //用户安检图片关联表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_STATE);                  //安全状态表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_CONTENT);                //安全情况表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_HIDDEEN);                //安全隐患表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_HIDDEEN_REASON);         //安全隐患原因表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_INFO_PHOTO);            //安全信息与照片关联表
    }

    //SQLiteDatabase 数据库操作类
    //execSQL 直接执行sql语句
    //sqLiteDatabase.execSQL("select table Student where name='王老五'")
    //sqLiteDatabase.execSQL(String str,Object[] objs);//直接执行sql语句，并把里面的？替换成后面的对象
    //                 {"select table Student where name=? and chengji=?",new String[]{"王老五","50"}

    //insert 插入方法 【android封装好的插入数据的方法】
    //update 更新方法 【更新数据的方法，封装好的】
    //query() 查询方法
    //rawQuery 未加工的查询方法
    //delete 删除方法

    //版本更新回调函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
