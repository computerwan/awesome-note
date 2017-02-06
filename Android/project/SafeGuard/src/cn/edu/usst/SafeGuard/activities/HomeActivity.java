package cn.edu.usst.SafeGuard.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.edu.usst.SafeGuard.R;
import cn.edu.usst.SafeGuard.utils.Md5Utils;
import cn.edu.usst.SafeGuard.utils.MyConstants;
import cn.edu.usst.SafeGuard.utils.SpTools;

public class HomeActivity extends Activity {

    private GridView gv_menus; //主界面的按钮

    private int icons[] = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app
            , R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan
            , R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};

    private String names[] = {"手机防盗", "通讯卫士", "软件管家", "进程管理",
            "流量统计", "病毒查杀", "缓存清理", "高级工具", "设置中心"};
    private MyAdapter adapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();//初始化界面
        initData();//给GridView设置数据
        initEvent();//初始化组件的事件
    }

    private void initEvent() {
        gv_menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断点击的位置
                switch (position){
                    case 0://手机防盗
                       //自定义对话框
                        //是否设置过密码
                        if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(),MyConstants.PASSWORD,""))){
                            //没有设置过密码，设置密码对话框
                            showSettingPassDialog();
                        }else{
                            //输入密码的对话框
                            showEnterPassDialog();
                        }
                        break;
                    case 1://
                        break;
                }
            }
        });
    }
    /**
     * 显示自定义输入代码的对话框
     */
    private void showEnterPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        		View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);
        		final EditText et_passone = (EditText) view.findViewById(R.id.et_dialog_enter_password_passone);
        		Button bt_setpass = (Button) view.findViewById(R.id.bt_dialog_enter_password_login);
        		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_enter_password_cancel);

        		builder.setView(view );
        		bt_setpass.setOnClickListener(new View.OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				//设置密码
        				String passone = et_passone.getText().toString().trim();

        				if ( TextUtils.isEmpty(passone)){
        					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        					return;
        				}  else {
        					// 密码判断,md5 2次加密
        					passone = Md5Utils.md5(Md5Utils.md5(passone));
        					//读取sp中保存的密文，进行判断
        					if (passone.equals(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))){
        						//一致
        						//进入手机防盗界面
        						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
        						startActivity(intent);
        					} else {
        						//不一致
        						Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT).show();
        						return;
        					}
        					//关闭对话框
        					dialog.dismiss();
        				}

        			}
        		});
        		bt_cancel.setOnClickListener(new View.OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				dialog.dismiss();//关闭对话框
        			}
        		});
        		dialog = builder.create();
        		dialog.show();
    }

    private void showSettingPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view =View.inflate(getApplicationContext(),R.layout.dialog_setting_password,null);
        final EditText et_passone= (EditText) view.findViewById(R.id.et_dialog_setting_password_passone);
        final EditText et_passtwo= (EditText) view.findViewById(R.id.et_dialog_setting_password_passtwo);
        Button bt_setpass= (Button) view.findViewById(R.id.bt_dialog_setting_password_setpass);
        Button bt_cancel= (Button) view.findViewById(R.id.bt_dialog_setting_password_cancel);
        builder.setView(view);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();//关闭对话框
            }
        });
        bt_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passone= et_passone.getText().toString().trim();
                String passtwo=et_passtwo.getText().toString().trim();
                if(TextUtils.isEmpty(passone)||TextUtils.isEmpty(passtwo)){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!passone.equals(passtwo)){
                    //密码不一直
                    Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    //保存密码
                    Toast.makeText(getApplicationContext(),"密码保存成功",Toast.LENGTH_SHORT).show();
                    //保存密码到sp中
                    passone= Md5Utils.md5(Md5Utils.md5(passone));//对其进行加密，可以多次编译。（银行通常：10次以上）
                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD,passone);
                   dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 初始化组件的数据
     */
    private void initData() {
        adapter = new MyAdapter();
        gv_menus.setAdapter(adapter);//设置gridView的适配器

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icons.length;//返回的是功能数
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
            //获取组件
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
            //设置数据(图片和文字)
            iv_icon.setImageResource(icons[position]);
            tv_name.setText(names[position]);
            return view;
        }
    }

    private void initView() {
        setContentView(R.layout.layout_home);
        gv_menus = (GridView) findViewById(R.id.gv_home_menus);
    }
}
