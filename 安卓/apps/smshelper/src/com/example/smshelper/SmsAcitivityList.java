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
    String[] msgs = {"��һ�����Զ���", "�ڶ������Զ���", "���������Զ���", "���������Զ���", "���������Զ���"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smslist);
        lv = (ListView) findViewById(R.id.lv);
        //����һ��������
        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.smsitem,msgs));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("��"+(i+1)+"����Ŀ�������");
                String msg =msgs[i];
                Intent intent = new Intent();
                intent.putExtra("msg",msg);
                //��Ϊ��ʵ�ڲ��࣬����Ҫ��SmsAcitivityList.this
                //intent.setClass(SmsAcitivityList.this,MyActivity.class);

                //ʹ��setResult֮����ô�ͻ�ص�������ǰActivity���ڵ�onActivityResult(ǰ����MyActivityʹ�õ���startActivityForResult)
                setResult(0,intent);
               //������ǰ��sms
                finish();

            }

        });
    }
}

