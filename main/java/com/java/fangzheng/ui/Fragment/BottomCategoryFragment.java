package com.java.fangzheng.ui.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.fangzheng.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomCategoryFragment extends BottomSheetDialogFragment {
    private TagFlowLayout mFlowLayoutOn;
    private TagFlowLayout mFlowLayoutOff;
    private ImageButton back_btn;
    private TextView tvHint;
    private List<String> mTagsOn = new ArrayList<>();
    private List<String> mTagsOff = new ArrayList<>();
    private DataChangeListener mDataChangeListener;

    public interface DataChangeListener {
        void onDataChange(List<String> tagsOn, List<String> tagsOff);
    }
    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListener = dataChangeListener;
    }


    public static BottomCategoryFragment newInstance(List<String> tagsOn, List<String> tagsOff) {
        BottomCategoryFragment fragment = new BottomCategoryFragment();
        fragment.mTagsOn = tagsOn;
        fragment.mTagsOff = tagsOff;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_category, container, false);
        back_btn = v.findViewById(R.id.back_btn);
        tvHint = v.findViewById(R.id.tvHint);
        mFlowLayoutOn = (TagFlowLayout) v.findViewById(R.id.flowlayoutOn);
        mFlowLayoutOff = (TagFlowLayout) v.findViewById(R.id.flowlayoutOff);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CategoryAdapter categoryAdapterOn = new CategoryAdapter(mTagsOn);
        CategoryAdapter categoryAdapterOff = new CategoryAdapter(mTagsOff);
        mFlowLayoutOn.setAdapter(categoryAdapterOn);
        mFlowLayoutOff.setAdapter(categoryAdapterOff);
        if (mTagsOff.size() == 0) {
            tvHint.setText("已全部添加到我的频道");
        } else {
            tvHint.setText("");
        }

        mFlowLayoutOn.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                if(position == 0)
                    return true;
                mTagsOff.add(mTagsOn.get(position));
                mTagsOn.remove(position);
                categoryAdapterOn.notifyDataChanged();
                categoryAdapterOff.notifyDataChanged();
                if (mTagsOff.size() == 0) {
                    tvHint.setText("已全部添加到我的频道");
                } else {
                    tvHint.setText("");
                }
                return true;
            }
        });

        mFlowLayoutOff.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                mTagsOn.add(mTagsOff.get(position));
                mTagsOff.remove(position);
                categoryAdapterOn.notifyDataChanged();
                categoryAdapterOff.notifyDataChanged();
                if (mTagsOff.size() == 0) {
                    tvHint.setText("已全部添加到我的频道");
                } else {
                    tvHint.setText("");
                }
                return true;
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void onDestroy() {
        mDataChangeListener.onDataChange(mTagsOn, mTagsOff);
        super.onDestroy();
    }
    private int getPixelsFromDp(float dpValue){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private class CategoryAdapter extends TagAdapter<String> {

        public CategoryAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, String s) {
            TextView tv = new TextView(getActivity());
            tv.setText(s);
            tv.setWidth(getPixelsFromDp(70));
            tv.setHeight(getPixelsFromDp(40));
            tv.setGravity(Gravity.CENTER);
            tv.setBackground(getResources().getDrawable(R.drawable.tag_background));
            tv.setTextSize(15);
            if(s == "推荐") {
                tv.setTextColor(Color.GRAY);
            } else {
                tv.setTextColor(Color.BLACK);
            }
            return tv;
        }

    }

}