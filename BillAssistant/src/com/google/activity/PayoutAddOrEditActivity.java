package com.google.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.AccountBookSelectAdapter;
import com.google.adapter.CategoryAdapter;
import com.google.adapter.UserAdapter;
import com.google.business.AccountBookBusiness;
import com.google.business.CategoryBusiness;
import com.google.business.PayoutBusiness;
import com.google.business.UserBusiness;
import com.google.controls.NumberDialog;
import com.google.controls.NumberDialog.OnNumberDialogListener;
import com.google.model.ModelAccountBook;
import com.google.model.ModelCategory;
import com.google.model.ModelPayout;
import com.google.model.ModelUser;
import com.google.utility.DateTools;
import com.google.utility.RegexTools;

public class PayoutAddOrEditActivity extends FrameActivity
	implements android.view.View.OnClickListener,OnNumberDialogListener
{
	
	private Button btnSave;
	private Button btnCancel;
	
	private ModelPayout mModelPayout;
	private PayoutBusiness mPayoutBusiness;
	private Integer mAccountBookID;
	private Integer mCategoryID;
	private String mPayoutUserID;
	private String mPayoutTypeArr[];
		
	private EditText etAccountBookName;
	private EditText etAmount;
	private AutoCompleteTextView actvCategoryName;
	private EditText etPayoutDate;
	private EditText etPayoutType;
	private EditText etPayoutUser;
	private EditText etComment;
	private Button btnSelectCategory;
	private Button btnSelectUser;
	private Button btnSelectAccountBook;
	private Button btnSelectAmount;
	private Button btnSelectPayoutDate;
	private Button btnSelectPayoutType;
	
	private AccountBookBusiness mBusinessAccountBook;
	private CategoryBusiness mBusinessCategory;
	private ModelAccountBook mModelAccountBook;
	private UserBusiness mUserBusiness;
	private List<RelativeLayout> mItemColor;
	private List<ModelUser> mUserSelectedList;

	@Override
	public void onClick(View v) {
		int _ID = v.getId();
		switch (_ID) {
		case R.id.btnSelectAccountBook:
			showAccountBookSelectDialog();
			break;
		case R.id.btnSelectAmount:
			(new NumberDialog(this)).show();
			break;
		case R.id.btnSelectCategory:
			showCategorySelectDialog();
			break;
		case R.id.btnSelectPayoutDate:
			Calendar _Calendar = Calendar.getInstance();
			showDateSelectDialog(_Calendar.get(Calendar.YEAR), _Calendar.get(Calendar.MONTH), _Calendar.get(Calendar.DATE));
			break;
		case R.id.btnSelectPayoutType:
			showPayoutTypeSelectDialog();
			break;
		case R.id.btnSelectUser:
			showUserSelectDialog(etPayoutType.getText().toString());
			break;
		case R.id.btnSave:
			addOrEditPayout();
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
		
		addMainBody(R.layout.payout_add_or_edit);
		removeBottomBox();
		initView();
		initVariable();
		bindData();
		setTitle();
		initListeners();
	}
	
	private void setTitle() {
		String title;
		if(mModelPayout == null)
		{
			title = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[]{getString(R.string.TitleAdd)});
		}
		else {
			title = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[]{getString(R.string.TitleEdit)});
			initData(mModelPayout);
		}
		
		setTopBarTitle(title);
	}

	protected void initView() {
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnSelectAccountBook = (Button)findViewById(R.id.btnSelectAccountBook);
		btnSelectAmount = (Button)findViewById(R.id.btnSelectAmount);
		btnSelectCategory = (Button)findViewById(R.id.btnSelectCategory);
		btnSelectPayoutDate = (Button)findViewById(R.id.btnSelectPayoutDate);
		btnSelectPayoutType = (Button)findViewById(R.id.btnSelectPayoutType);
		btnSelectUser = (Button)findViewById(R.id.btnSelectUser);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		etPayoutDate = (EditText) findViewById(R.id.etPayoutDate);
		etPayoutType = (EditText) findViewById(R.id.etPayoutType);
		etAmount = (EditText) findViewById(R.id.etAmount);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		actvCategoryName = (AutoCompleteTextView) findViewById(R.id.actvCategoryName);
		etPayoutUser = (EditText) findViewById(R.id.etPayoutUser);
		etComment = (EditText) findViewById(R.id.etComment);
	}

	protected void initListeners() {
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSelectAmount.setOnClickListener(this);
		btnSelectCategory.setOnClickListener(this);
		btnSelectPayoutDate.setOnClickListener(this);
		btnSelectPayoutType.setOnClickListener(this);
		btnSelectUser.setOnClickListener(this);
		actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
		btnSelectAccountBook.setOnClickListener(this);
	}
	

	protected void initVariable() {
		mPayoutBusiness = new PayoutBusiness(this);
		mModelPayout = (ModelPayout) getIntent().getSerializableExtra("ModelPayout");
		mBusinessAccountBook = new AccountBookBusiness(this);
		mBusinessCategory = new CategoryBusiness(this);
		mModelAccountBook = mBusinessAccountBook.getDefaultModelAccountBook();
		mUserBusiness = new UserBusiness(this);
	}

	protected void bindData()
	{
		
		mAccountBookID = mModelAccountBook.getAccountBookID();
		etAccountBookName.setText(mModelAccountBook.getAccountBookName());
		actvCategoryName.setAdapter(mBusinessCategory.getAllCategoryArrayAdapter());
		etPayoutDate.setText(DateTools.getFormatDateTime(new Date(),"yyyy-MM-dd"));
		mPayoutTypeArr = getResources().getStringArray(R.array.PayoutType);
		etPayoutType.setText(mPayoutTypeArr[0]);
	}
	
	private void initData(ModelPayout pModelPayout)
	{
		etAccountBookName.setText(pModelPayout.getAccountBookName());
		mAccountBookID = pModelPayout.getAccountBookID();
		etAmount.setText(pModelPayout.getAmount().toString());
		actvCategoryName.setText(pModelPayout.getCategoryName());
		mCategoryID = pModelPayout.getCategoryID();
		etPayoutDate.setText(DateTools.getFormatDateTime(pModelPayout.getPayoutDate(), "yyyy-MM-dd"));
		etPayoutType.setText(pModelPayout.getPayoutType());
		
		UserBusiness userBusiness = new UserBusiness(this);
		String userNameString = userBusiness.getUserNameByUserID(pModelPayout.getPayoutUserID());
		
		etPayoutUser.setText(userNameString);
		mPayoutUserID = pModelPayout.getPayoutUserID();
		etComment.setText(pModelPayout.getComment());
	}

	private void addOrEditPayout() {
		Boolean checkResult = checkData();
		if(checkResult == false)
		{
			return;
		}
		
		if(mModelPayout == null)
		{
			mModelPayout = new ModelPayout();
		}
		mModelPayout.setAccountBookID(mAccountBookID);
		mModelPayout.setCategoryID(mCategoryID);
		mModelPayout.setAmount(new BigDecimal(etAmount.getText().toString().trim()));
		mModelPayout.setPayoutDate(DateTools.getDate(etPayoutDate.getText().toString().trim(), "yyyy-MM-dd"));
		mModelPayout.setPayoutType(etPayoutType.getText().toString().trim());
		mModelPayout.setPayoutUserID(mPayoutUserID);
		mModelPayout.setComment(etComment.getText().toString().trim());
		
		Boolean _Result = false;
		
		if(mModelPayout.getPayoutID() == 0)
		{
			_Result = mPayoutBusiness.insertPayout(mModelPayout);
		}
		else {
			_Result = mPayoutBusiness.updatePayoutByPayoutID(mModelPayout);
		}

		if(_Result)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddSucceed), 1).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddFail), 1).show();
		}
	}
	
	private Boolean checkData() {
		Boolean checkResult = RegexTools.IsMoney(etAmount.getText().toString().trim());
		if(checkResult == false) 
		{
			etAmount.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextMoney), 1).show();
			return checkResult;
		}
		
		checkResult = RegexTools.IsNull(mCategoryID);
		if(checkResult == false) 
		{
			btnSelectCategory.setFocusable(true);
			btnSelectCategory.setFocusableInTouchMode(true);
			btnSelectCategory.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextCategoryIsNull), 1).show();
			return checkResult;
		}
		
		if(mPayoutUserID == null)
		{
			btnSelectUser.setFocusable(true);
			btnSelectUser.setFocusableInTouchMode(true);
			btnSelectUser.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUserIsNull), 1).show();
			return false;
		}
		
		String payoutType = etPayoutType.getText().toString();
		if(payoutType.equals(mPayoutTypeArr[0]) || payoutType.equals(mPayoutTypeArr[1]))
		{
			if(mPayoutUserID.split(",").length <= 1)
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser), 1).show();
				return false;
			}
		}
		else {
			if(mPayoutUserID.equals(""))
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser2), 1).show();
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void setNumberFinish(BigDecimal pNumber) {
		((EditText)findViewById(R.id.etAmount)).setText(pNumber.toString());
	}

	private class OnAutoCompleteTextViewItemClickListener
			implements AdapterView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPostion,
				long arg3) {
			ModelCategory modelCategory = (ModelCategory)pAdapterView.getAdapter().getItem(pPostion);
			mCategoryID = modelCategory.getCategoryID();
		}

	}
	
	private void showAccountBookSelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView)_View.findViewById(R.id.ListViewSelect);
		AccountBookSelectAdapter accountBookSelectAdapter = new AccountBookSelectAdapter(this);
		listView.setAdapter(accountBookSelectAdapter);

		builder.setTitle(R.string.ButtonTextSelectAccountBook);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(_View);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
	}
	
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog malertDialog;
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog)
		{
			malertDialog = pAlertDialog;
		}
		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPosition,
				long arg3) {
			ModelAccountBook modelAccountBook = (ModelAccountBook)pAdapterView.getAdapter().getItem(pPosition);
			etAccountBookName.setText(modelAccountBook.getAccountBookName());
			mAccountBookID = modelAccountBook.getAccountBookID();
			malertDialog.dismiss();
		}
	}
	
	private void showCategorySelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.category_select_list, null);
		ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.ExpandableListViewCategory);
		CategoryAdapter categoryAdapterSelect = new CategoryAdapter(this);
		expandableListView.setAdapter(categoryAdapterSelect);

		builder.setIcon(R.drawable.category_small_icon);
		builder.setTitle(R.string.ButtonTextSelectCategory);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		expandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(alertDialog,categoryAdapterSelect));
		expandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(alertDialog,categoryAdapterSelect));
	}
	
	private class OnCategoryGroupItemClickListener implements OnGroupClickListener
	{
		private AlertDialog malertDialog;
		private CategoryAdapter m_CategoryAdapter;
		
		public OnCategoryGroupItemClickListener(AlertDialog pAlertDialog,CategoryAdapter pCategoryAdapter)
		{
			malertDialog = pAlertDialog;
			m_CategoryAdapter = pCategoryAdapter;
		}
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			int count = m_CategoryAdapter.getChildrenCount(groupPosition);
			if(count == 0)
			{
				ModelCategory modelCategory = (ModelCategory)m_CategoryAdapter.getGroup(groupPosition);
				actvCategoryName.setText(modelCategory.getCategoryName());
				mCategoryID = modelCategory.getCategoryID();
				malertDialog.dismiss();
			}
			return false;
		}
	
	}
	
	private class OnCategoryChildItemClickListener implements OnChildClickListener
	{
		private AlertDialog malertDialog;
		private CategoryAdapter m_CategoryAdapter;
		
		public OnCategoryChildItemClickListener(AlertDialog pAlertDialog,CategoryAdapter pCategoryAdapter)
		{
			malertDialog = pAlertDialog;
			m_CategoryAdapter = pCategoryAdapter;
		}
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			ModelCategory modelCategory = (ModelCategory)m_CategoryAdapter.getChild(groupPosition, childPosition);
			actvCategoryName.setText(modelCategory.getCategoryName());
			mCategoryID = modelCategory.getCategoryID();
			malertDialog.dismiss();
			return false;
		}
	
	}
	
	private void showDateSelectDialog(int pYear,int pMonth,int pDay)
	{
		(new DatePickerDialog(this, new OnDateSelectedListener(), pYear, pMonth, pDay)).show();
	}
	
	private class OnDateSelectedListener implements OnDateSetListener
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			Date date = new Date(year-1900, monthOfYear, dayOfMonth);
			etPayoutDate.setText(DateTools.getFormatDateTime(date,"yyyy-MM-dd"));
		}
	}

	private void showPayoutTypeSelectDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.payout_type_select_list, null);
		ListView listView = (ListView)view.findViewById(R.id.ListViewPayoutType);

		builder.setTitle(R.string.ButtonTextSelectPayoutType);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnPayoutTypeItemClickListener(alertDialog));
	}
	
	private class OnPayoutTypeItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog malertDialog;
		public OnPayoutTypeItemClickListener(AlertDialog pAlertDialog)
		{
			malertDialog = pAlertDialog;
		}
		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPosition,
				long arg3) {
			String payoutType = (String)pAdapterView.getAdapter().getItem(pPosition);
			etPayoutType.setText(payoutType);
			etPayoutUser.setText("");
			mPayoutUserID = "";
			malertDialog.dismiss();
		}
	}
	
	private void showUserSelectDialog(String pPayoutType)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.user, null);
		LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.LinearLayoutMain);
		linearLayout.setBackgroundResource(R.drawable.blue);
		ListView listView = (ListView)view.findViewById(R.id.lvUserList);
		UserAdapter _UserAdapter = new UserAdapter(this);
		listView.setAdapter(_UserAdapter);

		builder.setIcon(R.drawable.user_small_icon);
		builder.setTitle(R.string.ButtonTextSelectUser);
		builder.setNegativeButton(R.string.ButtonTextBack, new OnSelectUserBack());
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnUserItemClickListener(alertDialog,pPayoutType));
	}
	
	private class OnSelectUserBack implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			etPayoutUser.setText("");
			String name = "";
			mPayoutUserID = "";
			if(mUserSelectedList != null)
			{
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					name += mUserSelectedList.get(i).getUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getUserID() + ",";
				}
				etPayoutUser.setText(name);
			}
			
			mItemColor = null;
			mUserSelectedList = null;
		}
	}
	
	private class OnUserItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog malertDialog;
		private String mpayoutType;
		
		public OnUserItemClickListener(AlertDialog pAlertDialog,String pPayoutType)
		{
			malertDialog = pAlertDialog;
			mpayoutType = pPayoutType;
		}
		@Override
		public void onItemClick(AdapterView pAdapterView, View arg1, int pPosition,
				long arg3) {
//			ModelUser modelUser = (ModelUser)((Adapter)pAdapterView.getAdapter()).getItem(pPosition);
//			((OnListSelectListener)ActivityBase.this).OnSelected(modelUser,"User");
//			malertDialog.dismiss();
			
			String payoutTypeArr[] = getResources().getStringArray(R.array.PayoutType);
			ModelUser modelUser = (ModelUser)pAdapterView.getAdapter().getItem(pPosition);
			if(mpayoutType.equals(payoutTypeArr[0]) || mpayoutType.equals(payoutTypeArr[1]))
			{
				RelativeLayout main = (RelativeLayout)arg1.findViewById(R.id.RelativeLayoutMain);
				
				
				if(mItemColor == null && mUserSelectedList == null)
				{
					mItemColor = new ArrayList<RelativeLayout>();
					mUserSelectedList = new ArrayList<ModelUser>();
				}
				
				if(mItemColor.contains(main))
				{
					main.setBackgroundResource(R.drawable.blue);
					mItemColor.remove(main);
					mUserSelectedList.remove(modelUser);
				}
				else {
					main.setBackgroundResource(R.drawable.red);
					mItemColor.add(main);
					mUserSelectedList.add(modelUser);
				}
				
//				if(mpayoutType.equals(payoutTypeArr[1]))
//				{
//					if(m_UserSelectedList.size() == 2)
//					{
//						((OnListSelectListener)ActivityBase.this).OnSelected(m_UserSelectedList,"User");
//						malertDialog.dismiss();
//					}
//				}
				return;
			}
			
			if(mpayoutType.equals(payoutTypeArr[2]))
			{
				mUserSelectedList = new ArrayList<ModelUser>();
				mUserSelectedList.add(modelUser);
				etPayoutUser.setText("");
				String name = "";
				mPayoutUserID = "";
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					name += mUserSelectedList.get(i).getUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getUserID() + ",";
				}
				etPayoutUser.setText(name);
				
				mItemColor = null;
				mUserSelectedList = null;
				malertDialog.dismiss();
			}
		}
	}

}
