package com.google.wash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserInfoActivity extends Activity {
    
	@ViewInject(R.id.editText_surname)
	private EditText edit_surname;
	@ViewInject(R.id.editText_phone)
	private EditText edit_phonenum;
	@ViewInject(R.id.editText_plot)
	private EditText edit_plot;
	@ViewInject(R.id.editText_detail)
	private EditText edit_detail;
	@ViewInject(R.id.spinnercell)
	private Spinner  cell;
	@ViewInject(R.id.group_gender)
	private RadioGroup gender;
	@ViewInject(R.id.button_save)
	private Button save;
	@ViewInject(R.id.ivBack)
	private ImageView imageBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		ViewUtils.inject(this);
	}
	@OnClick({R.id.ivBack,R.id.editText_surname,R.id.editText_phone,
		R.id.editText_plot,R.id.editText_detail,R.id.spinnercell,
		R.id.group_gender,R.id.button_save})
	private void click(View v){
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		case R.id.button_save:
			int id = gender.getCheckedRadioButtonId();
			String gender = id==R.id.radio0?"先生":"女士";
			int pos = cell.getSelectedItemPosition();
			String cell=null;
			switch (pos) {
			case 0:
				cell="青羊区";
				break;
			case 1:
				cell="蓝羊区";
				break;
			case 2:
				cell="黑羊区";
				break;
			case 3:
				cell="红羊区";
				break;
			default:
				break;
			}
			String surname= edit_surname.getText().toString();
			String phonenum= edit_phonenum.getText().toString();
			String plot= edit_plot.getText().toString();
			String detail= edit_detail.getText().toString();
			if (!surname.equals("")&&!phonenum.equals("")&&!plot.equals("")&&!detail.equals("")) {
				Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
				intent.putExtra("name",surname+gender);
				intent.putExtra("phonenum", phonenum);
				intent.putExtra("address","成都市"+cell+plot+detail);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(this,"地址填写不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
}
