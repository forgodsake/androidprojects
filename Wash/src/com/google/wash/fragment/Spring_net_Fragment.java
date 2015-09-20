package com.google.wash.fragment;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.wash.ChooseActivity;
import com.google.wash.R;
import com.google.wash.entity.ClothesFromNet;
import com.google.wash.entity.ClothesFromNet.WashInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.image.SmartImageView;

public class Spring_net_Fragment extends Fragment {
    
	@ViewInject(R.id.gridViewClothes)
	private GridView gridView;
	private MyAdapter adapter;
	private String jsonString;
	List<WashInfo> list_net;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.spring_wash, container,false);
		ViewUtils.inject(this, view);
		
		new Thread(){
			public void run() {
				try {
					loadData();
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					handler.sendEmptyMessage(2);
				}
			};
		}.start();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),ChooseActivity.class);
				intent.putExtra("detail", position);
				startActivity(intent);
			}
		});
		return view;
	}
	
	private void loadData() throws Exception{
		HttpClient client = new DefaultHttpClient();
		String uri = "http://192.168.1.101:8080/demo/myClive?clive=wash&url=192.168.1.101";
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
			jsonString = EntityUtils.toString(response.getEntity());
			ClothesFromNet clothes = new Gson().fromJson(jsonString, ClothesFromNet.class);
			list_net = clothes.washInfo;
		}
	}
	private class MyAdapter extends BaseAdapter {
        
		private List<WashInfo> washInfo;
		
		MyAdapter(List<WashInfo> list){
			washInfo = list;
		}
		
		@Override
		public int getCount() {
			
			return washInfo.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView==null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_piece_item_net, null);
				viewHolder.head = (SmartImageView) convertView.findViewById(R.id.imageViewGoods);
				viewHolder.name = (TextView) convertView.findViewById(R.id.textViewGoods);
				viewHolder.amount = (TextView) convertView.findViewById(R.id.textViewPrice);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.head.setImageUrl(washInfo.get(position).getWashHead());
			viewHolder.name.setText(washInfo.get(position).getWashName());
			viewHolder.amount.setText(washInfo.get(position).getAmount());
			
			return convertView;
		}
		
		public class ViewHolder{
			 public SmartImageView  head;
			 public TextView  name;
			 public TextView  amount;
		}
		
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter = new MyAdapter(list_net);
				gridView.setAdapter(adapter);
				break;
			case 2:
				Toast.makeText(getActivity(), "您未打开网络或服务器", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		};
	};
	
}
