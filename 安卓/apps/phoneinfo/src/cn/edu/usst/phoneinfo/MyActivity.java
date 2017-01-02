package cn.edu.usst.phoneinfo;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void displayContacts(View v){
       //��ȡ��ϵ����Ϣ raw_contact,data
        //Provider��authorities��com.android.contacts
        //Provider�İ��ţ�raw_contacts ; data

        Uri contact_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");

        ContentResolver resolver = getContentResolver();

        //select contact_id from raw_contact;
        Cursor contacts_cursor = resolver.query(contact_uri, new String[]{"contact_id"}, null, null, null);
        while(contacts_cursor.moveToNext()){
            String id = contacts_cursor.getString(0);

            //����idȥ��ѯdata��ֻҪid��ͬ��data�����ݣ�����ͬһ����ϵ�˵���Ϣ
            Cursor dataCursor = resolver.query(data_uri, new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{id}, null);
            while(dataCursor.moveToNext()){
                String data = dataCursor.getString(0);
                String type =dataCursor.getString(1);
                System.out.println("data:"+data);
                System.out.println("type:"+type);

            }
            dataCursor.close();
            System.out.println("======================");
        }
        contacts_cursor.close();

    }
}
