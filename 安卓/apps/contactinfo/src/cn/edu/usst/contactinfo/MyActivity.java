package cn.edu.usst.contactinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cn.edu.usst.contactinfo.domain.ContactInfo;
import cn.edu.usst.contactinfo.utils.ContactsUtils;

import java.util.List;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
public void displayContacts(View v){
    List<ContactInfo> list = ContactsUtils.display(this);

    for(ContactInfo contactInfo :list){
        System.out.println(contactInfo);
    }
}

}
