package com.example.administrator.ydxcgld.Adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ydxcgld.Bean.Dingdan;
import com.example.administrator.ydxcgld.Bean.Worker;
import com.example.administrator.ydxcgld.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class JishiguanliAdapter extends MyBaseAdapter {
    public JishiguanliAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_jishiguanli_listview;
    }

    @Override
    public Object getViewHolder(View rootView) {
       return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        final Worker worker = (Worker) dataItem;
        //将holder 转为自己holder
        JishiguanliAdapter.ViewHolder myHolder = (JishiguanliAdapter.ViewHolder) viewHolder;
        myHolder.tv_name.setText(worker.getW_Name());
        myHolder.tv_phone.setText(worker.getW_Tel());
        if(worker.getW_IsFormal()==1&&worker.getW_IsFormalMe()==1){
            myHolder.tv_shenhe.setText("是");
        }else{
            myHolder.tv_shenhe.setText("否");
        }
        if(worker.getW_IsStart()==1){
            myHolder.tv_kaishijiedan.setText("是");
        }else{
            myHolder.tv_kaishijiedan.setText("否");
        }
    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_shenhe;
        private TextView tv_kaishijiedan;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_name = (TextView) this.root.findViewById(R.id.tv_item_jishiguanli_name);
            this.tv_phone =  (TextView) this.root.findViewById(R.id.tv_item_jishiguanli_phone);
            this.tv_shenhe = (TextView) this.root.findViewById(R.id.tv_item_jishiguanli_shenhe);
            this.tv_kaishijiedan = (TextView) this.root.findViewById(R.id.tv_item_jishiguanli_kaishijiedan);
        }
    }
}
