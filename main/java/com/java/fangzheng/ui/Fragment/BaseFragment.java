package com.java.fangzheng.ui.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void navigateToWithBundle(Class cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
