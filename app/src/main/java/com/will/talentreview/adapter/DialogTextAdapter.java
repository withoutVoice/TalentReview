package com.will.talentreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.will.talentreview.R;

import java.util.List;

/**
 * 弹框适配器
 *
 * @author yanchenglong
 * @time 2018-06-07
 */

public class DialogTextAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> stringList;

    /**
     * 构造方法
     *
     * @param activity   上下文对象
     * @param stringList 数据集合
     */
    public DialogTextAdapter(Context activity, List<String> stringList) {
        this.stringList = stringList;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_dialog_text, null);
            holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(stringList.get(position));
        return view;
    }

    /**
     * 控件实体
     */
    class ViewHolder {
        private TextView tvName;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
