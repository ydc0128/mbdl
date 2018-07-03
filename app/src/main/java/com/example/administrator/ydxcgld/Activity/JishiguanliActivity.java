package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Adapter.JishiguanliAdapter;
import com.example.administrator.ydxcgld.Bean.Dingdan;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Bean.Worker;
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

public class JishiguanliActivity extends BaseActivity {

    private String url = Contast.Domain + "api/AgentWorkers.ashx?";
    private static final String TAG = "JishiguanliActivity";

    private ImageView iv_back;
    private Button btn_add;
    private List<Worker> workerList;
    private ListView listView;
    private JishiguanliAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jishiguanli);
        initViews();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refelash();
    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(JishiguanliActivity.this);
        pd.setMessage("拼命加载中...");
        pd.show();
        FormBody.Builder params = new FormBody.Builder();
        params.add("keys", Contast.KEYS);
        params.add("AG_Tel", Contast.daili.getAG_Tel());
        params.add("AG_IMEI", Contast.daili.getAG_IMEI());
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
                        Toast.makeText(JishiguanliActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(JishiguanliActivity.this,error.getErrorStr() , Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workerList = JSON.parseArray(string,Worker.class);
                                adapter.setData(workerList);
                            }
                        });

                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(JishiguanliActivity.this,"服务器繁忙，请稍后重试...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lv_jishiguanli_listview);
        workerList = new ArrayList<>();
        adapter = new JishiguanliAdapter(this,workerList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Worker worker = (Worker) adapter.getItem(position);
//                Intent intent = new Intent(JishiguanliActivity.this,JishixiangqingActivity.class);
//                intent.putExtra("worker",worker);
//                startActivity(intent);
            }
        });
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_jishiguanli_back);
        btn_add = (Button) findViewById(R.id.btn_jishiguanli_more);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JishiguanliActivity.this,AddJishiActivity.class);
                startActivity(intent);
            }
        });
    }
}
