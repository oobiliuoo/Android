package com.oobiliuoo.parkingtong.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.net.TcpClient;
import com.oobiliuoo.parkingtong.utils.Utils;

/**
 * @author biliu
 */
public class LoginActivity extends AppCompatActivity {

    private static final int MIX_NAME_LENGTH = 9 ;
    private static final int MAX_NAME_LENGTH = 11 ;
    private static final int MIX_PWD_LENGTH = 6 ;


    private EditText etName;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private Handler mHandler;
    private String[] userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfo = new String[2];

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    // 连接成功
                    case 1:
                        break;
                    // 连接失败
                    case 2:
                        Utils.showToast(LoginActivity.this, "服务器连接失败，请检查网络设置");
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

    private void handMsg(String string) {
        if(Utils.RESPOND_ACCESS_LOGIN.equals(string)){
            Utils.showToast(LoginActivity.this,"登录成功");
            Intent intent = new Intent();
            intent.putExtra("login_return","access_login");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private void initView() {
        etName = findViewById(R.id.login_ev_name);
        etPassword = findViewById(R.id.login_ev_pwd);

        btnLogin = findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(LoginActivity.this, "登录");
                userInfo[0] = etName.getText().toString();
                userInfo[1] = etPassword.getText().toString();
                if(userInfo[0].length() > MIX_NAME_LENGTH && userInfo[0].length() < MAX_NAME_LENGTH
                        && userInfo[1].length() > MIX_PWD_LENGTH){
                    if(isNumeric(userInfo[0])){
                        TcpClient mTcpClient = new TcpClient(mHandler, Utils.IP_ADDRESS, Utils.IP_PORT);
                        String message = Utils.REQUEST_LOGIN + userInfo[0] + Utils.DIVISION + userInfo[1];
                        mTcpClient.setSendMsg(message);
                        mTcpClient.send();
                    }else {
                        Utils.showToast(LoginActivity.this,"帐号输入出错");
                    }
                }else {
                    Utils.showToast(LoginActivity.this,"帐号或密码输入错误");
                }
            }
        });

        tvRegister = findViewById(R.id.login_tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(LoginActivity.this, "注册");
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private boolean isNumeric(String str) {
        for(int i =0;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }


}