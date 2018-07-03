package com.example.administrator.ydxcgld.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ydxcgld.R;

public class JishiActivity extends BaseActivity {

    private ImageView iv_back;
    private TextView tv_liebiao;
    private TextView tv_weizhi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jishi);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_jishi_back);
        tv_liebiao = (TextView) findViewById(R.id.tv_jishi_jishiliebiao);
        tv_weizhi = (TextView) findViewById(R.id.tv_jishi_jishiweizhi);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_liebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JishiActivity.this,JishiguanliActivity.class);
                startActivity(intent);
            }
        });

        tv_weizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JishiActivity.this,JishiweizhiActivity.class);
                startActivity(intent);
            }
        });


    }
}
