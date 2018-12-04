package com.will.talentreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.entity.Product;
import com.will.talentreview.utils.GlideUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.SquareImageView;

import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-20
 */

public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> mProducts;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.mProducts = products;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Product getItem(int i) {
        return mProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvRate;
        TextView tvTerm;
        TextView tvType;
        LinearLayout llBoughtContainer;
        View vDivider;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_product_list, null);
            holder = new ViewHolder();
            holder.ivCover = view.findViewById(R.id.iv_cover);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvRate = view.findViewById(R.id.tv_rate);
            holder.tvTerm = view.findViewById(R.id.tv_term);
            holder.tvType = view.findViewById(R.id.tv_type);
            holder.llBoughtContainer = view.findViewById(R.id.ll_bought_container);
            holder.vDivider = view.findViewById(R.id.v_bought_divider);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Product product = getItem(i);
        holder.tvName.setText(StringUtils.excludeNull(product.getProductName(), "未知"));
        holder.tvRate.setText(StringUtils.excludeNull(product.getAnnualInterestRate(), "0.0") + "%");
        holder.tvTerm.setText(StringUtils.excludeNull(product.getAgeLimit(), "0") + "年");
        holder.tvType.setText(StringUtils.excludeNull(product.getProductRule(), "0元") + "起购  |  " + StringUtils.excludeNull(product.getProductType(), "未知"));
        GlideUtil.showCommonImage(mContext, product.getUrl(), holder.ivCover);
        int visibility=StringUtils.isEquals("1", product.getIsBought()) ? View.VISIBLE : View.GONE;
        holder.llBoughtContainer.setVisibility(visibility);
        holder.vDivider.setVisibility(visibility);
        return view;
    }
}
