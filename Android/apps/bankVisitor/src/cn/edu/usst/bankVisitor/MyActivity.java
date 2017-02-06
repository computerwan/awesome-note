package cn.edu.usst.bankVisitor;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void add(View v) {
        ContentResolver resolver = getContentResolver();

        Uri uri = Uri.parse("content://cn.edu.usst.db/accounts");
        ContentValues values = new ContentValues();

        resolver.insert(uri, values);
    }
}
