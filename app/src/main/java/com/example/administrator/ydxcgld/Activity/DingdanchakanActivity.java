package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
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

public class DingdanchakanActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private String url = Contast.Domain + "api/AgentOrderInfo.ashx?";

    private static final String TAG = "DingdanchakanActivity";

    private ImageView iv_back;
    private RadioButton rb_all;
    private RadioButton rb_yiwancheng;
    private RadioButton rb_danjiedan;
    private RadioButton rb_jinxingzhong;

    private View line_all;
    private View line_yiwancheng;
    private View line_danjiedan;
    private View line_jinxingzhong;

    private ListView listView;
    private DingdanAdapter adapter;
    private List<Dingdan> dingdanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdanchakan);
        initViews();
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_dingdanchakan_listview);
        dingdanList = new ArrayList<>();
        adapter = new DingdanAdapter(this,dingdanList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refelash();
    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(DingdanchakanActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("AG_Tel", Contast.daili.getAG_Tel());
        params.add("AG_IMEI", Contast.daili.getAG_IMEI());
        params.add("type", "all");
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
                        Toast.makeText(DingdanchakanActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(DingdanchakanActivity.this,error.getErrorStr() , Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(DingdanchakanActivity.this,"服务器繁忙，请稍后重试...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_dingdanchakan_back);
        rb_all = (RadioButton) findViewById(R.id.rb_dingdanchakan_all);
        rb_yiwancheng = (RadioButton) findViewById(R.id.rb_dingdanchakan_yiwancheng);
        rb_danjiedan = (RadioButton) findViewById(R.id.rb_dingdanchakan_danjiedan);
        rb_jinxingzhong = (RadioButton) findViewById(R.id.rb_dingdanchakan_jinxingzhong);
        line_all = findViewById(R.id.line_dingdanchakan_all);
        line_yiwancheng = findViewById(R.id.line_dingdanchakan_yiwancheng);
        line_danjiedan = findViewById(R.id.line_dingdanchakan_danjiedan);
        line_jinxingzhong = findViewById(R.id.line_dingdanchakan_jinxingzhong);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rb_all.setOnCheckedChangeListener(this);
        rb_yiwancheng.setOnCheckedChangeListener(this);
        rb_danjiedan.setOnCheckedChangeListener(this);
        rb_jinxingzhong.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_dingdanchakan_all:
                if (isChecked) {
                    line_all.setVisibility(View.VISIBLE);
                    rb_yiwancheng.setChecked(false);
                    rb_danjiedan.setChecked(false);
                    rb_jinxingzhong.setChecked(false);
                    line_yiwancheng.setVisibility(View.INVISIBLE);
                    line_danjiedan.setVisibility(View.INVISIBLE);
                    line_jinxingzhong.setVisibility(View.INVISIBLE);
                    adapter.setData(dingdanList);
                } else {
                    line_all.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_dingdanchakan_yiwancheng:
                if (isChecked) {
                    line_yiwancheng.setVisibility(View.VISIBLE);
                    rb_all.setChecked(false);
                    rb_danjiedan.setChecked(false);
                    rb_jinxingzhong.setChecked(false);
                    line_all.setVisibility(View.INVISIBLE);
                    line_danjiedan.setVisibility(View.INVISIBLE);
                    line_jinxingzhong.setVisibility(View.INVISIBLE);
                    List<Dingdan> yiwanchengList = new ArrayList<>();
                    for (Dingdan item : dingdanList){
                        int type = item.getO_TypeID();
                        if(type==4){
                            yiwanchengList.add(item);
                        }
                    }
                    adapter.setData(yiwanchengList);
                } else {
                    line_yiwancheng.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_dingdanchakan_danjiedan:
                if (isChecked) {
                    line_danjiedan.setVisibility(View.VISIBLE);
                    rb_all.setChecked(false);
                    rb_yiwancheng.setChecked(false);
                    rb_jinxingzhong.setChecked(false);
                    line_yiwancheng.setVisibility(View.INVISIBLE);
                    line_all.setVisibility(View.INVISIBLE);
                    line_jinxingzhong.setVisibility(View.INVISIBLE);
                    List<Dingdan> danjiedanList = new ArrayList<>();
                    for (Dingdan item : dingdanList){
                        int type = item.getO_TypeID();
                        if(type==1){
                            danjiedanList.add(item);
                        }
                    }
                    adapter.setData(danjiedanList);
                } else {
                    line_danjiedan.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rb_dingdanchakan_jinxingzhong:
                if (isChecked) {
                    line_jinxingzhong.setVisibility(View.VISIBLE);
                    rb_all.setChecked(false);
                    rb_yiwancheng.setChecked(false);
                    rb_danjiedan.setChecked(false);
                    line_danjiedan.setVisibility(View.INVISIBLE);
                    line_yiwancheng.setVisibility(View.INVISIBLE);
                    line_all.setVisibility(View.INVISIBLE);
                    List<Dingdan> jinxingzhongList = new ArrayList<>();
                    for (Dingdan item : dingdanList){
                        int type = item.getO_TypeID();
                        if(type==2||type==3){
                            jinxingzhongList.add(item);
                        }
                    }
                    adapter.setData(jinxingzhongList);
                } else {
                    line_jinxingzhong.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
