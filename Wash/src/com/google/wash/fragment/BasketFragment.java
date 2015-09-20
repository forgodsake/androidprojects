package com.google.wash.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.hardware.camera2.TotalCaptureResult;
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
import com.google.wash.entity.Clothes;
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
	private List<HashMap<String, Object>> list;
	private Clothes clothes=new Clothes();
	private int pos, num,sum;
	private ArrayList<String> list1,list2;
	
	@SuppressWarnings("unchecked")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.basket, container, false);
	    ViewUtils.inject(this, view);
		list = new ArrayList<HashMap<String,Object>>();
		adapter = new MySimpleAdapter(getActivity(), list, R.layout.basket_list_item, new String[]{"image","name","price","num"}, new int []{R.id.imageViewGoods,R.id.textViewName,R.id.textViewPrice,R.id.textViewNum});
		washList.setAdapter(adapter);
		
		num = getActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE).getInt("num", 0);
		pos = getActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE).getInt("position", 6);
	
		position=(HashSet<String>)getActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE ).getStringSet("saveposition", new HashSet<String>()) ;
		count = (HashSet<String>)getActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE ).getStringSet("savecount", new HashSet<String>()) ;
	    list1 = new ArrayList<String>(position);
	    sort(list1);
	    list2 = new ArrayList<String>(count);
	    sort(list2);
	    sum = 0;
	    for (int i = 0; i < list1.size(); i++) {
	    	String positioni = list1.get(i);
			String counti = list2.get(i);
			int x1 = Integer.parseInt(positioni); 
			int x2 = Integer.parseInt(counti); 
			 HashMap<String, Object>   map =  new HashMap<String, Object>();
			 map.put("image",clothes.getImages()[x1]);
			 map.put("name", clothes.getNames()[x1]);
			 int price = Integer.parseInt(clothes.getPrices()[x1])*(x2-100*x1);
			 map.put("price","￥"+ price );
			 map.put("num",x2-100*x1 );
			 list.add(map);
			 adapter.notifyDataSetChanged();
			 sum += price;
		}
			
		if (num>0) {
			
		    if(!position.contains(String.valueOf(pos))){
				position.add(String.valueOf(pos));
				count.add(String.valueOf(num+pos*100));
				HashMap<String, Object>   map =  new HashMap<String, Object>();
				map.put("image",clothes.getImages()[pos]);
				map.put("name", clothes.getNames()[pos]);
				int price =  Integer.parseInt(clothes.getPrices()[pos])*num;
				map.put("price","￥"+ price);
				map.put("num",num );
				list.add(map);
				adapter.notifyDataSetChanged();
				setlength(washList);
				sum+=price;
			}else {
				HashMap<String, Object>  map = list.get(position());
				int numold = Integer.parseInt(map.get("num").toString());
				int priceold = Integer.parseInt(map.get("price").toString().substring(1));
				count.remove(String.valueOf(numold+pos*100));
				map.put("num",num+numold );
				count.add(String.valueOf(numold+num+pos*100));
				saveData();
				int price = Integer.parseInt(clothes.getPrices()[pos])*num;
				map.put("price","￥"+ (price+priceold) );
				adapter.notifyDataSetChanged();
				setlength(washList);
				sum+=price;
			};
			//添加完毕清除记录
			getActivity().getSharedPreferences("myShared", Context.MODE_APPEND).edit().putInt("num", 0).commit();
			getActivity().getSharedPreferences("myShared", Context.MODE_APPEND).edit().putInt("position", 6).commit();
			saveData();
		}
		if (list.size()!=0) {
	    	blank.setVisibility(View.GONE);
		}
		text_totally.setText("￥"+sum);
		int pay = (sum-30)>0?(sum-30):0;
		text_total.setText("￥"+pay);
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
			adapter.notifyDataSetChanged();
			blank.setVisibility(View.VISIBLE);
			if (position!=null) {
				position.clear();
				position = null;
				count.clear();
				count = null;
				saveData();
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
				adapter.notifyDataSetChanged();
				blank.setVisibility(View.VISIBLE);
				if (position!=null) {
					position.clear();
					position = null;
					count.clear();
					count = null;
					saveData();
				}
			}
		}
	}
	
	//计算重复添加衣物的位置
	public int position(){
		 sort(list1);
		 for (int i = 0; i< list1.size(); i++) {
			 String positioni = list1.get(i);
			 if (Integer.parseInt(positioni)==pos) {
					return i;
		     }
		   }
		 return 0;
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
                switch (msg.what) {  
                case 1:  
                	TextView text_price0 = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewPrice);
                	TextView text_num = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewNum);
                	int priceTotal0 = Integer.parseInt(text_price0.getText().toString().substring(1));
                	int num = Integer.parseInt(text_num.getText().toString());
                	int sum0 = Integer.parseInt(text_totally.getText().toString().substring(1));
                	text_totally.setText("￥"+(sum-priceTotal0));
            		int pay0 = (sum-30)>0?(sum-30):0;
            		text_total.setText("￥"+(pay0-priceTotal0));
                	list.remove(msg.arg1);  
                	position.remove(pos);
                	count.remove(num+pos*100);
                    notifyDataSetChanged();  
                    setlength(washList);
                    if (list.size()==0) {
                    	blank.setVisibility(View.VISIBLE);
					}
                    break;  
                case 2:  
                	TextView text_price = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewPrice);
                	TextView text_num_del = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewNum);
                	int sum = Integer.parseInt(text_totally.getText().toString().substring(1));
                	int priceTotal = Integer.parseInt(text_price.getText().toString().substring(1));
                	int numdel = Integer.parseInt(text_num_del.getText().toString());
                	int price = priceTotal/numdel;
                	numdel--;
                	int total = numdel*price;
                	if (numdel>0) {
                		text_num_del.setText(numdel+"");
                		text_price.setText("￥"+total);
                		text_totally.setText("￥"+(sum+total-priceTotal));
                		int pay = (sum-30)>0?(sum-30):0;
                		text_total.setText("￥"+(pay+total-priceTotal));
					}else {
						text_num_del.setText("1");
						text_price.setText("￥"+price);
					}
                	break;  
                case 3:  
                	TextView text_price1 = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewPrice);
                	TextView text_num_add = (TextView) washList.getChildAt(msg.arg1).findViewById(R.id.textViewNum);
                	int sum1 = Integer.parseInt(text_totally.getText().toString().substring(1));
                	int priceTotal1 = Integer.parseInt(text_price1.getText().toString().substring(1));
                	int numadd = Integer.parseInt(text_num_add.getText().toString());
                	int price1 = priceTotal1/numadd;
                	numadd++;
                	int total1=numadd*price1;
                	text_num_add.setText(numadd+"");
                	text_price1.setText("￥"+total1);
                	text_totally.setText("￥"+(sum1+total1-priceTotal1));
            		int pay = (sum1-30)>0?(sum1-30):0;
            		text_total.setText("￥"+(pay+total1-priceTotal1));
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
		Editor editor =	getActivity().getSharedPreferences("myShared", Context.MODE_APPEND).edit();
		editor.putStringSet("savecount", count);
		editor.putStringSet("saveposition", position);
		editor.commit();
	}
	
	//对set进行排序 从而按照点击位置找出相关数据
	private void sort(ArrayList<String> list){
	    Collections.sort(list, new Comparator() {
	        @Override
	        public int compare(Object o1, Object o2) {
	          return new Double((String) o1).compareTo(new Double((String) o2));
	        }
	      });
	}
}
