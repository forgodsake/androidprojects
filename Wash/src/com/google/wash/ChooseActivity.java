package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.wash.entity.Clothes;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ChooseActivity extends Activity implements OnClickListener{
	
	private int x=1;
	private int y;
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
	    ViewUtils.inject(this);
	    
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
			intent.putExtra("num", x);
			intent.putExtra("position", y);
			Editor editor = getSharedPreferences("myShared", MODE_APPEND).edit();
			editor.putInt("num", x);
			editor.putInt("position", y);
			editor.commit();
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
	}
	
}
