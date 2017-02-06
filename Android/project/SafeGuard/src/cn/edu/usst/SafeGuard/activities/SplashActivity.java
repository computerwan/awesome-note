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

    private static final int LOADMAIN = 1;//加载主界面
    private static final int SHOWUPDATEDIALOG = 0;//弹出更新对话框
    private RelativeLayout rl_root;//界面的根布局组件
    private int versionCode; //版本号
    private String versionName; //版本名
    private TextView tv_versionName; //显示版本名称
    UrlBean bean = new UrlBean();
    private long startTimeMillis;
    private long endTimeMillis;
    private ProgressBar pb_download;//下载版本的进度条


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiData();//初始化数据
        initView();//初始化界面
        initAnimation();//初始化动画
        checkVersion();//检测服务器版本
    }

    private void intiData() {
        //获取自己的版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号
            versionCode = packageInfo.versionCode;
            //获取版本名
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            //异常不会发生。
        }
    }

    private void initView() {
        setContentView(R.layout.layout_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
        //设置textView
        tv_versionName.setText(versionName);
        pb_download = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
    }

    private void initAnimation() {


        //创建AlphaAnimation动画，0.0完全透明，1.0完全显示
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(3000);//设置动画播放时间
        aa.setFillAfter(true);//界面停留在动画结束的状态，不设置默认初始状态
        //——————————————————————
        //创建旋转动画,从0到360度，设置pivot旋转点信息。
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(3000);
        ra.setFillAfter(true);
        //——————————————————————
        //创建比例动画，从小到大,8个参数：x轴从0~1，y轴从0~1，设置pivot旋转点信息。
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(3000);
        sa.setFillAfter(true);
        //——————————————————————
        //创建动画集合
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(ra);
        as.addAnimation(sa);

        //显示动画，给根布局设置动画
        rl_root.startAnimation(as);


    }

    private void checkVersion() {
        //访问服务器，获取数据url,在子线程中访问服务器
        new Thread() {
            @Override
            public void run() {
                BufferedReader reader = null;
                HttpURLConnection conn = null;
                try {
                    //毫秒显示当前的时候
                    startTimeMillis = System.currentTimeMillis();
                    URL url = new URL("http://120.27.95.171/MyJDBC/SafeGuard.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000); //网络连接超时
                    conn.setReadTimeout(5000);//读取数据超时
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream(); //获取字节流
                        reader = new BufferedReader(new InputStreamReader(inputStream));  //将字节流转换为缓冲的字符流
                        String line = reader.readLine();//读取一行信息
                        StringBuilder jsonString = new StringBuilder();
                        while (line != null) {
                            //有数据
                            jsonString.append(line);
                            //继续读取
                            line = reader.readLine();
                        }
                        //解析json数据
                        UrlBean parseJson = parseJson(jsonString);
                        //
                        isNewVersion(parseJson);//是否有新版本
                        System.out.println("版本号" + parseJson.getVersionCode());

                        reader.close();  //关闭输入流
                        conn.disconnect();//断开连接

                    } else {
                        //找不到文件：404错误
                        Toast.makeText(SplashActivity.this, "错误类型：" + code, Toast.LENGTH_SHORT).show();
                        loadMain();
                    }
                } catch (IOException e) {
                    //网络连接不上
                    Toast.makeText(SplashActivity.this, "网络连接不上", Toast.LENGTH_SHORT).show();
                    loadMain();
                    e.printStackTrace();
                }

                super.run();
            }
        }.start();
    }

    //因为在子线程中，无法处理Activity，所以需要用handler来处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理消息
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
     * 显示是否更新新版本的对话框
     */
    private void showUpdateDialog() {
        //对话框的上下文要是Activity.class类型,AlertDialog是Activity的一部分
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //让用户禁用取消操作(防止弹出对话框的时候，直接取消)
        //builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                //取消事件处理，进入主界面
                loadMain();
            }
        });
        builder.setTitle("提醒")
                .setMessage("是否更新新版本?新版本具有如下信息" + bean.getDesc())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("更新主界面");
                        downLoadNewApk();//下载新版本
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //取消，进入主界面
                loadMain();
            }
        });
        builder.show();//显示对话框

    }

    private void loadMain() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();  //关闭自己
    }

    /**
     * 新版本的下载安装
     */
    private void downLoadNewApk() {
        HttpUtils utils = new HttpUtils();
        //网上地址，本地地址
        File file = new File("/storage/emulated/0/safeguard.apk");
        file.delete();//删除原来文件
        utils.download(bean.getUrl(), "/storage/emulated/0/safeguard.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                //已经在主线程执行
                Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                //安装apk
                installApk();
                pb_download.setVisibility(View.GONE);//隐藏记录条
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                pb_download.setVisibility(View.VISIBLE);//设置进度条显示
                pb_download.setMax((int) total); //设置进度条的最大值
                pb_download.setProgress((int) current);//设置当前的进度
                super.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * 安装下载的apk
     */
    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //setData和setType方法不能同时使用
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(new File("/storage/emulated/0/safeguard.apk"));
        intent.setDataAndType(data, type);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果用户取消更新apk，那么就直接进入主界面
        loadMain();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void isNewVersion(UrlBean parseJson) {
        int serverCode = parseJson.getVersionCode(); //获取服务器的版本
        //执行结束时间
        endTimeMillis = System.currentTimeMillis();
        if (endTimeMillis - startTimeMillis < 3000) {
            //设置休眠的时候，保证至少要休眠三秒
            SystemClock.sleep(3000 - (endTimeMillis - startTimeMillis));
        }
        //对比自己的版本
        if (serverCode == versionCode) {
            //版本号一致，进入主界面
            Message msg = Message.obtain();
            msg.what = LOADMAIN;
            handler.sendMessage(msg);
        } else {
            //弹出对话框，显示新版本的描述信息，让用户点击是否更新
            Message msg = Message.obtain();
            msg.what = SHOWUPDATEDIALOG;
            handler.sendMessage(msg);
        }
    }

    /**
     * @param jsonString 从服务器获取的json数据
     * @return url信息的封装对象
     */

    private UrlBean parseJson(StringBuilder jsonString) {
        //解析json数据
        try {
            //把json字符串数据封装成json对象
            JSONObject object = new JSONObject(jsonString + "");
            int version = object.getInt("version");
            String url = object.getString("url");
            String desc = object.getString("desc");
            //将数据封装到bean对象中
            bean.setDesc(desc);
            bean.setUrl(url);
            bean.setVersionCode(version);
        } catch (JSONException e) {
            //文件格式错误
            Toast.makeText(SplashActivity.this, "文件格式错误", Toast.LENGTH_SHORT).show();
            loadMain();
            e.printStackTrace();
        }
        return bean;
    }
}


