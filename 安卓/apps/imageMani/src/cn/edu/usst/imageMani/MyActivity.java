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
        //��ʾԭͼ
        Bitmap srcBitmap = BitmapFactory.decodeFile(path);
        ivSrc.setImageBitmap(srcBitmap);

        //1��׼����ֽ��Bitmap��������ߣ�������Ϣ��
        Bitmap copybitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        //2��׼������
        Canvas canvas = new Canvas(copybitmap);
        //3,׼������
        Paint paint = new Paint();
        //4,����ͼƬ������һ������
        Matrix matrix = new Matrix();
        //ͼƬ���ţ�����ͬ
        //matrix.setScale(0.6f, 0.8f);


        //5,��ͼƬ���չ��򻭵�������
        canvas.drawBitmap(srcBitmap,matrix,paint);

        //��������ʾ��ImageView��
        ivDest.setImageBitmap(copybitmap);
    }
}
