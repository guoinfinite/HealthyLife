package com.example.healthylife.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthylife.R;
import com.example.healthylife.fragment.SportFragment;


/**
 * 测试界面
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //添加SportFragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_launch", false);
        SportFragment sportFragment = new SportFragment();
        sportFragment.setArguments(bundle);
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frag, sportFragment).
                commit();
    }
}
