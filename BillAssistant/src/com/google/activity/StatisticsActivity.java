package com.google.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.AccountBookSelectAdapter;
import com.google.business.AccountBookBusiness;
import com.google.business.StatisticsBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;
import com.google.model.ModelAccountBook;

public class StatisticsActivity extends FrameActivity implements OnSlideMenuListener {

	private TextView tvStatisticsResult;
	private StatisticsBusiness mStatisticsBusiness;
	private ModelAccountBook mAccountBook;
	private AccountBookBusiness mAccountBookBusiness;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addMainBody(R.layout.statistics);
		initVariable();
		initView();
		initListeners();
		bindData();
		setTitle();
		createSlideMenu(R.array.SlideMenuStatistics);
	}
	
	private void setTitle() {
		String titel = getString(R.string.ActivityTitleStatistics, new Object[]{mAccountBook.getAccountBookName()});
		setTopBarTitle(titel);
	}

	protected void initView() {
		tvStatisticsResult = (TextView) findViewById(R.id.tvStatisticsResult);
	}

	protected void initListeners() {
	}
	
	protected void initVariable() {
		mStatisticsBusiness = new StatisticsBusiness(StatisticsActivity.this);
		mAccountBookBusiness = new AccountBookBusiness(StatisticsActivity.this);
		mAccountBook = mAccountBookBusiness.getDefaultModelAccountBook();
	}

	protected void bindData()
	{
		ShowProgressDialog(R.string.StatisticsProgressDialogTitle, R.string.StatisticsProgressDialogWaiting);
		new BindDataThread().start();
	}
	
	private class BindDataThread extends Thread
	{
		@Override
		public void run() {
			String result = mStatisticsBusiness.GetPayoutUserIDByAccountBookID(mAccountBook.getAccountBookID());
			Message message = new Message();
			message.obj = result;
			message.what = 1;
			mHandler.sendMessage(message);
		}
	}
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				tvStatisticsResult.setText(result);
				DismissProgressDialog();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideMenuToggle();
		if (pSlideMenuItem.getItemID() == 0) {
			showAccountBookSelectDialog();
		}
		if(pSlideMenuItem.getItemID() == 1) {
			ExportData();
		}
	}
	
	private void showAccountBookSelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView)view.findViewById(R.id.ListViewSelect);
		AccountBookSelectAdapter adapterAccountBookSelect = new AccountBookSelectAdapter(this);
		listView.setAdapter(adapterAccountBookSelect);

		builder.setTitle(R.string.ButtonTextSelectAccountBook);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
	}
	
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog)
		{
			m_AlertDialog = pAlertDialog;
		}
		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPosition,
				long arg3) {
			mAccountBook = (ModelAccountBook)pAdapterView.getAdapter().getItem(pPosition);
			bindData();
			m_AlertDialog.dismiss();
		}
	}
	
	
	private void ExportData() {
		String result = "";
		try {
			result = mStatisticsBusiness.ExportStatistics(mAccountBook.getAccountBookID());
		} catch (Exception e) {
			result = "导出失败";
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}
