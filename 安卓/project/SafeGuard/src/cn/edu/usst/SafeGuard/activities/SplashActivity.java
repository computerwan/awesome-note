package cn.edu.usst.SafeGuard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.*;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.usst.SafeGuard.R;
import cn.edu.usst.SafeGuard.domain.UrlBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends Activity {

    private static final int LOADMAIN = 1;//����������
    private static final int SHOWUPDATEDIALOG = 0;//�������¶Ի���
    private RelativeLayout rl_root;//����ĸ��������
    private int versionCode; //�汾��
    private String versionName; //�汾��
    private TextView tv_versionName; //��ʾ�汾����
    UrlBean bean = new UrlBean();
    private long startTimeMillis;
    private long endTimeMillis;
    private ProgressBar pb_download;//���ذ汾�Ľ�����


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiData();//��ʼ������
        initView();//��ʼ������
        initAnimation();//��ʼ������
        checkVersion();//���������汾
    }

    private void intiData() {
        //��ȡ�Լ��İ汾��Ϣ
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //��ȡ�汾��
            versionCode = packageInfo.versionCode;
            //��ȡ�汾��
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            //�쳣���ᷢ����
        }
    }

    private void initView() {
        setContentView(R.layout.layout_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
        //����textView
        tv_versionName.setText(versionName);
        pb_download = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
    }

    private void initAnimation() {


        //����AlphaAnimation������0.0��ȫ͸����1.0��ȫ��ʾ
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(3000);//���ö�������ʱ��
        aa.setFillAfter(true);//����ͣ���ڶ���������״̬��������Ĭ�ϳ�ʼ״̬
        //��������������������������������������������
        //������ת����,��0��360�ȣ�����pivot��ת����Ϣ��
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(3000);
        ra.setFillAfter(true);
        //��������������������������������������������
        //����������������С����,8��������x���0~1��y���0~1������pivot��ת����Ϣ��
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(3000);
        sa.setFillAfter(true);
        //��������������������������������������������
        //������������
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(ra);
        as.addAnimation(sa);

        //��ʾ�����������������ö���
        rl_root.startAnimation(as);


    }

    private void checkVersion() {
        //���ʷ���������ȡ����url,�����߳��з��ʷ�����
        new Thread() {
            @Override
            public void run() {
                BufferedReader reader = null;
                HttpURLConnection conn = null;
                try {
                    //������ʾ��ǰ��ʱ��
                    startTimeMillis = System.currentTimeMillis();
                    URL url = new URL("http://120.27.95.171/MyJDBC/SafeGuard.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000); //�������ӳ�ʱ
                    conn.setReadTimeout(5000);//��ȡ���ݳ�ʱ
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream(); //��ȡ�ֽ���
                        reader = new BufferedReader(new InputStreamReader(inputStream));  //���ֽ���ת��Ϊ������ַ���
                        String line = reader.readLine();//��ȡһ����Ϣ
                        StringBuilder jsonString = new StringBuilder();
                        while (line != null) {
                            //������
                            jsonString.append(line);
                            //������ȡ
                            line = reader.readLine();
                        }
                        //����json����
                        UrlBean parseJson = parseJson(jsonString);
                        //
                        isNewVersion(parseJson);//�Ƿ����°汾
                        System.out.println("�汾��" + parseJson.getVersionCode());

                        reader.close();  //�ر�������
                        conn.disconnect();//�Ͽ�����

                    } else {
                        //�Ҳ����ļ���404����
                        Toast.makeText(SplashActivity.this, "�������ͣ�" + code, Toast.LENGTH_SHORT).show();
                        loadMain();
                    }
                } catch (IOException e) {
                    //�������Ӳ���
                    Toast.makeText(SplashActivity.this, "�������Ӳ���", Toast.LENGTH_SHORT).show();
                    loadMain();
                    e.printStackTrace();
                }

                super.run();
            }
        }.start();
    }

    //��Ϊ�����߳��У��޷�����Activity��������Ҫ��handler������
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //������Ϣ
            switch (msg.what) {
                case LOADMAIN:
                    loadMain();
                    break;
                case SHOWUPDATEDIALOG:
                    showUpdateDialog();
                    break;
            }
        }
    };

    /**
     * ��ʾ�Ƿ�����°汾�ĶԻ���
     */
    private void showUpdateDialog() {
        //�Ի����������Ҫ��Activity.class����,AlertDialog��Activity��һ����
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //���û�����ȡ������(��ֹ�����Ի����ʱ��ֱ��ȡ��)
        //builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                //ȡ���¼���������������
                loadMain();
            }
        });
        builder.setTitle("����")
                .setMessage("�Ƿ�����°汾?�°汾����������Ϣ" + bean.getDesc())
                .setPositiveButton("����", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("����������");
                        downLoadNewApk();//�����°汾
                    }
                }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //ȡ��������������
                loadMain();
            }
        });
        builder.show();//��ʾ�Ի���

    }

    private void loadMain() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();  //�ر��Լ�
    }

    /**
     * �°汾�����ذ�װ
     */
    private void downLoadNewApk() {
        HttpUtils utils = new HttpUtils();
        //���ϵ�ַ�����ص�ַ
        File file = new File("/storage/emulated/0/safeguard.apk");
        file.delete();//ɾ��ԭ���ļ�
        utils.download(bean.getUrl(), "/storage/emulated/0/safeguard.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                //�Ѿ������߳�ִ��
                Toast.makeText(getApplicationContext(), "���سɹ�", Toast.LENGTH_SHORT).show();
                //��װapk
                installApk();
                pb_download.setVisibility(View.GONE);//���ؼ�¼��
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                pb_download.setVisibility(View.VISIBLE);//���ý�������ʾ
                pb_download.setMax((int) total); //���ý����������ֵ
                pb_download.setProgress((int) current);//���õ�ǰ�Ľ���
                super.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * ��װ���ص�apk
     */
    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //setData��setType��������ͬʱʹ��
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(new File("/storage/emulated/0/safeguard.apk"));
        intent.setDataAndType(data, type);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //����û�ȡ������apk����ô��ֱ�ӽ���������
        loadMain();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void isNewVersion(UrlBean parseJson) {
        int serverCode = parseJson.getVersionCode(); //��ȡ�������İ汾
        //ִ�н���ʱ��
        endTimeMillis = System.currentTimeMillis();
        if (endTimeMillis - startTimeMillis < 3000) {
            //�������ߵ�ʱ�򣬱�֤����Ҫ��������
            SystemClock.sleep(3000 - (endTimeMillis - startTimeMillis));
        }
        //�Ա��Լ��İ汾
        if (serverCode == versionCode) {
            //�汾��һ�£�����������
            Message msg = Message.obtain();
            msg.what = LOADMAIN;
            handler.sendMessage(msg);
        } else {
            //�����Ի�����ʾ�°汾��������Ϣ�����û�����Ƿ����
            Message msg = Message.obtain();
            msg.what = SHOWUPDATEDIALOG;
            handler.sendMessage(msg);
        }
    }

    /**
     * @param jsonString �ӷ�������ȡ��json����
     * @return url��Ϣ�ķ�װ����
     */

    private UrlBean parseJson(StringBuilder jsonString) {
        //����json����
        try {
            //��json�ַ������ݷ�װ��json����
            JSONObject object = new JSONObject(jsonString + "");
            int version = object.getInt("version");
            String url = object.getString("url");
            String desc = object.getString("desc");
            //�����ݷ�װ��bean������
            bean.setDesc(desc);
            bean.setUrl(url);
            bean.setVersionCode(version);
        } catch (JSONException e) {
            //�ļ���ʽ����
            Toast.makeText(SplashActivity.this, "�ļ���ʽ����", Toast.LENGTH_SHORT).show();
            loadMain();
            e.printStackTrace();
        }
        return bean;
    }
}


