package com.example.administrator.ydxcgld.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.ydxcgld.Adapter.DialogListViewAdapter;
import com.example.administrator.ydxcgld.Bean.Daili;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Contast.Contast;
import com.example.administrator.ydxcgld.R;
import com.example.administrator.ydxcgld.Util.BankCardTextWatcher;
import com.example.administrator.ydxcgld.Util.ImageUtils;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GerenziliaoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyZiliaoActivity";

    private String url = Contast.Domain + "api/AgentInfoSave.ashx?";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private ImageView iv_back;
    private CircleImageView iv_touxiang;
    private Button btn_bianji;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_shenfenzheng;
    private EditText et_kaihuhang;
    private EditText et_yinhangkahao;
    private EditText et_kaihuhangdizhi;
    private RadioButton rb_nan;
    private RadioButton rb_nv;
    private ImageView iv_zhengmian;
    private ImageView iv_fanmian;
    private ImageView iv_shouchi;
    private Button btn_tijiao;

    private List<String> photoList;

    private File touxiangFile;
    private File zhengmianFile;
    private File fanmianFile;
    private File shouchiFile;

    private Uri imageUri;

    private int from = -1;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenziliao);
        initViews();
        setViews();
        BankCardTextWatcher.bind(et_yinhangkahao);
    }


    private void initViews() {
        photoList = new ArrayList<>();
        photoList.add("拍照");
        photoList.add("从相册中选择");
        iv_back = (ImageView) findViewById(R.id.iv_myziliao_back);
        iv_zhengmian = (ImageView) findViewById(R.id.iv_myziliao_zhengmian);
        iv_fanmian = (ImageView) findViewById(R.id.iv_myziliao_fanmian);
        iv_shouchi = (ImageView) findViewById(R.id.iv_myziliao_shouchi);
        iv_touxiang = (CircleImageView) findViewById(R.id.iv_myziliao_touxiang);
        et_name = (EditText) findViewById(R.id.et_myziliao_name);
        et_phone = (EditText) findViewById(R.id.et_myziliao_phone);
        et_shenfenzheng = (EditText) findViewById(R.id.et_myziliao_shenfenzhenghao);
        et_kaihuhang = (EditText) findViewById(R.id.et_myziliao_kaihuhang);
        et_yinhangkahao = (EditText) findViewById(R.id.et_myziliao_yinhangkahao);
        et_kaihuhangdizhi = (EditText) findViewById(R.id.et_myziliao_kaihuhangdizhi);
        rb_nan = (RadioButton) findViewById(R.id.rb_myziliao_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_myziliao_nv);
        btn_bianji = (Button) findViewById(R.id.btn_myziliao_more);
        btn_tijiao = (Button) findViewById(R.id.btn_myziliao_tijiao);
        rb_nan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb_nv.setChecked(false);
                }else{
                    rb_nv.setChecked(true);
                }
            }
        });
        rb_nv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb_nan.setChecked(false);
                }else{
                    rb_nan.setChecked(true);
                }
            }
        });

        iv_back.setOnClickListener(this);
        iv_zhengmian.setOnClickListener(this);
        iv_fanmian.setOnClickListener(this);
        iv_shouchi.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);
        btn_bianji.setOnClickListener(this);
        btn_tijiao.setOnClickListener(this);
    }


    private void setViews() {
        iv_touxiang.setClickable(false);
        iv_zhengmian.setClickable(false);
        iv_fanmian.setClickable(false);
        iv_shouchi.setClickable(false);
        et_name.setCursorVisible(false);
        et_name.setFocusable(false);
        et_name.setFocusableInTouchMode(false);
        et_phone.setCursorVisible(false);
        et_phone.setFocusable(false);
        et_phone.setFocusableInTouchMode(false);
        et_shenfenzheng.setCursorVisible(false);
        et_shenfenzheng.setFocusable(false);
        et_shenfenzheng.setFocusableInTouchMode(false);
        et_kaihuhangdizhi.setCursorVisible(false);
        et_kaihuhangdizhi.setFocusable(false);
        et_kaihuhangdizhi.setFocusableInTouchMode(false);
        et_kaihuhang.setCursorVisible(false);
        et_kaihuhang.setFocusable(false);
        et_kaihuhang.setFocusableInTouchMode(false);
        et_yinhangkahao.setCursorVisible(false);
        et_yinhangkahao.setFocusable(false);
        et_yinhangkahao.setFocusableInTouchMode(false);
        rb_nan.setClickable(false);
        rb_nv.setClickable(false);
        if(Contast.daili.getAG_Sex()==1){
            rb_nan.setChecked(true);
        }else if(Contast.daili.getAG_Sex()==0){
            rb_nv.setChecked(true);
        }
        if(!TextUtils.isEmpty(Contast.daili.getAG_Image())){
            String url = Contast.Domain+Contast.daili.getAG_Image();
            Picasso.with(GerenziliaoActivity.this).load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.touxiang)
                    .into(iv_touxiang);
        }else{
            Picasso.with(GerenziliaoActivity.this).load(R.mipmap.touxiang).into(iv_touxiang);
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_IdentityCardImage1())){
            String url = Contast.Domain+Contast.daili.getAG_IdentityCardImage1();
            Picasso.with(GerenziliaoActivity.this).load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.paizhao)
                    .into(iv_zhengmian);
        }else{
            Picasso.with(GerenziliaoActivity.this).load(R.mipmap.paizhao).into(iv_zhengmian);
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_IdentityCardImage2())){
            String url = Contast.Domain+Contast.daili.getAG_IdentityCardImage2();
            Picasso.with(GerenziliaoActivity.this).load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.paizhao)
                    .into(iv_fanmian);
        }else{
            Picasso.with(GerenziliaoActivity.this).load(R.mipmap.paizhao).into(iv_fanmian);
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_IdentityCardImage3())){
            String url = Contast.Domain+Contast.daili.getAG_IdentityCardImage3();
            Picasso.with(GerenziliaoActivity.this).load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.paizhao)
                    .into(iv_shouchi);
        }else{
            Picasso.with(GerenziliaoActivity.this).load(R.mipmap.paizhao).into(iv_shouchi);
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_Name())){
            et_name.setText(Contast.daili.getAG_Name());
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_Tel())){
            et_phone.setText(Contast.daili.getAG_Tel());
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_IdentityCard())){
            et_shenfenzheng.setText(Contast.daili.getAG_IdentityCard());
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_BankName())){
            et_kaihuhang.setText(Contast.daili.getAG_BankName());
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_BankCard())){
            et_yinhangkahao.setText(Contast.daili.getAG_BankCard());
        }

        if(!TextUtils.isEmpty(Contast.daili.getAG_BankAdress())){
            et_kaihuhangdizhi.setText(Contast.daili.getAG_BankAdress());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_myziliao_back:
                finish();
                break;
            case R.id.iv_myziliao_touxiang:
                dialogListShow(1);
                break;
            case R.id.iv_myziliao_zhengmian:
                dialogListShow(2);
                break;
            case R.id.iv_myziliao_fanmian:
                dialogListShow(3);
                break;
            case R.id.iv_myziliao_shouchi:
                dialogListShow(4);
                break;
            case R.id.btn_myziliao_more:
                flag = true;
                iv_touxiang.setClickable(true);
                iv_zhengmian.setClickable(true);
                iv_fanmian.setClickable(true);
                iv_shouchi.setClickable(true);
                et_name.setCursorVisible(true);
                et_name.setFocusable(true);
                et_name.setFocusableInTouchMode(true);
                et_shenfenzheng.setCursorVisible(true);
                et_shenfenzheng.setFocusable(true);
                et_shenfenzheng.setFocusableInTouchMode(true);
                et_kaihuhangdizhi.setCursorVisible(true);
                et_kaihuhangdizhi.setFocusable(true);
                et_kaihuhangdizhi.setFocusableInTouchMode(true);
                et_kaihuhang.setCursorVisible(true);
                et_kaihuhang.setFocusable(true);
                et_kaihuhang.setFocusableInTouchMode(true);
                et_yinhangkahao.setCursorVisible(true);
                et_yinhangkahao.setFocusable(true);
                et_yinhangkahao.setFocusableInTouchMode(true);
                btn_tijiao.setVisibility(View.VISIBLE);
                rb_nan.setClickable(true);
                rb_nv.setClickable(true);
                btn_bianji.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_myziliao_tijiao:
                save();
                break;
        }
    }

    private void dialogListShow(final int i) {
        View photoView = View.inflate(GerenziliaoActivity.this, R.layout.item_listview_dialog, null);//填充ListView布局
        ListView listView = (ListView) photoView.findViewById(R.id.lv_item_listview_dialog);//初始化ListView控件
        listView.setAdapter(new DialogListViewAdapter(GerenziliaoActivity.this, photoList));//ListView设置适配器

        final AlertDialog dialog2 = new AlertDialog.Builder(GerenziliaoActivity.this)
                .setView(photoView)//在这里把写好的这个listview的布局加载dialog中
                .create();
        dialog2.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TextView tv = (TextView) arg1.findViewById(R.id.tv_item_listview_dialog_text);//取得每条item中的textview控件
                String aiche = tv.getText().toString();
                if ("拍照".equals(aiche)) {
                    File outputImage = new File(getExternalCacheDir(),
                            "output_image"+i+".jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri = FileProvider.getUriForFile(GerenziliaoActivity.this,
                                "com.example.administrator.ydxcgld.fileprovider", outputImage);
                    } else {
                        imageUri = Uri.fromFile(outputImage);
                    }
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    from = i;
                    startActivityForResult(intent, TAKE_PHOTO);
                } else if ("从相册中选择".equals(aiche)) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    from = i;
                    startActivityForResult(intent, CHOOSE_PHOTO);
                }
                dialog2.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    beginCrop(imageUri,from);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKatKit(data);
                    } else {
                        handleImageBeforKatKit(data);
                    }
                }
                break;

            case Crop.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    beginCrop(data.getData(),from);
                }
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
        }
    }

    // 将裁剪回来的数据进行处理
    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(data);
            try {
                if (from == 1) {
                    iv_touxiang.setImageBitmap(null);
                    touxiangFile = new File(ImageUtils.saveBitmap(GerenziliaoActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(touxiangFile);
                    Picasso.with(GerenziliaoActivity.this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.touxiang)
                            .into(iv_touxiang);
                    Log.i("image", "touxiangFile=" + touxiangFile.getAbsolutePath());
                } else if (from == 2) {
                    iv_zhengmian.setImageBitmap(null);
                    zhengmianFile = new File(ImageUtils.saveBitmap(GerenziliaoActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(zhengmianFile);
                    Picasso.with(GerenziliaoActivity.this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.paizhao)
                            .into(iv_zhengmian);
                    Log.i("image", "zhengmianFile=" + zhengmianFile.getAbsolutePath());
                }else if (from == 3) {
                    iv_fanmian.setImageBitmap(null);
                    fanmianFile = new File(ImageUtils.saveBitmap(GerenziliaoActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(fanmianFile);
                    Picasso.with(GerenziliaoActivity.this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.paizhao)
                            .into(iv_fanmian);
                    Log.i("image", "fanmianFile=" + fanmianFile.getAbsolutePath());
                }else if (from == 4) {
                    iv_shouchi.setImageBitmap(null);
                    shouchiFile = new File(ImageUtils.saveBitmap(GerenziliaoActivity.this, uri.getPath()));
                    Uri image = Uri.fromFile(shouchiFile);
                    Picasso.with(GerenziliaoActivity.this).load(image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.paizhao)
                            .into(iv_shouchi);
                    Log.i("image", "shouchiFile=" + shouchiFile.getAbsolutePath());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(GerenziliaoActivity.this, Crop.getError(data).getMessage(),
                    Toast.LENGTH_SHORT).show();

        }
    }

    // 开始裁剪
    private void beginCrop(Uri uri,int i) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"+i+".jpg"));
        // start() 方法根据其的需求选择不同的重载方法
        Crop.of(uri, destination).asSquare().start(GerenziliaoActivity.this);
    }


    private void handleImageBeforKatKit(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        disPlayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKatKit(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(GerenziliaoActivity.this, uri)) {
            //  如果是Document类型的uri，则通过Document  Id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public/_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取图片路径即可
            imagePath = uri.getPath();
        }
        disPlayImage(imagePath);
    }

    private void disPlayImage(String imagePath) {
        if (imagePath != null) {
            Uri uri = Uri.fromFile(new File(imagePath));
            beginCrop(uri,from);
        } else {
            Toast.makeText(GerenziliaoActivity.this, "图片选取失败", Toast.LENGTH_SHORT).show();
        }
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取图片的真是路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void save() {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String shenfenzheng = et_shenfenzheng.getText().toString().trim();
        String kaihuhang = et_kaihuhang.getText().toString().trim();
        String kaihuhangdizhi = et_kaihuhangdizhi.getText().toString().trim();
        String yinhangkahao = et_yinhangkahao.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(shenfenzheng)){
            Toast.makeText(this,"身份证号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(kaihuhang)){
            Toast.makeText(this,"开户行不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(kaihuhangdizhi)){
            Toast.makeText(this,"开户行地址不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(yinhangkahao)){
            Toast.makeText(this,"银行卡号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        int sex = -1;
        if(rb_nan.isChecked()){
            sex = 1;
        }else if(rb_nv.isChecked()){
            sex = 0;
        }
        String imageStr1 = "";
        String imageStr2 = "";
        String imageStr3 = "";
        String imageStr4 = "";
        if (touxiangFile != null) {
            imageStr1 = ImageUtils.getBase64String(touxiangFile);
        }
        if (zhengmianFile != null) {
            imageStr2 = ImageUtils.getBase64String(zhengmianFile);
        }
        if (fanmianFile != null) {
            imageStr3 = ImageUtils.getBase64String(fanmianFile);
        }
        if (shouchiFile != null) {
            imageStr4 = ImageUtils.getBase64String(shouchiFile);
        }
        final ProgressDialog pd2 = new ProgressDialog(GerenziliaoActivity.this);
        pd2.setMessage("拼命加载中...");
        pd2.show();
        FormBody.Builder params2 = new FormBody.Builder();
        params2.add("data1", imageStr1);
        params2.add("data2", imageStr2);
        params2.add("data3", imageStr3);
        params2.add("data4", imageStr4);
        params2.add("AG_Tel", phone);
        params2.add("AG_IMEI", Contast.daili.getAG_IMEI());
        params2.add("AG_Name", name);
        params2.add("AG_Sex", ""+sex);
        params2.add("AG_IdentityCard", shenfenzheng);
        params2.add("AG_BankName", kaihuhang);
        params2.add("AG_BankCard", yinhangkahao);
        params2.add("AG_BankAdress", kaihuhangdizhi);
        params2.add("keys", Contast.KEYS);
        OkHttpClient client2 = new OkHttpClient();
        Request request2 = new Request.Builder()
                .url(url)
                .post(params2.build())
                .build();
        okhttp3.Call call2 = client2.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd2.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GerenziliaoActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd2.dismiss();
                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    if (string.contains("ErrorStr")) {
                        final Error error = JSON.parseObject(string, Error.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GerenziliaoActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else  {
                        Contast.daili = JSON.parseObject(string, Daili.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GerenziliaoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GerenziliaoActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 捕捉back
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(flag){
                dialogShow();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void dialogShow() {
        AlertDialog dialog = new AlertDialog.Builder(GerenziliaoActivity.this)
                .setTitle("提示")
                .setMessage("是否对当前修改行进保存？")
                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                })
                .setPositiveButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }
}
