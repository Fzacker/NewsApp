package com.java.fangzheng.ui.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.java.fangzheng.R;
import com.java.fangzheng.ui.CollectionActivity;
import com.java.fangzheng.ui.HistoryActivity;
import com.java.fangzheng.ui.LoginActivity;
import com.java.fangzheng.utils.AlbumUtil;
import com.java.fangzheng.utils.MyDatabaseHelper;
import com.java.fangzheng.utils.SharedPreUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProfileFragment extends BaseFragment {
    private ShapeableImageView change_btn;
    private TextView tv_username;
    private CardView collection, history;
    private static final int CHOOSE_PHOTO = 1;

    private MyDatabaseHelper dbHelper;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        change_btn = v.findViewById(R.id.btn_change);
        tv_username = v.findViewById(R.id.tv_username);
        collection = v.findViewById(R.id.collection);
        history = v.findViewById(R.id.history);
        dbHelper = new MyDatabaseHelper(getContext());
        String username = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
        try {
            String path = getContext().getCacheDir().getPath();
            String fileName = "user_head";
            File file = new File(path, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                change_btn.setImageBitmap(round);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!username.equals("")) {
            tv_username.setText(username);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectionActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "权限获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == -1) {
                    String imgPath = AlbumUtil.handleImageOnKitKat(getContext(), data);
                    setHead(imgPath);
                }
                break;
            default:
                break;
        }
    }
    private void setHead(String imgPath) {
        if (imgPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
            try {
                String path = getContext().getCacheDir().getPath();
                File file = new File(path, "user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            change_btn.setImageBitmap(round);
        } else {
            Toast.makeText(getContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}