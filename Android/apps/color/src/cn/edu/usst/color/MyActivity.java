package cn.edu.usst.color;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MyActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private ImageView iv;

    private SeekBar skbRed;
    private SeekBar skbGreen;
    private SeekBar skbBlue;
    private float redPercent = 1;
    private float greenPercent = 1;
    private float bluePercent = 1;

    private Bitmap srcBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        iv = (ImageView) findViewById(R.id.iv);
        skbRed = (SeekBar) findViewById(R.id.skb_red);
        skbGreen = (SeekBar) findViewById(R.id.skb_green);
        skbBlue = (SeekBar) findViewById(R.id.skb_blue);

        //也可以通过new OnSeekBarChangeListener来进行监听
        skbRed.setOnSeekBarChangeListener(this);
        skbGreen.setOnSeekBarChangeListener(this);
        skbBlue.setOnSeekBarChangeListener(this);

        String path = "/storage/emulated/0/1.jpg";
        srcBitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(srcBitmap);
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        // seekbar进度改变时的回调
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // 开始拖动seekbar的回调
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // 停止拖动seekbar的回调
        // seekBar的范围：0-100;
        // 颜色函数的范围：0-2
        int progress = seekBar.getProgress();
        float percent = progress / 50f;// (0-2f)

        if (seekBar == skbRed) {
            this.redPercent = percent;
        } else if (seekBar == skbGreen) {
            this.greenPercent = percent;
        } else if (seekBar == skbBlue) {
            this.bluePercent = percent;
        }
        // 1.获得图片的拷贝
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
                srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Matrix matrix = new Matrix();

        // 2.去处理图片的中的颜色数据
        Paint paint = new Paint();
        // 设置画笔的颜色过滤
        // vector:0-2 0:没有 2：最多
        float[] cm = new float[]{1 * redPercent, 0, 0, 0, 0, // red vector
                0, 1 * greenPercent, 0, 0, 0, // green vector
                0, 0, 1 * bluePercent, 0, 0, // blue vector
                0, 0, 0, 1, 0 // alpha vector
        };
        paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(cm)));
        canvas.drawBitmap(srcBitmap, matrix, paint);

        // 3.将处理的结果展示
        iv.setImageBitmap(copyBitmap);
    }
}
