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

    private GridView gv_menus; //������İ�ť

    private int icons[] = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app
            , R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan
            , R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};

    private String names[] = {"�ֻ�����", "ͨѶ��ʿ", "����ܼ�", "���̹���",
            "����ͳ��", "������ɱ", "��������", "�߼�����", "��������"};
    private MyAdapter adapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();//��ʼ������
        initData();//��GridView��������
        initEvent();//��ʼ��������¼�
    }

    private void initEvent() {
        gv_menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //�жϵ����λ��
                switch (position){
                    case 0://�ֻ�����
                       //�Զ���Ի���
                        //�Ƿ����ù�����
                        if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(),MyConstants.PASSWORD,""))){
                            //û�����ù����룬��������Ի���
                            showSettingPassDialog();
                        }else{
                            //��������ĶԻ���
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
     * ��ʾ�Զ����������ĶԻ���
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
        				//��������
        				String passone = et_passone.getText().toString().trim();

        				if ( TextUtils.isEmpty(passone)){
        					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
        					return;
        				}  else {
        					// �����ж�,md5 2�μ���
        					passone = Md5Utils.md5(Md5Utils.md5(passone));
        					//��ȡsp�б�������ģ������ж�
        					if (passone.equals(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))){
        						//һ��
        						//�����ֻ���������
        						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
        						startActivity(intent);
        					} else {
        						//��һ��
        						Toast.makeText(getApplicationContext(), "���벻��ȷ", Toast.LENGTH_SHORT).show();
        						return;
        					}
        					//�رնԻ���
        					dialog.dismiss();
        				}

        			}
        		});
        		bt_cancel.setOnClickListener(new View.OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				dialog.dismiss();//�رնԻ���
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
              dialog.dismiss();//�رնԻ���
            }
        });
        bt_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passone= et_passone.getText().toString().trim();
                String passtwo=et_passtwo.getText().toString().trim();
                if(TextUtils.isEmpty(passone)||TextUtils.isEmpty(passtwo)){
                    Toast.makeText(getApplicationContext(),"���벻��Ϊ��",Toast.LENGTH_SHORT).show();
                }else if(!passone.equals(passtwo)){
                    //���벻һֱ
                    Toast.makeText(getApplicationContext(),"���벻һ��",Toast.LENGTH_SHORT).show();
                }else{
                    //��������
                    Toast.makeText(getApplicationContext(),"���뱣��ɹ�",Toast.LENGTH_SHORT).show();
                    //�������뵽sp��
                    passone= Md5Utils.md5(Md5Utils.md5(passone));//������м��ܣ����Զ�α��롣������ͨ����10�����ϣ�
                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD,passone);
                   dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * ��ʼ�����������
     */
    private void initData() {
        adapter = new MyAdapter();
        gv_menus.setAdapter(adapter);//����gridView��������

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icons.length;//���ص��ǹ�����
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
            //��ȡ���
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
            //��������(ͼƬ������)
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
