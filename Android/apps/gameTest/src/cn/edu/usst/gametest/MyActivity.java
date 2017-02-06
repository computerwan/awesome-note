package cn.edu.usst.gametest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import cn.edu.usst.alipaytest.IAlipayService;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void bindService(View v) {
        Intent intent = new Intent();
        intent.setAction("cn.edu.usst.alipay");
        bindService(intent, new MyConnection(), BIND_AUTO_CREATE);
    }

    private IAlipayService agent;

    private class MyConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            agent = IAlipayService.Stub.asInterface(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    public void call(View v) {
        try {
            int result = agent.callPayInService("aaa@qq.com", 100);
            System.out.println(result );

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
