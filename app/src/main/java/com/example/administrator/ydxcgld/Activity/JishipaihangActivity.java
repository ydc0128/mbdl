package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Adapter.JishipaihangAdapter;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Bean.Paihang;
import com.example.administrator.ydxcgld.Contast.Contast;
import com.example.administrator.ydxcgld.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JishipaihangActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "JishipaihangActivity";

    private String url = Contast.Domain + "api/AgentWorkersOnline.ashx?";


    private ImageView iv_back;

    private RadioButton rb_all;
    private RadioButton rb_day;
    private RadioButton rb_mouth;

    private View line_all;
    private View line_day;
    private View line_mouth;


    private ListView listView;
    private JishipaihangAdapter adapter;
    private List<Paihang> paihangList;

    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jishipaihang);
        Intent intent = getIntent();
        type = intent.getStringExtra("from");
        initViews();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(type.equals("all")){
            rb_all.setChecked(true);
        }else if(type.equals("today")){
            rb_day.setChecked(true);
        }else if(type.equals("month")){
            rb_mouth.setChecked(true);
        }
        refelash(type);

    }

    private void refelash(String type) {
        final ProgressDialog pd = new ProgressDialog(JishipaihangActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        final FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("AG_Tel", Contast.daili.getAG_Tel());
        params.add("AG_IMEI", Contast.daili.getAG_IMEI());
        params.add("type", type);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JishipaihangActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    Log.i(TAG, "onResponse: " + string);
                    if (string.contains("ErrorStr")) {
                        final Error error = JSON.parseObject(string, Error.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(JishipaihangActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                paihangList = JSON.parseArray(string, Paihang.class);
                                adapter.setData(paihangList);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(JishipaihangActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_jishipaihang_listview);
        paihangList = new ArrayList<>();
        adapter = new JishipaihangAdapter(this, paihangList);
        listView.setAdapter(adapter);
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_jishipaihang_back);

        rb_all = (RadioButton) findViewById(R.id.rb_jishipaihang_all);
        rb_day = (RadioButton) findViewById(R.id.rb_jishipaihang_day);
        rb_mouth = (RadioButton) findViewById(R.id.rb_jishipaihang_mouth);

        line_all = findViewById(R.id.line_jishipaihang_all);
        line_day = findViewById(R.id.line_jishipaihang_day);
        line_mouth = findViewById(R.id.line_jishipaihang_mouth);

        rb_all.setOnCheckedChangeListener(this);
        rb_day.setOnCheckedChangeListener(this);
        rb_mouth.setOnCheckedChangeListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_jishipaihang_all:
                if (isChecked) {
                    type = "all";
                    refelash(type);
                    line_all.setVisibility(View.VISIBLE);
                    rb_day.setChecked(false);
                    rb_mouth.setChecked(false);
                    line_day.setVisibility(View.INVISIBLE);
                    line_mouth.setVisibility(View.INVISIBLE);
                } else {
                    line_all.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_jishipaihang_day:
                if (isChecked) {
                    type = "today";
                    refelash(type);
                    line_day.setVisibility(View.VISIBLE);
                    rb_all.setChecked(false);
                    rb_mouth.setChecked(false);
                    line_all.setVisibility(View.INVISIBLE);
                    line_mouth.setVisibility(View.INVISIBLE);
                } else {
                    line_day.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_jishipaihang_mouth:
                if (isChecked) {
                    type = "month";
                    refelash(type);
                    line_mouth.setVisibility(View.VISIBLE);
                    rb_day.setChecked(false);
                    rb_all.setChecked(false);
                    line_day.setVisibility(View.INVISIBLE);
                    line_all.setVisibility(View.INVISIBLE);
                } else {
                    line_mouth.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
