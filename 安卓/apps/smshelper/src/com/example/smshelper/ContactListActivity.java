package com.example.myapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.widget.AdapterView.*;

public class ContactListActivity extends Activity {
    String[] contacts = {"13312345678", "13388888888", "13388888887", "13388888868"};
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactslist);
        lv = (ListView) findViewById(R.id.contacts_lv);
        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.contacts_item,contacts));

        //¼àÌýÆ÷
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String contact = contacts[i];
                Intent intent = new Intent();
                intent.putExtra("contact",contact);
                setResult(0,intent);
                finish();
            }
        });

    }
}
