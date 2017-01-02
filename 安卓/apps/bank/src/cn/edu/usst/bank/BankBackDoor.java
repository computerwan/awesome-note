package cn.edu.usst.bank;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class BankBackDoor extends ContentProvider{
    private static final int SUCCESS = 1;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    //只有满足条件，才能访问后台程序。
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
   	static{
        //数据库是由BankDBOPenHelpter文件创建
        //访问后台要输入：content://cn.edu.usst.db/accounts才能访问。
   		matcher.addURI("cn.edu.usst.db", "accounts", SUCCESS);
   	}


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int result =matcher.match(uri);
        if(SUCCESS==result){
            System.out.println("成功使用后台");
        }else{
            throw  new RuntimeException("输入错误");
        }


        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
