package cn.edu.usst.bank;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BankDBOpenHelper helper = new BankDBOpenHelper(this);
      	SQLiteDatabase db = helper.getWritableDatabase();
    }
}
