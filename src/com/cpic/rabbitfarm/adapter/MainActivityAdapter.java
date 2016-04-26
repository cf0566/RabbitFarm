package com.cpic.rabbitfarm.adapter;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.cpic.rabbitfarm.R;
import com.cpic.rabbitfarm.activity.MainActivity;
import com.cpic.rabbitfarm.adapter.FriendInviteAdapter.ViewHolder;
import com.cpic.rabbitfarm.bean.LandListInfo;
import com.cpic.rabbitfarm.utils.GlideRoundTransform;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MainActivityAdapter extends BaseAdapter{

	private ArrayList<LandListInfo> datas;
	private Context context;
	
	public MainActivityAdapter(Context context) {
		this.context = context;
	}
	public void SetDatas(ArrayList<LandListInfo> datas){
		this.datas = datas;
	}
	
	@Override
	public int getCount() {
		return datas == null ? 0 :datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_main_activity_plant, null);
			holder.ivPlant = (ImageView) convertView.findViewById(R.id.item_activity_main_iv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
//		Glide.with(context).load(datas.get(position).getImg()).placeholder(R.drawable.zhongzibaicai)
//		.into(holder.ivPlant);
		Log.i("oye", datas.get(position).getImg());
		
		return convertView;
	}

	class ViewHolder{
		ImageView ivPlant;
	}
}
