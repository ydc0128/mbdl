package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Bean.Paihang;
import com.example.administrator.ydxcgld.Contast.Contast;
import com.example.administrator.ydxcgld.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String url = Contast.Domain + "api/AgentOrder.ashx?";

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView iv_touxiang;
    private Button btn_shuaxin;
    private ImageView iv_dingdan;
    private ImageView iv_jishi;

    private CircleImageView iv_cehua_touxiang;
    private TextView tv_name;
    private TextView tv_diqu;
    private TextView tv_gerenziliao;
    private TextView tv_xiugaimima;
    private TextView tv_fuwuxieyi;
    private TextView tv_yijianfankui;
    private TextView tv_guanyuwomen;
    private TextView tv_logout;


    private TextView tv_day_dingdanshu;
    private TextView tv_day_dingdanjine;
    private TextView tv_day_haopingshu;

    private TextView tv_mouth_dingdanshu;
    private TextView tv_mouth_dingdanjine;
    private TextView tv_mouth_haopingshu;

    private TextView tv_all_dingdanshu;
    private TextView tv_all_dingdanjine;
    private TextView tv_all_haopingshu;

    private LinearLayout ll_day;
    private LinearLayout ll_mouth;
    private LinearLayout ll_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        //获取侧滑对象内容
        initCehuaViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDraweLayout();
        refelash();
    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
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
                        Toast.makeText(MainActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this,error.getErrorStr() , Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Paihang> numList = JSON.parseArray(string, Paihang.class);
                                Contast.day = numList.get(0);
                                tv_day_dingdanshu.setText(""+Contast.day.getNum_order());
                                tv_day_dingdanjine.setText(""+Contast.day.getNum_Price());
                                tv_day_haopingshu.setText(""+Contast.day.getNum_Star());

                                Contast.mouth = numList.get(1);
                                tv_mouth_dingdanshu.setText(""+Contast.mouth.getNum_order());
                                tv_mouth_dingdanjine.setText(""+Contast.mouth.getNum_Price());
                                tv_mouth_haopingshu.setText(""+Contast.mouth.getNum_Star());

                                Contast.all = numList.get(2);
                                tv_all_dingdanshu.setText(""+Contast.all.getNum_order());
                                tv_all_dingdanjine.setText(""+Contast.all.getNum_Price());
                                tv_all_haopingshu.setText(""+Contast.all.getNum_Star());
                            }
                        });

                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"服务器繁忙，请稍后重试...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setDraweLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                tv_name.setText(Contast.daili.getAG_Name());
                if(TextUtils.isEmpty(Contast.daili.getAG_Image())){
                    Picasso.with(MainActivity.this).load(R.mipmap.touxiang).into(iv_cehua_touxiang);
                }else {
                    String url = Contast.Domain+Contast.daili.getAG_Image();
                    Picasso.with(MainActivity.this).load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.touxiang)
                            .into(iv_cehua_touxiang);
                }
                if(!TextUtils.isEmpty(Contast.daili.getAG_Province())){
                    tv_diqu.setText(Contast.daili.getAG_Province());
                    return;
                }

                if(!TextUtils.isEmpty(Contast.daili.getAG_City())){
                    tv_diqu.setText(Contast.daili.getAG_City());
                    return;
                }

                if(!TextUtils.isEmpty(Contast.daili.getAG_County())){
                    tv_diqu.setText(Contast.daili.getAG_County());
                    return;
                }
            }
        };

        drawerLayout.setDrawerListener(toggle);
    }

    private void initViews() {
        iv_touxiang = (ImageView) findViewById(R.id.iv_main_touxiang);
        btn_shuaxin = (Button) findViewById(R.id.btn_main_shuaxin);
        iv_dingdan = (ImageView) findViewById(R.id.iv_main_dingdan);
        iv_jishi = (ImageView) findViewById(R.id.iv_main_jishi);
        ll_day = (LinearLayout) findViewById(R.id.ll_main_day);
        ll_mouth = (LinearLayout) findViewById(R.id.ll_main_mouth);
        ll_all = (LinearLayout) findViewById(R.id.ll_main_all);


        tv_day_dingdanshu = (TextView) findViewById(R.id.tv_main_day_dingdanshu);
        tv_day_dingdanjine = (TextView) findViewById(R.id.tv_main_day_dingdanjine);
        tv_day_haopingshu = (TextView) findViewById(R.id.tv_main_day_haopingshu);

        tv_mouth_dingdanshu = (TextView) findViewById(R.id.tv_main_mouth_dingdanshu);
        tv_mouth_dingdanjine = (TextView) findViewById(R.id.tv_main_mouth_dingdanjine);
        tv_mouth_haopingshu = (TextView) findViewById(R.id.tv_main_mouth_haopingshu);

        tv_all_dingdanshu = (TextView) findViewById(R.id.tv_main_all_dingdanshu);
        tv_all_dingdanjine = (TextView) findViewById(R.id.tv_main_all_dingdanjine);
        tv_all_haopingshu = (TextView) findViewById(R.id.tv_main_all_haopingshu);

        iv_touxiang.setOnClickListener(this);
        btn_shuaxin.setOnClickListener(this);
        iv_dingdan.setOnClickListener(this);
        iv_jishi.setOnClickListener(this);
        ll_day.setOnClickListener(this);
        ll_mouth.setOnClickListener(this);
        ll_all.setOnClickListener(this);


    }

    private void initCehuaViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        iv_cehua_touxiang = (CircleImageView) findViewById(R.id.iv_cehua_touxiang);
        tv_name = (TextView) findViewById(R.id.tv_cehua_name);
        tv_diqu = (TextView) findViewById(R.id.tv_cehua_diqu);
        tv_gerenziliao = (TextView) findViewById(R.id.tv_cehua_gerenziliao);
        tv_xiugaimima = (TextView) findViewById(R.id.tv_cehua_mimaxiugai);
        tv_fuwuxieyi = (TextView) findViewById(R.id.tv_cehua_fuwuxieyi);
        tv_yijianfankui = (TextView) findViewById(R.id.tv_cehua_yijianfankui);
        tv_guanyuwomen = (TextView) findViewById(R.id.tv_cehua_guanyuwomen);
        tv_logout = (TextView) findViewById(R.id.tv_cehua_logout);

        tv_gerenziliao.setOnClickListener(this);
        iv_cehua_touxiang.setOnClickListener(this);
        tv_xiugaimima.setOnClickListener(this);
        tv_fuwuxieyi.setOnClickListener(this);
        tv_yijianfankui.setOnClickListener(this);
        tv_guanyuwomen.setOnClickListener(this);
        tv_logout.setOnClickListener(this);

        showDrawerLayout();
    }

    private void showDrawerLayout() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    /**
     * 捕捉back
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.closeDrawer(Gravity.LEFT);
            }else{
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_main_touxiang:
                drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开
                break;
            case R.id.btn_main_shuaxin:
                refelash();
                break;
            case R.id.iv_main_dingdan:
                Intent intent6 = new Intent(this,DingdanchakanActivity.class);
                startActivity(intent6);
                break;
            case R.id.iv_main_jishi:
                Intent intent7 = new Intent(this,JishiActivity.class);
                startActivity(intent7);
                break;
            case R.id.ll_main_day:
                Intent intent3 = new Intent(this,DingdantongjiActivity.class);
                intent3.putExtra("from","day");
                startActivity(intent3);
                break;
            case R.id.ll_main_mouth:
                Intent intent4 = new Intent(this,DingdantongjiActivity.class);
                intent4.putExtra("from","month");
                startActivity(intent4);
                break;
            case R.id.ll_main_all:
                Intent intent5 = new Intent(this,DingdantongjiActivity.class);
                intent5.putExtra("from","all");
                startActivity(intent5);
                break;
            case R.id.iv_cehua_touxiang:
            case R.id.tv_cehua_gerenziliao:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent2 = new Intent(this,GerenziliaoActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_cehua_mimaxiugai:
                Intent intent1 = new Intent(this,YuanmimaxiugaiActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_cehua_fuwuxieyi:
                break;
            case R.id.tv_cehua_yijianfankui:
                break;
            case R.id.tv_cehua_guanyuwomen:
                break;
            case R.id.tv_cehua_logout:
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isLogin", false);
                edit.putString("AG_Tel", "");
                edit.putString("AG_IMEI", "");
                edit.commit();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
