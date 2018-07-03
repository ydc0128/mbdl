package com.example.administrator.ydxcgld.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ydxcgld.Bean.Paihang;
import com.example.administrator.ydxcgld.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class JishipaihangAdapter extends MyBaseAdapter {

    public JishipaihangAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_jishipaihang_listview;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        Paihang paihang = (Paihang) dataItem;
        JishipaihangAdapter.ViewHolder myHolder = (JishipaihangAdapter.ViewHolder) viewHolder;
        myHolder.tv_name.setText(paihang.getW_Name());
        myHolder.tv_dingdanshu.setText(""+paihang.getNum_order());
        myHolder.tv_dingdanzonge.setText(""+paihang.getNum_Price());
        myHolder.tv_haopingshu.setText(""+paihang.getNum_Star());
    }

    /**
     * ViewHolder 通过构造方法中 实现具体view的绑定的方式 创建一个自实现绑定View的ViewHolder
     * Created by bailiangjin on 16/7/5.
     */
    public static class ViewHolder {
        public final View root;
        private TextView tv_name;
        private TextView tv_dingdanzonge;
        private TextView tv_dingdanshu;
        private TextView tv_haopingshu;


        public ViewHolder(View root) {
            this.root = root;
            this.tv_name = (TextView) this.root.findViewById(R.id.tv_jishipaihang_listview_name);
            this.tv_dingdanzonge =  (TextView) this.root.findViewById(R.id.tv_jishipaihang_listview_zonge);
            this.tv_dingdanshu = (TextView) this.root.findViewById(R.id.tv_jishipaihang_listview_dingdanshu);
            this.tv_haopingshu = (TextView) this.root.findViewById(R.id.tv_jishipaihang_listview_haopingshu);
        }
    }
}
