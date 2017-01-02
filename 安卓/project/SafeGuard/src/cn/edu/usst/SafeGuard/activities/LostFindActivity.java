package cn.edu.usst.SafeGuard.activities;


import android.app.Activity;
import android.os.Bundle;
import cn.edu.usst.SafeGuard.R;

public class LostFindActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.layout_lostfind);
    }
}
