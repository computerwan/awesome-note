package cn.edu.usst.loadpic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class MyActivity extends Activity {

    private ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void loadBitmap(View view) {
       // String path = "/storage/emulated/0/1.jpg";
        String path = "/storage/emulated/0/DCIM/Camera/20160301_232613.jpg";

        // 通过手机的屏幕的宽高和图片的宽高来计算采样率

        //1，获取屏幕的宽和高
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        //2，获取图片的宽和高
        try {
            ExifInterface exif = new ExifInterface(path);
            int picHeight = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            int picWidth = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);

            // 3，获取采样率=用图片的宽度/屏幕的宽度
            int widthSample = (int) (picWidth * 1f / screenWidth + 0.5f);// 四舍五入
            int heightSample = (int) (picHeight * 1f / screenHeight + 0.5f);// 四舍五入

            int sample = (int) (Math.sqrt(widthSample * widthSample
                    + heightSample * heightSample) + 0.5f);

            //,3，设置采样率
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=sample;

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
