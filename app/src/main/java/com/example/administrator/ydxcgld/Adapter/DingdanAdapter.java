package com.example.administrator.ydxcgld.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.ydxcgld.Bean.Dingdan;
import com.example.administrator.ydxcgld.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class DingdanAdapter extends MyBaseAdapter {

    private static final String TAG = "DingdanAdapter";

    public DingdanAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_dingdan;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final Dingdan dingdan = (Dingdan) dataItem;
        //将holder 转为自己holder
        ViewHolder myHolder = (ViewHolder) viewHolder;
        String fuwu = dingdan.getO_WPart();
        Log.i(TAG, "setItemData: fuwu=" + fuwu);
        StringBuffer sb = new StringBuffer();
        if (fuwu.contains("|")) {
            String[] split = fuwu.split("\\|");
            for (int i = 0; i < split.length; i++) {
                Log.i(TAG, "setItemData: split=" + split[i]);
                String[] strings = split[i].split(",");
                int num = Integer.parseInt(strings[0]);
                switch (num) {
                    case 1:
                    case 7:
                        sb.append("车外清洗").append(",");
                        break;
                    case 2:
                    case 9:
                        sb.append("标准清洗").append(",");
                        break;
                    case 3:
                    case 10:
                        sb.append("发动机清洗").append(",");
                        break;
                    case 4:
                    case 11:
                        sb.append("打蜡").append(",");
                        break;
                    case 5:
                    case 12:
                        sb.append("室内精洗").append(",");
                        break;
                    case 6:
                    case 13:
                        sb.append("空调消毒").append(",");
                        break;
                }
            }
        } else {
            String[] split = fuwu.split(",");
            for (int i = 0; i < split.length; i++) {
                Log.i(TAG, "setItemData: split=" + split[i]);
            }
            int num = Integer.parseInt(split[0]);
            switch (num) {
                case 1:
                case 7:
                    sb.append("车外清洗").append(",");
                    break;
                case 2:
                case 9:
                    sb.append("标准清洗").append(",");
                    break;
                case 3:
                case 10:
                    sb.append("发动机清洗").append(",");
                    break;
                case 4:
                case 11:
                    sb.append("打蜡").append(",");
                    break;
                case 5:
                case 12:
                    sb.append("室内精洗").append(",");
                    break;
                case 6:
                case 13:
                    sb.append("空调消毒").append(",");
                    break;
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        myHolder.tv_fuwu.setText(sb.toString());
        if(dingdan.getO_IsEvaluate()==0){
            myHolder.tv_start.setText("未评价");
        }else if(dingdan.getO_IsEvaluate()==1){
            myHolder.tv_start.setText(dingdan.getO_EvaluateStar()+"星评价");
        }
        myHolder.tv_dingdanhao.setText(dingdan.getO_ID());
        myHolder.tv_name.setText(dingdan.getO_WorkerName());
        myHolder.tv_car.setText(dingdan.getO_PlateNumber()+"-"+dingdan.getO_BrandName()+"-"
        +dingdan.getO_BrandTypeName()+"-"+dingdan.getO_CarColor());

        myHolder.tv_dizhi.setText(dingdan.getO_Adress()+";"+dingdan.getO_WriteAdress());
        String str = dingdan.getO_Time();
        String shijian = str.replace("T","  ");
        myHolder.tv_time.setText(shijian);
        if(TextUtils.isEmpty(dingdan.getO_Remark())){
            myHolder.tv_beizhu.setText("备注：暂无");
        }else{
            myHolder.tv_beizhu.setText("备注："+dingdan.getO_Remark());
        }





    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_start;
        private TextView tv_dingdanhao;
        private TextView tv_name;
        private TextView tv_fuwu;
        private TextView tv_car;
        private TextView tv_dizhi;
        private TextView tv_time;
        private TextView tv_beizhu;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_start = (TextView) this.root.findViewById(R.id.tv_item_dingdan_start);
            this.tv_dingdanhao = (TextView) this.root.findViewById(R.id.tv_item_dingdan_dingdanhao);
            this.tv_name = (TextView) this.root.findViewById(R.id.tv_item_dingdan_name);
            this.tv_fuwu = (TextView) this.root.findViewById(R.id.tv_item_dingdan_fuwu);
            this.tv_car = (TextView) this.root.findViewById(R.id.tv_item_dingdan_car);
            this.tv_dizhi = (TextView) this.root.findViewById(R.id.tv_item_dingdan_dizhi);
            this.tv_time = (TextView) this.root.findViewById(R.id.tv_item_dingdan_time);
            this.tv_beizhu = (TextView) this.root.findViewById(R.id.tv_item_dingdan_beizhu);
        }
    }
}
