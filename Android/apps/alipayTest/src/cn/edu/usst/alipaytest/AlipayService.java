package cn.edu.usst.alipaytest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AlipayService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new AlipayAgent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("alipay service create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("alipay service destory");
    }

    public int pay(String account,float money){
        System.out.println("alipay account finished");
        return 1;
    }
    private class AlipayAgent extends IAlipayService.Stub{

        public int callPayInService(String account, float money) throws RemoteException {
            int result = pay(account, money);
            return result;
        }
    }
}
