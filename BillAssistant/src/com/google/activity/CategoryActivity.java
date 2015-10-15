package com.google.activity;

import java.io.Serializable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.CategoryAdapter;
import com.google.business.CategoryBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;
import com.google.model.ModelCategory;
import com.google.model.ModelCategoryTotal;

public class CategoryActivity extends FrameActivity implements OnSlideMenuListener {
	
	private ExpandableListView elvCategoryList;
	private ModelCategory mSelectModelCategory;
	private CategoryBusiness mBusinessCategory;
	private CategoryAdapter mAdapterCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addMainBody(R.layout.category);
		initVariable();
		initView();
		initListeners();
		bindData();
		createSlideMenu(R.array.SlideMenuCategory);
	}
	
	private void setTitle() {
		int count = mBusinessCategory.getNotHideCount();
		String title = getString(R.string.ActivityTitleCategory, new Object[]{count});
		setTopBarTitle(title);
	}

	protected void initView() {
		elvCategoryList = (ExpandableListView) findViewById(R.id.ExpandableListViewCategory);
	}

	protected void initListeners() {
		registerForContextMenu(elvCategoryList);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenuInfo pMenuInfo) {
		ExpandableListContextMenuInfo expandableListContextMenuInfo = (ExpandableListContextMenuInfo)pMenuInfo;
		long position = expandableListContextMenuInfo.packedPosition;
		int type = ExpandableListView.getPackedPositionType(position);
		int groupPosition = ExpandableListView.getPackedPositionGroup(position);

		switch (type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			mSelectModelCategory = (ModelCategory)mAdapterCategory.getGroup(groupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int childPosition = ExpandableListView.getPackedPositionChild(position);
			mSelectModelCategory = (ModelCategory)mAdapterCategory.getChild(groupPosition, childPosition);
			break;
		default:
			break;
		}
		
		pContextMenu.setHeaderIcon(R.drawable.category_small_icon);
		if(mSelectModelCategory != null)
		{
			pContextMenu.setHeaderTitle(mSelectModelCategory.getCategoryName());
		}
		CreateMenu(pContextMenu);
		if(mAdapterCategory.GetChildCountOfGroup(groupPosition) != 0 && mSelectModelCategory.getParentID() == 0)
		{
			pContextMenu.findItem(2).setEnabled(false);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem p_Item) {
		Intent intent;
		switch (p_Item.getItemId()) {
		case 1:
			intent = new Intent(this, CategoryAddOrEditActivity.class);
			intent.putExtra("ModelCategory", mSelectModelCategory);
			this.startActivityForResult(intent, 1);
			break;
		case 2:
			delete(mSelectModelCategory);
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(p_Item);
	}

	protected void initVariable() {
		mBusinessCategory = new CategoryBusiness(CategoryActivity.this);
	}

	protected void bindData()
	{
		mAdapterCategory = new CategoryAdapter(this);
		elvCategoryList.setAdapter(mAdapterCategory);
		setTitle();
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideMenuToggle();
		if (pSlideMenuItem.getItemID() == 0) {
			Intent _Intent = new Intent(this, CategoryAddOrEditActivity.class);
			this.startActivityForResult(_Intent, 1);
			
			return;
		}
		
		if (pSlideMenuItem.getItemID() == 1) {
			List<ModelCategoryTotal> list = mBusinessCategory.categoryTotalByRootCategory();
			Intent intent = new Intent();
			intent.putExtra("Total", (Serializable)list);
			intent.setClass(this, CategoryChartActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void delete(ModelCategory pModelCategory)
	{
		Object object[] = {pModelCategory.getCategoryName()}; 	
		String message = getString(R.string.DialogMessageCategoryDelete,object);
		showAlertDialog(R.string.DialogTitleDelete,message,new CategoryDeleteClickListener(pModelCategory));
	}
	
	private class CategoryDeleteClickListener implements DialogInterface.OnClickListener {
		private ModelCategory mModelCategory;
		public CategoryDeleteClickListener(ModelCategory pModelCategory)
		{
			mModelCategory = pModelCategory;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Boolean result;
				result = mBusinessCategory.hideCategoryByByPath(mModelCategory.getPath());
				if(result == true)
				{
					bindData();
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.TipsDeleteFail, 1).show();
				}
		}
		
	}
}
