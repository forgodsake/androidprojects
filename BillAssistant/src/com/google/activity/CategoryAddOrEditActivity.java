package com.google.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.business.CategoryBusiness;
import com.google.model.ModelCategory;
import com.google.utility.RegexTools;

public class CategoryAddOrEditActivity extends FrameActivity
	implements android.view.View.OnClickListener
{
	
	private Button btnSave;
	private Button btnCancel;
	
	private CategoryBusiness mBusinessCategory;
	private ModelCategory mModelCategory;
	private Spinner spParentID;
	private EditText etCategoryName;

	@Override
	public void onClick(View v) {
		int ID = v.getId();
		
		switch (ID) {
		case R.id.btnSave:
			addOrEditCategory();
			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addMainBody(R.layout.category_add_or_edit);
		removeBottomBox();
		initView();
		initVariable();
		bindData();
		setTitle();
		initListeners();
	}
	
	private void setTitle() {
		String title;
		if(mModelCategory == null)
		{
			title = getString(R.string.ActivityTitleCategoryAddOrEdit, new Object[]{getString(R.string.TitleAdd)});
		}
		else {
			title = getString(R.string.ActivityTitleCategoryAddOrEdit, new Object[]{getString(R.string.TitleEdit)});
			initData(mModelCategory);
		}
		
		setTopBarTitle(title);
	}

	protected void initView() {
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		etCategoryName = (EditText)findViewById(R.id.etCategoryName);
		spParentID = (Spinner)findViewById(R.id.SpinnerParentID);
	}

	protected void initListeners() {
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	

	protected void initVariable() {
		mBusinessCategory = new CategoryBusiness(this);
		mModelCategory = (ModelCategory) getIntent().getSerializableExtra("ModelCategory");
	}

	protected void bindData()
	{
		ArrayAdapter<ModelCategory> arrayAdapter = mBusinessCategory.getRootCategoryArrayAdapter();
		spParentID.setAdapter(arrayAdapter);
	}
	
	private void initData(ModelCategory pModelCategory)
	{
		etCategoryName.setText(pModelCategory.getCategoryName());
		ArrayAdapter arrayAdapter = (ArrayAdapter) spParentID.getAdapter();
		
		if(pModelCategory.getParentID() != 0)
		{
			int position = 0;
			for(int i=1;i<arrayAdapter.getCount();i++)
			{
				ModelCategory modelCategory = (ModelCategory)arrayAdapter.getItem(i);
				if(modelCategory.getCategoryID() == pModelCategory.getParentID())
				{
					position = arrayAdapter.getPosition(modelCategory);
				}
			}	
			spParentID.setSelection(position);
		}
		else {
			int count = mBusinessCategory.getNotHideCountByParentID(pModelCategory.getCategoryID());
			if(count != 0)
			{
				spParentID.setEnabled(false);
			}
		}
	}

	private void addOrEditCategory() {
		String categoryName = etCategoryName.getText().toString().trim();
		Boolean CheckResult = RegexTools.IsChineseEnglishNum(categoryName);
	    if(!CheckResult)
	    {
			Toast.makeText(this, getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{getString(R.string.TextViewTextCategoryName)}), 1).show();
	    	return;
	    }
		
		if(mModelCategory == null)
		{
			mModelCategory = new ModelCategory();
			mModelCategory.setTypeFlag(getString(R.string.PayoutTypeFlag));
			mModelCategory.setPath("");
		}
		mModelCategory.setCategoryName(categoryName);
		if(!spParentID.getSelectedItem().toString().equals("--Please Choose--"))
		{
			ModelCategory modelCategory = (ModelCategory)spParentID.getSelectedItem();
			if(modelCategory != null)
			{
				mModelCategory.setParentID(modelCategory.getCategoryID());
			}
		} else {
			mModelCategory.setParentID(0);
		}
		
		Boolean result = false;
		
		if(mModelCategory.getCategoryID() == 0)
		{
			result = mBusinessCategory.insertCategory(mModelCategory);
		}
		else {
			result = mBusinessCategory.updateCategoryByCategoryID(mModelCategory);
		}

		if(result)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddSucceed), 1).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddFail), 1).show();
		}
	}
}
