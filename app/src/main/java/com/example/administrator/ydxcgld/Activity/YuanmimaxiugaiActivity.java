package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Contast.Contast;
import com.example.administrator.ydxcgld.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YuanmimaxiugaiActivity extends BaseActivity {


    private static final String TAG = "YuanmimaxiugaiActivity";
    private String url = Contast.Domain+"api/AgentUpdateByPassword.ashx?";

    private ImageView iv_back;
    private EditText et_pwd;
    private EditText et_Newpwd;
    private Button btn_queding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuanmimaxiugai);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_yuanmimaxiugai_back);
        et_pwd = (EditText) findViewById(R.id.et_yuanmimaxiugai_pwd);
        et_Newpwd = (EditText) findViewById(R.id.et_yuanmimaxiugai_newpwd);
        btn_queding = (Button) findViewById(R.id.btn_yuanmimaxiugai_next);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString();
                String newPwd = et_Newpwd.getText().toString();
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(YuanmimaxiugaiActivity.this,"原密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.length()<6||pwd.length()>16){
                    Toast.makeText(YuanmimaxiugaiActivity.this,"原密码长度不正确，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(newPwd)){
                    Toast.makeText(YuanmimaxiugaiActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPwd.length()<6||newPwd.length()>16){
                    Toast.makeText(YuanmimaxiugaiActivity.this,"新密码长度不正确，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressDialog pd = new ProgressDialog(YuanmimaxiugaiActivity.this);
                pd.setMessage("拼命加载中...");
                pd.show();
                FormBody.Builder params = new FormBody.Builder();
                Log.i(TAG, "onClick: "+Contast.daili.getAG_Tel());
                params.add("AG_Tel", Contast.daili.getAG_Tel());
                params.add("AG_PwdNew", newPwd);
                params.add("AG_Pwd", pwd);
                params.add("keys", Contast.KEYS);
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
                                pd.dismiss();
                                Toast.makeText(YuanmimaxiugaiActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        pd.dismiss();
                        String string = response.body().string();
                        Log.i(TAG, "onResponse: " + string);
                        if (!TextUtils.isEmpty(string)) {
                            if (string.contains("ErrorStr")) {
                                final Error error = JSON.parseObject(string, Error.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YuanmimaxiugaiActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if(string.equals("ok")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YuanmimaxiugaiActivity.this, "密码修改完成", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(YuanmimaxiugaiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
}
