package cn.edu.usst.imageMani;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyActivity extends Activity {
    private ImageView ivSrc;
    private ImageView ivDest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ivSrc = (ImageView) findViewById(R.id.iv_src);
        ivDest = (ImageView) findViewById(R.id.iv_dest);
    }

    public void opts(View v) {
        String path = "/storage/emulated/0/1.jpg";
        //显示原图
        Bitmap srcBitmap = BitmapFactory.decodeFile(path);
        ivSrc.setImageBitmap(srcBitmap);

        //1，准备画纸（Bitmap，传入宽，高，基本信息）
        Bitmap copybitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        //2，准备画板
        Canvas canvas = new Canvas(copybitmap);
        //3,准备画笔
        Paint paint = new Paint();
        //4,操作图片（按照一定规则）
        Matrix matrix = new Matrix();
        //图片缩放，其他同
        //matrix.setScale(0.6f, 0.8f);


        //5,将图片按照规则画到画板上
        canvas.drawBitmap(srcBitmap,matrix,paint);

        //将数据显示到ImageView上
        ivDest.setImageBitmap(copybitmap);
    }
}
