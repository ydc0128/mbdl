package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Adapter.DingdanAdapter;
import com.example.administrator.ydxcgld.Bean.Dingdan;
import com.example.administrator.ydxcgld.Bean.Error;
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

public class DingdantongjiActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private String url = Contast.Domain + "api/AgentOrderInfo.ashx?";

    private static final String TAG = "DingdantongjiActivity";

    private ImageView iv_back;
    private Button btn_jishipaihang;
    private RadioButton rb_all;
    private RadioButton rb_day;
    private RadioButton rb_mouth;

    private View line_all;
    private View line_day;
    private View line_mouth;

    private TextView tv_dingdanshu;
    private TextView tv_dingdanjine;
    private TextView tv_haopingshu;

    private String from;
    private String type;

    private ListView listView;
    private DingdanAdapter adapter;
    private List<Dingdan> dingdanList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdantongji);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        initViews();
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_dingdantongji_listview);
        dingdanList = new ArrayList<>();
        adapter = new DingdanAdapter(this,dingdanList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViews();
        refelash(type);
    }

    private void refelash(String type) {
        final ProgressDialog pd = new ProgressDialog(DingdantongjiActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
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
                        Toast.makeText(DingdantongjiActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                pd.dismiss();
                final String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    Log.i(TAG, "onResponse: "+string);
                    if (string.contains("ErrorStr")) {
                        final Error error = JSON.parseObject(string, Error.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DingdantongjiActivity.this,error.getErrorStr() , Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dingdanList = JSON.parseArray(string,Dingdan.class);
                                adapter.setData(dingdanList);
                            }
                        });

                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DingdantongjiActivity.this,"服务器繁忙，请稍后重试...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setViews() {
        if(from.equals("day")){
            rb_day.setChecked(true);
            tv_dingdanshu.setText(""+Contast.day.getNum_order());
            tv_dingdanjine.setText(""+Contast.day.getNum_Price());
            tv_haopingshu.setText(""+Contast.day.getNum_Star());
            type = "today";
        }else if(from.equals("month")){
            rb_mouth.setChecked(true);
            tv_dingdanshu.setText(""+Contast.mouth.getNum_order());
            tv_dingdanjine.setText(""+Contast.mouth.getNum_Price());
            tv_haopingshu.setText(""+Contast.mouth.getNum_Star());
            type = "month";
        }else if(from.equals("all")){
            rb_all.setChecked(true);
            tv_dingdanshu.setText(""+Contast.all.getNum_order());
            tv_dingdanjine.setText(""+Contast.all.getNum_Price());
            tv_haopingshu.setText(""+Contast.all.getNum_Star());
            type = "all";
        }
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_dingdantongji_back);
        btn_jishipaihang = (Button) findViewById(R.id.btn_dingdantongji_more);
        rb_all = (RadioButton) findViewById(R.id.rb_dingdantongji_all);
        rb_day = (RadioButton) findViewById(R.id.rb_dingdantongji_day);
        rb_mouth = (RadioButton) findViewById(R.id.rb_dingdantongji_mouth);

        line_all = findViewById(R.id.line_dingdantongji_all);
        line_day = findViewById(R.id.line_dingdantongji_day);
        line_mouth = findViewById(R.id.line_dingdantongji_mouth);

        tv_dingdanshu = (TextView) findViewById(R.id.tv_dingdantongji_dingdanshu);
        tv_dingdanjine = (TextView) findViewById(R.id.tv_dingdantongji_dingdanjine);
        tv_haopingshu = (TextView) findViewById(R.id.tv_dingdantongji_haopingshu);

        rb_all.setOnCheckedChangeListener(this);
        rb_day.setOnCheckedChangeListener(this);
        rb_mouth.setOnCheckedChangeListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_jishipaihang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DingdantongjiActivity.this,JishipaihangActivity.class);
                if(rb_all.isChecked()){
                    intent.putExtra("from","all");
                }else if(rb_day.isChecked()){
                    intent.putExtra("from","today");
                }else if(rb_mouth.isChecked()){
                    intent.putExtra("from","month");
                }else{
                    intent.putExtra("from","all");
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rb_dingdantongji_all:
                if(isChecked){
                    type = "all";
                    refelash(type);
                    line_all.setVisibility(View.VISIBLE);
                    rb_day.setChecked(false);
                    rb_mouth.setChecked(false);
                    line_day.setVisibility(View.INVISIBLE);
                    line_mouth.setVisibility(View.INVISIBLE);
                    tv_dingdanshu.setText(""+Contast.all.getNum_order());
                    tv_dingdanjine.setText(""+Contast.all.getNum_Price());
                    tv_haopingshu.setText(""+Contast.all.getNum_Star());
                }else{
                    line_all.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_dingdantongji_day:
                if(isChecked){
                    type = "today";
                    refelash(type);
                    line_day.setVisibility(View.VISIBLE);
                    rb_all.setChecked(false);
                    rb_mouth.setChecked(false);
                    line_all.setVisibility(View.INVISIBLE);
                    line_mouth.setVisibility(View.INVISIBLE);
                    tv_dingdanshu.setText(""+Contast.day.getNum_order());
                    tv_dingdanjine.setText(""+Contast.day.getNum_Price());
                    tv_haopingshu.setText(""+Contast.day.getNum_Star());
                }else{
                    line_day.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_dingdantongji_mouth:
                if(isChecked){
                    type = "month";
                    refelash(type);
                    line_mouth.setVisibility(View.VISIBLE);
                    rb_day.setChecked(false);
                    rb_all.setChecked(false);
                    line_day.setVisibility(View.INVISIBLE);
                    line_all.setVisibility(View.INVISIBLE);
                    tv_dingdanshu.setText(""+Contast.mouth.getNum_order());
                    tv_dingdanjine.setText(""+Contast.mouth.getNum_Price());
                    tv_haopingshu.setText(""+Contast.mouth.getNum_Star());
                }else{
                    line_mouth.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
