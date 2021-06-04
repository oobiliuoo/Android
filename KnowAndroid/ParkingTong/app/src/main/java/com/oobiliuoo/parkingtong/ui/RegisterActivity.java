package com.oobiliuoo.parkingtong.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.UsersInfo;
import com.oobiliuoo.parkingtong.net.TcpClient;
import com.oobiliuoo.parkingtong.utils.Utils;

import org.litepal.LitePal;

public class RegisterActivity extends AppCompatActivity {

    /** 电话长度 */
    private static final int TEL_LENGTH = 11;
    /** 密码最小长度 */
    private static final int MIN_PWD_LENGTH = 6;


    private Button btnRegister;
    private EditText etTel;
    private EditText etName;
    private EditText etPwd;
    private EditText etPwd2;
    private RadioGroup rgGender;

    /** 保存用户输入*/
    private String[] userInfo;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userInfo = new String[4];

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    // 连接成功
                    case 1:
                        break;
                    // 连接失败
                    case 2:
                        Utils.showToast(RegisterActivity.this, "服务器连接失败，请检查网络设置");
                        break;
                    // 接收结果
                    case 3:
                        handMsg(msg.obj.toString());
                        break;
                    default:
                        break;
                }

            }
        };


        initView();
    }

    /**
     *  处理服务器响应
     *  string: 服务器传回的信息
     * */
    private void handMsg(String string) {
        if(Utils.RESPOND_REGISTER_OK.equals(string)){
            Utils.showToast(RegisterActivity.this,"注册成功" + string);
            // 将信息保存到本地数据库
            saveData();
            finish();
        }
    }

    /**
     *  初始化界面控件
     * */
    private void initView() {
        etTel = findViewById(R.id.register_et_tel);
        etName= findViewById(R.id.register_et_name);
        etPwd = findViewById(R.id.register_et_pwd);
        etPwd2 = findViewById(R.id.register_et_pwd2);
        rgGender = findViewById(R.id.register_rg_gender);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.register_rb_man:
                        userInfo[2] = "男";
                        break;
                    case R.id.register_rb_woman:
                        userInfo[2] = "女";
                        break;
                    default:
                        break;
                }
            }
        });


        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInfo[0] = etTel.getText().toString();
                userInfo[1] = etName.getText().toString();
                userInfo[3] = etPwd.getText().toString();
                // 检查输入格式是否正确
                if(!check()){
                    return;
                }
                /*
                // 将信息发送至远程服务器
                TcpClient tcpClient = new TcpClient(mHandler,Utils.IP_ADDRESS,Utils.IP_PORT);
                // 请求信息
                String str = Utils.REQUEST_REGISTER + userInfo[0] +Utils.DIVISION
                        + userInfo[1] + Utils.DIVISION + userInfo[2] + Utils.DIVISION
                        + userInfo[3];
                tcpClient.setSendMsg(str);
                tcpClient.send();
                Utils.showToast(RegisterActivity.this,"注册成功");

                 */

                Utils.sendMessage(mHandler,3,Utils.RESPOND_REGISTER_OK);
            }
        });
    }

    /**
     *  将用户信息保存到litePal数据库
     * */
    private void saveData() {
        // 首次执行需要下面语句创建数据库
        SQLiteDatabase db = LitePal.getDatabase();

        UsersInfo user = new UsersInfo();
        user.setTel(userInfo[0]);
        user.setName(userInfo[1]);
        user.setGender(userInfo[2]);
        user.setPwd(userInfo[3]);
        user.save();
        Utils.mLog1("DataBase","save ok "+user.getTel());
    }

    /**
     *  检查输入格式是否正确
     *
     * */
    private boolean check() {
        // 判断电话长度
        if(userInfo[0].length()!=TEL_LENGTH ){
            Utils.showToast(RegisterActivity.this,"电话输入有误");
            return false;
        }
        // 判断电话是否为纯数字
        if(!Utils.isNumeric(etTel.getText().toString())){
            Utils.showToast(RegisterActivity.this,"电话输入有误");
            return false;
        }
        // 判断密码格式
        if( userInfo[3].length()<MIN_PWD_LENGTH || etPwd2.getText().toString().length()<MIN_PWD_LENGTH ){
            Utils.showToast(RegisterActivity.this,"密码输入有误");
            return false;
        }
        // 判断两次密码是否相等
        String pwd = etPwd.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        if(!pwd.equals(pwd2)){
            Utils.showToast(RegisterActivity.this,"密码不匹配");
            return false;
        }

        return true;
    }
}