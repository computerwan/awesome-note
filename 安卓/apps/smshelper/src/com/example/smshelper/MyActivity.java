package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

public class MyActivity extends Activity {
    String msg = null;
    private EditText sms_body;
    private EditText ed_contact;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sms_body = (EditText) findViewById(R.id.sms_body);
        ed_contact = (EditText) findViewById(R.id.ed_contact);
    }

    public void selectMsg(View v) {
        //ͨ����ʽ��ͼֱ�Ӽ���smsActivity
        Intent intent = new Intent();
        intent.setClass(this, SmsAcitivityList.class);
        //**���������ķ��أ�startActivity(intent);�ǿ���һ���µ� myActivity
        startActivityForResult(intent, 1);

    }

    public void selectContact(View v) {
        Intent intent = new Intent();
        intent.setClass(this, ContactListActivity.class);
        startActivityForResult(intent, 2);
    }
    public void sendMsg(View v){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(ed_contact.getText().toString(),null,sms_body.getText().toString(),null,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (data != null) {
                msg = data.getStringExtra("msg");
                sms_body.setText(msg);
            }
            System.out.println("������SmsAcitivityList");
        } else if (requestCode == 2) {
            if (data != null) {
                String contact = data.getStringExtra("contact");
                ed_contact.setText(contact);
            }
            System.out.println("������contactListActivity");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
