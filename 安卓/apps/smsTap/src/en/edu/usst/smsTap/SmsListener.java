package en.edu.usst.smsTap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;


public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("已接收到短信");
        Object[] objs = (Object[]) intent.getExtras().get("pdus");

        for (Object obj : objs) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
            String messageBody = message.getMessageBody();
            String sender = message.getOriginatingAddress();

            System.out.println("短信内容：" + messageBody);
            System.out.println("发信人" + sender);

        }

    }



}


/*
 www.grepcode.com搜android.provider.Telephony.SMS_RECEIVED中的Telephony

     public static final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }



 */