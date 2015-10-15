package com.google.wash;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.wash.database.DBStorage;
import com.google.wash.entity.Clothes;
import com.google.wash.utils.Const;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ChooseActivity extends Activity implements OnClickListener{
	
	private int x=1;
	private int y;
	private DBStorage dbstorage;
	private Clothes clothes=new Clothes();
	@ViewInject(R.id.imageViewGoods)
	private ImageView goodsImage;
	@ViewInject(R.id.textViewGoodsName)
	private TextView goodsName;
	@ViewInject(R.id.textViewPrice)
	private TextView price;
	@ViewInject(R.id.textViewDes)
	private TextView des;
	@ViewInject(R.id.textViewDetail)
	private TextView detail;
	@ViewInject(R.id.textViewUnit)
	private TextView unit;
    @ViewInject(R.id.buttonDelete)
	private Button btDelete;
    @ViewInject(R.id.buttonAdd)
    private Button btAdd;
    @ViewInject(R.id.textViewNum)
    private TextView num;
    @ViewInject(R.id.buttonAddto)
    private Button btAddTo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.choose);
	    
	    Const.activityList.add(this);
	    
	    ViewUtils.inject(this);
	    dbstorage=new DBStorage(this);
		dbstorage.openOrCreateDataBase();
	    
	    y=getIntent().getIntExtra("detail", 6);
	    btDelete.setOnClickListener(this);
	    btAdd.setOnClickListener(this);
	    btAddTo.setOnClickListener(this);
	    initView();
	}
    
	private void initView(){
		for (int i = 0; i < clothes.getImages().length; i++) {
			if (y==i) {
				goodsImage.setImageResource(clothes.getImages()[i]);
				goodsName.setText(clothes.getNames()[i]);
				price.setText(clothes.getPrices()[i]);
				unit.setText("/件");
				des.setText("");
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonDelete:
			x--;
			if (x<1) {
				x=1;
			}
			num.setText(x+"");
			break;
		case R.id.buttonAdd:
			x++;
			num.setText(x+"");
			break;
		case R.id.buttonAddto:
			Intent intent = new Intent(ChooseActivity.this,MainActivity.class);
			Cursor cursor = dbstorage.selectAll();
			while (cursor.moveToNext()) {
				if (cursor.getString(3).equals(clothes.getNames()[y])) {
					Map<String,String> cloth = new HashMap<String, String>();
					cloth.put("name", clothes.getNames()[y]);
					cloth.put("image", ""+clothes.getImages()[y]);
					cloth.put("price", clothes.getPrices()[y]);
					x = x + Integer.parseInt(cursor.getString(2));
					cloth.put("num",""+x);
					dbstorage.update(cloth);
					intent.putExtra("num", x);
					startActivity(intent);
					finish();
					return;
				}
			}
			Map<String,String> cloth = new HashMap<String, String>();
			cloth.put("name", clothes.getNames()[y]);
			cloth.put("image", ""+clothes.getImages()[y]);
			cloth.put("price", clothes.getPrices()[y]);
			cloth.put("num",""+ x);
			dbstorage.insert(cloth);
			intent.putExtra("num", x);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbstorage.dataBaseClose();
	}
	
}
