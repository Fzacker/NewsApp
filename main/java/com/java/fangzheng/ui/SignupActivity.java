package com.java.fangzheng.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.java.fangzheng.R;
import com.java.fangzheng.utils.MyDatabaseHelper;
import com.java.fangzheng.utils.StatusBarUtils;

public class SignupActivity extends AppCompatActivity {
    private MyDatabaseHelper databaseHelper;
    private Button signup_btn;
    private EditText signup_name, signup_password, signup_password_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);

        databaseHelper = new MyDatabaseHelper(this);
        signup_btn = findViewById(R.id.signup_btn);
        signup_name = findViewById(R.id.account);
        signup_password = findViewById(R.id.password);
        signup_password_again = findViewById(R.id.passwordConfirm);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = signup_name.getText().toString();
                String password = signup_password.getText().toString();
                String passwordAgain = signup_password_again.getText().toString();
                if(account.equals("")  || password.equals("") || passwordAgain.equals("")) {
                    Toast.makeText(SignupActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                } else {
                    if(password.equals(passwordAgain)) {
                        Boolean check = checkAccount(account);
                        if(check == false) {
                            Boolean insert = insertData(account, password);
                            if(insert == true) {
                                Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public Boolean insertData(String account, String password) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", account);
        contentValues.put("password", password);
        long result = sqLiteDatabase.insert("User", null, contentValues);
        if(result ==-1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkAccount(String account) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where name = ?", new String[]{account});
        if(cursor.getCount() > 0) {
            return true;
        } else {
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