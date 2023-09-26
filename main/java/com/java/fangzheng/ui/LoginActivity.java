package com.java.fangzheng.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.java.fangzheng.R;
import com.java.fangzheng.utils.MyDatabaseHelper;
import com.java.fangzheng.utils.SharedPreUtil;
import com.java.fangzheng.utils.StatusBarUtils;

public class LoginActivity extends AppCompatActivity {

    private MyDatabaseHelper databaseHelper;
    private Button login_btn;
    private EditText login_name, login_password;
    private TextView signup_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);

        databaseHelper = new MyDatabaseHelper(this);
        login_btn = findViewById(R.id.signup_btn);
        login_name = findViewById(R.id.account);
        login_password = findViewById(R.id.password);
        signup_text = findViewById(R.id.tvHint);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = login_name.getText().toString();
                String password = login_password.getText().toString();
                if(account.equals("")  || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean check_account = checkAccount(account);
                    if(check_account == true) {
                        Boolean check = checkPassword(account, password);
                        if(check == true) {
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, account);

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    public Boolean checkAccount(String account) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where name = ?", new String[]{account});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkPassword(String account, String password) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where name = ? and password = ?", new String[]{account, password});
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}