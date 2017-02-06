package com.example.myapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLOutput;

import static android.widget.AdapterView.*;

public class SmsAcitivityList extends Activity {

    ListView lv = null;
    String[] msgs = {"第一条测试短信", "第二条测试短信", "第三条测试短信", "第四条测试短信", "第五条测试短信"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smslist);
        lv = (ListView) findViewById(R.id.lv);
        //设置一个适配器
        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.smsitem,msgs));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("第"+(i+1)+"个项目被点击了");
                String msg =msgs[i];
                Intent intent = new Intent();
                intent.putExtra("msg",msg);
                //因为其实内部类，所以要加SmsAcitivityList.this
                //intent.setClass(SmsAcitivityList.this,MyActivity.class);

                //使用setResult之后，那么就会回到启动当前Activity所在的onActivityResult(前提是MyActivity使用的是startActivityForResult)
                setResult(0,intent);
               //结束当前的sms
                finish();

            }

        });
    }
}

