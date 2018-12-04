package com.will.talentreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.entity.Dict;
import com.will.talentreview.utils.StringUtils;

import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-26
 * 产品类型适配器
 */

public class ProductTypeGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Dict> mProductTypes;
    private LayoutInflater mInflater;
    private View currentSelectView;

    public ProductTypeGridAdapter(Context context, List<Dict> productTypes) {
        this.mContext = context;
        this.mProductTypes = productTypes;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mProductTypes.size();
    }

    @Override
    public Dict getItem(int i) {
        return mProductTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView tvType;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_product_type_grid, null);
            holder = new ViewHolder();
            holder.tvType = view.findViewById(R.id.tv_type);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Dict type = getItem(i);
        holder.tvType.setText(StringUtils.excludeNull(type.getValue(), "未知"));
        holder.tvType.setSelected(type.isSelected());
        holder.tvType.setTag(i);
        holder.tvType.setOnClickListener(onClickListener);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clearSelect();
            int position = (int) view.getTag();
            view.setSelected(!view.isSelected());
            getItem(position).setSelected(view.isSelected());
        }
    };

    public void clearSelect() {
        if (mProductTypes == null || mProductTypes.isEmpty()) {
            return;
        }
        for (Dict dict : mProductTypes) {
            dict.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public Dict getSelectedItem() {
        if (mProductTypes == null || mProductTypes.isEmpty()) {
            return null;
        }
        for (Dict dict : mProductTypes) {
            if (dict.isSelected()) {
                return dict;
            }
        }
        notifyDataSetChanged();
        return null;
    }
}
