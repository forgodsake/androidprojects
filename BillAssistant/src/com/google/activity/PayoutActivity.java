package com.google.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.AccountBookSelectAdapter;
import com.google.adapter.PayoutAdapter;
import com.google.business.AccountBookBusiness;
import com.google.business.PayoutBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;
import com.google.model.ModelAccountBook;
import com.google.model.ModelPayout;

public class PayoutActivity extends FrameActivity implements OnSlideMenuListener {

	private ListView lvPayoutList;
	private ModelPayout mSelectModelPayout;
	private PayoutBusiness mPayoutBusiness;
	private PayoutAdapter mPayoutAdapter;
	private ModelAccountBook mAccountBook;
	private AccountBookBusiness mBusinessAccountBook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addMainBody(R.layout.payout_list);
		initVariable();
		initView();
		initListeners();
		bindData();
		createSlideMenu(R.array.SlideMenuPayout);
	}
	
	private void setTitle() {
		int count = lvPayoutList.getCount();
		String title = getString(R.string.ActivityTitlePayout, new Object[]{mAccountBook.getAccountBookName(),count});
		setTopBarTitle(title);
	}

	protected void initView() {
		lvPayoutList = (ListView) findViewById(R.id.lvPayoutList);
	}

	protected void initListeners() {
		registerForContextMenu(lvPayoutList);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenuInfo pMenuInfo) {
		//
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo)pMenuInfo;
		ListAdapter listAdapter = lvPayoutList.getAdapter();
		mSelectModelPayout = (ModelPayout)listAdapter.getItem(adapterContextMenuInfo.position);
		pContextMenu.setHeaderIcon(R.drawable.payout_small_icon);
		pContextMenu.setHeaderTitle(mSelectModelPayout.getCategoryName());
		int ItemID[] = {1,2};
		CreateMenu(pContextMenu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem pItem) {
		switch (pItem.getItemId()) {
		case 1:
			Intent intent = new Intent(this, PayoutAddOrEditActivity.class);
			intent.putExtra("ModelPayout", mSelectModelPayout);
			this.startActivityForResult(intent, 1);
			break;
		case 2:
			delete(mSelectModelPayout);
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(pItem);
	}

	protected void initVariable() {
		mPayoutBusiness = new PayoutBusiness(PayoutActivity.this);
		mBusinessAccountBook = new AccountBookBusiness(PayoutActivity.this);
		mAccountBook = mBusinessAccountBook.getDefaultModelAccountBook();
		mPayoutAdapter = new PayoutAdapter(this,mAccountBook.getAccountBookID());
	}

	protected void bindData()
	{
		PayoutAdapter payoutAdapter = new PayoutAdapter(this,mAccountBook.getAccountBookID());
		lvPayoutList.setAdapter(payoutAdapter);
		setTitle();
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideMenuToggle();
		if (pSlideMenuItem.getItemID() == 0) {
			showAccountBookSelectDialog();
		}
	}
	
	private void showAccountBookSelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView)view.findViewById(R.id.ListViewSelect);
		AccountBookSelectAdapter AdapterAccountBookSelect = new AccountBookSelectAdapter(this);
		listView.setAdapter(AdapterAccountBookSelect);

		builder.setTitle(R.string.ButtonTextSelectAccountBook);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
	}
	
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog mAlertDialog;
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog)
		{
			mAlertDialog = pAlertDialog;
		}
		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPosition,
				long arg3) {
			mAccountBook = (ModelAccountBook)pAdapterView.getAdapter().getItem(pPosition);
			bindData();
			mAlertDialog.dismiss();
		}
	}
	
	public void delete(ModelPayout pModelPayout)
	{
		Object object[] = {pModelPayout.getCategoryName()}; 	
		String message = getString(R.string.DialogMessagePayoutDelete,object);
		showAlertDialog(R.string.DialogTitleDelete,message,new OnDeleteClickListener());
	}
	
	private class OnDeleteClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			Boolean result = mPayoutBusiness.deletePayoutByPayoutID(mSelectModelPayout.getPayoutID());
			if(result == true)
			{
//				mPayoutAdapter.GetList().remove(mListViewPosition);
//				mPayoutAdapter.notifyDataSetChanged();
				bindData();
			}
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
