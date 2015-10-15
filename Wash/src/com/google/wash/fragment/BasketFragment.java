package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.wash.PayActivity;
import com.google.wash.R;
import com.google.wash.UserInfoActivity;
import com.google.wash.database.DBStorage;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class BasketFragment extends Fragment {
    
	@ViewInject(R.id.rl_change)
	private RelativeLayout rl_change;
	@ViewInject(R.id.rl_save)
	private RelativeLayout rl_save;
	@ViewInject(R.id.textView_name)
    private TextView text_name;	
	@ViewInject(R.id.textView_phonenum)
	private TextView text_phone;
	@ViewInject(R.id.textView_address)
	private TextView text_address;
	@ViewInject(R.id.tvDeleteOrder)
	private TextView tvDeleteAll;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@ViewInject(R.id.imageViewBlank)
	private ImageView blank;
	@ViewInject(R.id.listViewAdd)
	private ListView washList;
	@ViewInject(R.id.textViewwrite)
	private TextView tvwrite;
	@ViewInject(R.id.imageView_pay)
	private ImageView imagepay;
	@ViewInject(R.id.textViewTotally)
	private TextView text_totally;
	@ViewInject(R.id.textViewTotal)
	private TextView text_total;
	private Set<String> position,count ;
	private SimpleAdapter adapter;
	private int sum;
    private ArrayList<Map<String, String>> list;
	private DBStorage dbstorage;
	
	@SuppressWarnings("unchecked")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.basket, container, false);
	    ViewUtils.inject(this, view);
	    dbstorage=new DBStorage(getActivity());
		dbstorage.openOrCreateDataBase();
		Cursor cursor = dbstorage.selectAll();
		list = new ArrayList<Map<String,String>>();
		if (!"".equals(cursor)&&cursor!=null) {
			
			while (cursor.moveToNext()) {
				Map<String,String> cloth = new HashMap<String, String>();
				cloth.put("image", cursor.getString(1));
				cloth.put("num",cursor.getString(2));
				cloth.put("name", cursor.getString(3));
				cloth.put("price", cursor.getString(4));
				list.add(cloth);
				sum += Integer.parseInt(cursor.getString(2))*Integer.parseInt(cursor.getString(4));
			}
		}
		adapter = new MySimpleAdapter(getActivity(), list, R.layout.basket_list_item, new String[]{"image","name","price","num"}, new int []{R.id.imageViewGoods,R.id.textViewName,R.id.textViewPrice,R.id.textViewNum});
		washList.setAdapter(adapter);
		
		
		
		if (list.size()!=0) {
	    	blank.setVisibility(View.GONE);
		}
		text_totally.setText(""+sum);
		int pay = (sum-30)>0?(sum-30):0;
		text_total.setText(""+pay);
		
	    return view;
	}
	@OnClick({R.id.ivBack,R.id.imageView_pay,R.id.tvDeleteOrder,R.id.rl_change})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			ViewPager pager=(ViewPager) getActivity().findViewById(R.id.fragment);
		    pager.setCurrentItem(0);
			break;
		case R.id.rl_change:
			Intent intent = new Intent(getActivity(),UserInfoActivity.class);
	    	startActivity(intent);
			break;
		case R.id.imageView_pay:
			  if (rl_change.getVisibility()==View.VISIBLE) {
				Toast.makeText(getActivity(), "请先填写用户信息", Toast.LENGTH_SHORT).show();
			}else {
				startActivityForResult(new Intent(getActivity(),PayActivity.class),1);
			}
			break;
		case R.id.tvDeleteOrder:
			list.clear();
			dbstorage.deleteAll();
			adapter.notifyDataSetChanged();
			blank.setVisibility(View.VISIBLE);
			if (position!=null) {
				position.clear();
				position = null;
				count.clear();
				count = null;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity().getIntent().getStringExtra("name") != null) {
			rl_change.setVisibility(View.GONE);
			rl_save.setVisibility(View.VISIBLE);
			Intent  intent = getActivity().getIntent();
		    String name = intent.getStringExtra("name");
		    String phonenum = intent.getStringExtra("phonenum");
		    String address = intent.getStringExtra("address");
		    if (name!=null) {
				text_name.setText(name);
				text_phone.setText(phonenum);
				text_address.setText(address);
			}
		}
		setlength(washList);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==1) {
			if (resultCode==2) {
				list.clear();
				dbstorage.deleteAll();
				adapter.notifyDataSetChanged();
				blank.setVisibility(View.VISIBLE);
				if (position!=null) {
					position.clear();
					position = null;
					count.clear();
					count = null;
				}
			}
		}
	}
	
	
	private class MySimpleAdapter extends SimpleAdapter {  
		  
        public MySimpleAdapter(Context context,  
                List<? extends Map<String, ?>> data, int resource,  
                String[] from, int[] to) {  
            super(context, data, resource, from, to);  
        }  
        
        @Override
		public Object getItem(int position) {
			return super.getItem(position);
		}

		@Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            
            final int mPosition = position;  
            convertView = super.getView(position, convertView, parent);  
            
            ImageView itemDelete = (ImageView) convertView  
                    .findViewById(R.id.imageView_delete);  
            itemDelete.setOnClickListener(new View.OnClickListener() {  
  
                @Override  
                public void onClick(View v) {  
                   
                    mHandler.obtainMessage(1, mPosition, 0)  
                            .sendToTarget();  
                }  
            });  
            Button buttonDelete = (Button) convertView  
            		.findViewById(R.id.buttonDelete);  
            buttonDelete.setOnClickListener(new View.OnClickListener() {  
            	
            	@Override  
            	public void onClick(View v) {  
            		
            		mHandler.obtainMessage(2, mPosition, 0)  
            		.sendToTarget();  
            	}  
            });  
            Button buttonAdd = (Button) convertView  
            		.findViewById(R.id.buttonAdd);  
            buttonAdd.setOnClickListener(new View.OnClickListener() {  
            	
            	@Override  
            	public void onClick(View v) {  
            		
            		mHandler.obtainMessage(3, mPosition, 0)  
            		.sendToTarget();  
            	}  
            });  
            return convertView;  
        }  
  
        private Handler mHandler = new Handler() {  
  
            @Override  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg); 
                TextView text_price = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewPrice);
            	TextView text_num = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewNum);
            	int price = Integer.parseInt(text_price.getText().toString());
            	int num = Integer.parseInt(text_num.getText().toString());
            	int sum = Integer.parseInt(text_totally.getText().toString());
            	int pay = (sum-price*num-30)>0?(sum-price*num-30):0;
            	String name=list.get(msg.arg1).get("name");
            	switch (msg.what) {  
                case 1: 
                	text_totally.setText(""+(sum-price*num));
            		text_total.setText(""+pay);
            		dbstorage.delete(name);
                	list.remove(msg.arg1);  
                    notifyDataSetChanged();  
                    setlength(washList);
                    if (list.size()==0) {
                    	blank.setVisibility(View.VISIBLE);
					}
                    break;  
                case 2:  
                	pay = (sum-price-30)>0?(sum-price-30):0;
                	if (num>1) {
						num--;
						dbstorage.update_num(num, name);
						text_num.setText(num+"");
						text_totally.setText(""+(sum-price));
						text_total.setText(""+pay);
					}
					
                	break;  
                case 3: 
                	pay = (sum+price-30)>0?(sum+price-30):0;
                	num++;
                	dbstorage.update_num(num, name);
                	text_num.setText(num+"");
                	text_totally.setText(""+(sum+price));
            		text_total.setText(""+pay);
                	break;  
                }  
            }  
        };  
    }
	//测算listview的item数量并调整长度
	private void setlength(ListView list){
		  //手动设置listview长度
	    int totalHeight = 0;
	    for (int i = 0; i < adapter.getCount(); i++) {
	    	View listItem = adapter.getView(i, null, list);
	    	listItem.measure(0, 0);
	    	totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = list.getLayoutParams();
	    params.height = totalHeight + (list.getDividerHeight() * (adapter.getCount() - 1));
	    list.setLayoutParams(params);
	};
	
	//保存数据
	private void saveData(){
	
	}
	
	//对set进行排序 从而按照点击位置找出相关数据
//	private void sort(ArrayList<String> list){
//	    Collections.sort(list, new Comparator<Object>() {
//	        @Override
//	        public int compare(Object o1, Object o2) {
//	          return new Double((String) o1).compareTo(new Double((String) o2));
//	        }
//	      });
//	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbstorage.dataBaseClose();
	}
	
	
	
}
