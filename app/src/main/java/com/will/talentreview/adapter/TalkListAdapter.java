package com.will.talentreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.entity.LeaveTalk;
import com.will.talentreview.utils.DateUtils;
import com.will.talentreview.utils.GlideUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CircleImageView;

import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-30
 * 留言列表适配器
 */

public class TalkListAdapter extends BaseAdapter {

    private Context mContext;
    private List<LeaveTalk> mTalks;
    private LayoutInflater mInflater;

    public TalkListAdapter(Context context, List<LeaveTalk> talks) {
        this.mContext = context;
        this.mTalks = talks;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mTalks.size();
    }

    @Override
    public LeaveTalk getItem(int i) {
        return mTalks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        CircleImageView ivAvatar;
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_talk_list, null);
            holder = new ViewHolder();
            holder.ivAvatar = view.findViewById(R.id.iv_avatar);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvDate = view.findViewById(R.id.tv_date);
            holder.tvContent = view.findViewById(R.id.tv_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        LeaveTalk talk = getItem(i);
        GlideUtil.showAvatar(mContext, talk.getUrl(), holder.ivAvatar);
        holder.tvName.setText(StringUtils.excludeNull(talk.getNickName(), "未知"));
        holder.tvDate.setText(StringUtils.excludeNull(talk.getCreateTime(), DateUtils.getYyyyMMdd(System.currentTimeMillis())));
        holder.tvContent.setText(StringUtils.excludeNull(talk.getContent()));
        return view;
    }
}
