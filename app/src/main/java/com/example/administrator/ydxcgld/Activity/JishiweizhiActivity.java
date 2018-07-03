package com.example.administrator.ydxcgld.Activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.ydxcgld.Bean.Error;
import com.example.administrator.ydxcgld.Bean.Worker;
import com.example.administrator.ydxcgld.Contast.Contast;
import com.example.administrator.ydxcgld.R;
import com.example.administrator.ydxcgld.Util.BitmapUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class JishiweizhiActivity extends BaseActivity implements View.OnClickListener{

    private String url = Contast.Domain + "api/AgentWorkers.ashx?";

    private static final String TAG = "JishiweizhiActivity";


    private ImageView iv_back;

    private double lat_max;
    private double lat_min;
    private double lon_max;
    private double lon_min;

    private double lat;
    private double lon;
    private double finallat;
    private double finallon;
    public LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    private int zoom;
    private List<Worker> workerList = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    private boolean loading = false;
    private boolean isFirstLocation = true;
    private ImageButton ib_shuaxin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //使用百度地图的任何功能都需要先初始化这段代码  最好放在全局中进行初始化
        //百度地图+定位+marker比较简单 我就不放到全局去了
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_jishiweizhi);

        refelash();

        mMapView = (MapView) findViewById(R.id.mv_jishiweizhi_ditu);

        mBaiduMap = mMapView.getMap();

//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        ib_shuaxin = (ImageButton) findViewById(R.id.ib_main_shuaxin);
        iv_back = (ImageView) findViewById(R.id.iv_jishiweizhi_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();
        initListener();
        ib_shuaxin.setOnClickListener(this);
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_main_shuaxin:
                setUserMapCenter();
                initLocation();
                mLocationClient.start();
                initListener();
                break;
        }
    }

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            lat = location.getLatitude();
            lon = location.getLongitude();
            //这个判断是为了防止每次定位都重新设置中心点和marker

            if (loading && isFirstLocation) {
                setUserMapCenter();
                setMarker();
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }

    }
    /**
     * 地图状态改变
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState(status);
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });
    }
    private void updateMapState(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        /**获取经纬度*/
        finallat = mCenterLatLng.latitude;
        finallon = mCenterLatLng.longitude;
        LatLng point = new LatLng(finallat, finallon);

    }
    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        Log.v("pcw", "setUserMapCenter : lat : " + lat + " lon : " + lon);
        LatLng cenpt = new LatLng(lat, lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(13).build()));
    }


    private void setMarker() {
        Log.i(TAG, "setMarker: ");
        for (int i = 0; i < workerList.size(); i++) {
            if (workerList.get(i) != null) {
//                Log.i(TAG, "setMarker: workerList=" + workerList.get(i).toString());
//                Log.i(TAG, "setMarker: images=" + images.get(i).toString());
//                Log.i(TAG, "setMarker: latLngs=" + latLngs.get(i).toString());
                final View view = View.inflate(getApplicationContext(), R.layout.item_touxiang, null);
                CircleImageView civ = (CircleImageView) view.findViewById(R.id.iv_item_touxiang);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_item_touxiang_name);
                tv_name.setText(workerList.get(i).getW_Name());
                String url =workerList.get(i).getW_Image();
                if (!TextUtils.isEmpty(images.get(i))) {
                    final int index = i;
                    Log.i(TAG, "setMarker: index=" + index);
                    Picasso.with(JishiweizhiActivity.this).load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.ic_launcher)
                            .into(civ, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    //设置成功后把View转换成Bitmap
                                    Bitmap viewBitmap = BitmapUtils.getViewBitmap(view);
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(viewBitmap);
                                    LatLng latLng = latLngs.get(index);

                                    //构建Marker对象和指定经纬度
                                    MarkerOptions marker = new MarkerOptions()
                                            .icon(bitmapDescriptor)
                                            .position(latLng);
                                    //添加到地图上
                                    mBaiduMap.addOverlay(marker);
                                    Log.i(TAG, "onSuccess: latLng=" + latLng);
                                    Log.i(TAG, "onSuccess: 图片加载成功");
                                }

                                @Override
                                public void onError() {
                                    Log.i(TAG, "onError: 图片加载失败");
                                }
                            });
                } else {
                    final int index = i;
                    Log.i(TAG, "setMarker: index=" + index);
                    Picasso.with(JishiweizhiActivity.this).load(R.drawable.bullet_blue)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .error(R.mipmap.ic_launcher)
                            .into(civ, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    //设置成功后把View转换成Bitmap
                                    Bitmap viewBitmap = BitmapUtils.getViewBitmap(view);
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(viewBitmap);
                                    LatLng latLng = latLngs.get(index);

                                    //构建Marker对象和指定经纬度
                                    MarkerOptions marker = new MarkerOptions()
                                            .icon(bitmapDescriptor)
                                            .position(latLng);
                                    //添加到地图上
                                    mBaiduMap.addOverlay(marker);
                                    Log.i(TAG, "onSuccess: latLng=" + latLng);
                                    Log.i(TAG, "onSuccess: 图片加载成功");
                                }

                                @Override
                                public void onError() {
                                    Log.i(TAG, "onError: 图片加载失败");
                                }
                            });
                }

            }
        }

    }

//        for ( int i = 0 ; i < workerList.size() ; i++ ){
//            if(!TextUtils.isEmpty(images.get(i))){
//
//                //设置成功后把View转换成Bitmap
//                View view = View.inflate(getApplicationContext(), R.layout.item_touxiang, null);
//                CircleImageView civ = (CircleImageView) view.findViewById(R.id.iv_item_touxiang);
//                Picasso.with(this).load(images.get(i))
//                        .memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .networkPolicy(NetworkPolicy.NO_CACHE)
//                        .error(R.mipmap.ic_launcher)
//                        .into(civ);
//
//                //设置成功后把View转换成Bitmap
//                Bitmap viewBitmap = BitmapUtils.getViewBitmap(view);
//
//                //调用百度地图提供的api获取刚转换的Bitmap
//                BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromBitmap(viewBitmap);
//                LatLng latLng = latLngs.get(i);
//
//                //构建Marker对象和指定经纬度
//                MarkerOptions marker= new MarkerOptions()
//                        .icon(bitmapDescriptor)
//                        .position(latLng);
//                //添加到地图上
//                mBaiduMap.addOverlay(marker);
//
//            }
//        }


//    /**
//     * 设置中心点
//     */
//    private void setUserMapCenter() {
//        List<Double> lats = new ArrayList<>();
//        List<Double> lons = new ArrayList<>();
//
//        for (int i = 0; i < workerList.size(); i++) {
//            lats.add(workerList.get(i).getW_Lat());
//            lons.add(workerList.get(i).getW_Lng());
//        }
//        if (lats.size()!=0) {
//            lat_max = Collections.max(lats);
//            lat_min = Collections.min(lats);
//            lon_max = Collections.max(lons);
//            lon_min = Collections.min(lons);
//
//        }else {
//
//        }
//        double distance = DistanceUtil.getDistance(new LatLng(lat_max, lon_max), new LatLng(lat_min, lon_min));
//        zoom = getZoom(distance);
//        LatLng cenpt = new LatLng((lat_max + lat_min) / 2, (lon_max + lon_min) / 2);
//        Log.i("distance", "distance=" + distance);
//        Log.i("zoom", "zoom=" + zoom);
//        //定义地图状态
//        MapStatus mMapStatus = new MapStatus.Builder()
//                .target(cenpt)
//                .zoom(zoom)
//                .build();
//        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        //改变地图状态
//        mBaiduMap.setMapStatus(mMapStatusUpdate);
//    }

    private void refelash() {
        final ProgressDialog pd = new ProgressDialog(JishiweizhiActivity.this);
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
                        Toast.makeText(JishiweizhiActivity.this, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(JishiweizhiActivity.this, error.getErrorStr(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                workerList = JSON.parseArray(string, Worker.class);
                                for (Worker worker : workerList) {
                                    latLngs.add(new LatLng(worker.getW_Lat(), worker.getW_Lng()));
                                    if (TextUtils.isEmpty(worker.getW_Image())) {
                                        images.add("");
                                    } else {
                                        images.add(Contast.Domain + worker.getW_Image());
                                    }
                                }
                                loading = true;
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(JishiweizhiActivity.this, "服务器繁忙，请稍后重试...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 计算缩放级别
     */
    private int getZoom(double distance) {

        int[] arr = {20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000,
                25000, 50000, 100000, 200000, 500000, 1000000, 2000000};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - distance > 0) {
                return 19 - i + 3;
            }
        }
        return 3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        ib_shuaxin.performClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}



