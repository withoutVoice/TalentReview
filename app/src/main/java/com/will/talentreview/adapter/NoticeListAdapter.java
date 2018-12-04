package com.will.talentreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.entity.NoticeInfo;
import com.will.talentreview.utils.DateUtils;
import com.will.talentreview.utils.StringUtils;

import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-29
 * 公告列表适配器
 */

public class NoticeListAdapter extends BaseAdapter{

    private Context mContext;
    private List<NoticeInfo> mNotices;
    private LayoutInflater mInflater;

    public NoticeListAdapter(Context context, List<NoticeInfo> noticeInfoList){
        this.mContext=context;
        this.mNotices=noticeInfoList;
        this.mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mNotices.size();
    }

    @Override
    public NoticeInfo getItem(int i) {
        return mNotices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView tvTitle;
        TextView tvDate;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            view=mInflater.inflate(R.layout.item_notice_list,null);
            holder=new ViewHolder();
            holder.tvTitle=view.findViewById(R.id.tv_title);
            holder.tvDate=view.findViewById(R.id.tv_date);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        NoticeInfo notice=getItem(i);
        holder.tvTitle.setText(StringUtils.excludeNull(notice.getTitle()));
        holder.tvDate.setText(notice.getCreateTime());
        return view;
    }
}
