package cn.edu.usst.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        manager=getFragmentManager();

        //获取事务,默认在网络界面
        transaction = manager.beginTransaction();
        NetworkFragment nf =new NetworkFragment();
        transaction.replace(R.id.container,nf);
        transaction.commit();
    }

    //sound
    public void dispaly(View v){
        transaction = manager.beginTransaction();
        DisplayFragment df =new DisplayFragment();
        transaction.replace(R.id.container,df);
        transaction.commit();
    }

    //sound
    public void general(View v){
        transaction = manager.beginTransaction();
        GeneralFragment gf =new GeneralFragment();
        transaction.replace(R.id.container,gf);
        transaction.commit();
    }
    //sound
    public void network(View v){
        transaction = manager.beginTransaction();
        NetworkFragment nf =new NetworkFragment();
        transaction.replace(R.id.container,nf);
        transaction.commit();
    }
    //sound
    public void sound(View v){
        transaction = manager.beginTransaction();
        SoundFragment sf =new SoundFragment();
        transaction.replace(R.id.container,sf);
        transaction.commit();
    }
}
