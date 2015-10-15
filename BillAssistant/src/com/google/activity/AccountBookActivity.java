package com.google.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.AccountBookAdapter;
import com.google.business.AccountBookBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;
import com.google.model.ModelAccountBook;
import com.google.utility.RegexTools;

public class AccountBookActivity extends FrameActivity implements OnSlideMenuListener {
    
	private ListView lvAccountBookList;
	private AccountBookAdapter mAccountBookAdapter;
	private AccountBookBusiness mAccountBookBusiness;
	private ModelAccountBook mSelectModelAccountBook;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainBody(R.layout.account_book);
        initVariable();
        initView();
        initListeners();
        bindData();
        createSlideMenu(R.array.SlideMenuAccountBook);
    }
    
    public void initVariable()
    {
    	mAccountBookBusiness = new AccountBookBusiness(this);
    }
    
    public void initView()
    {
    	lvAccountBookList = (ListView) findViewById(R.id.lvAccountBookList);
    }
    
    public void initListeners()
    {
    	registerForContextMenu(lvAccountBookList);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterContextMenuInfo _AdapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
    	ListAdapter _ListAdapter = lvAccountBookList.getAdapter();
    	
    	mSelectModelAccountBook = (ModelAccountBook)_ListAdapter.getItem(_AdapterContextMenuInfo.position);
    	
    	menu.setHeaderIcon(R.drawable.account_book_small_icon);
    	menu.setHeaderTitle(mSelectModelAccountBook.getAccountBookName());
    	
    	CreateMenu(menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
		case 1:
			showAccountBookAddOrEditDialog(mSelectModelAccountBook);
			break;
		case 2:
			delete();
			break;
		default:
			break;
		}

    	return super.onContextItemSelected(item);
    }
    
    public void bindData()
    {
    	mAccountBookAdapter = new AccountBookAdapter(this);
    	lvAccountBookList.setAdapter(mAccountBookAdapter);
    	setTitle();
    }

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideMenuToggle();
		if (pSlideMenuItem.getItemID() == 0) {
			showAccountBookAddOrEditDialog(null);
		}
	}
	
	private void setTitle() {
		int count = mAccountBookAdapter.getCount();
		String title = getString(R.string.ActivityTitleAccountBook, new Object[]{count});
		setTopBarTitle(title);
	}
	
	private void showAccountBookAddOrEditDialog(ModelAccountBook pModelAccountBook)
	{
		View view = getLayoutInflater().inflate(R.layout.account_book_add_or_edit, null);
		
		EditText etAccountBookName = (EditText) view.findViewById(R.id.etAccountBookName);
		CheckBox chkIsDefault = (CheckBox)view.findViewById(R.id.chkIsDefault);
		
		if (pModelAccountBook != null) {
			etAccountBookName.setText(pModelAccountBook.getAccountBookName());
		}
		
		String title;
		
		if(pModelAccountBook == null)
		{
			title = getString(R.string.DialogTitleAccountBook,new Object[]{getString(R.string.TitleAdd)});
		}
		else {
			title = getString(R.string.DialogTitleAccountBook,new Object[]{getString(R.string.TitleEdit)});
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
				.setView(view)
				.setIcon(R.drawable.account_book_big_icon)
				.setNeutralButton(getString(R.string.ButtonTextSave), new OnAddOrEditAccountBookListener(pModelAccountBook,etAccountBookName,chkIsDefault,true))
				.setNegativeButton(getString(R.string.ButtonTextCancel), new OnAddOrEditAccountBookListener(null,null,null,false))
				.show();
	}
	
	private class OnAddOrEditAccountBookListener implements DialogInterface.OnClickListener
	{
		private ModelAccountBook mModelAccountBook;
		private EditText etAccountBookName;
		private boolean mIsSaveButton;
		private CheckBox chkIsDefault;
		
		public OnAddOrEditAccountBookListener(ModelAccountBook pModelAccountBook,EditText petAccountBookName,CheckBox pchkIsDefault,Boolean pIsSaveButton)
		{
			mModelAccountBook = pModelAccountBook;
			etAccountBookName = petAccountBookName;
			mIsSaveButton = pIsSaveButton;
			chkIsDefault = pchkIsDefault;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(mIsSaveButton == false)
			{
				setAlertDialogIsClose(dialog, true);
				return;
			}
			
			if (mModelAccountBook == null) {
				mModelAccountBook = new ModelAccountBook();
			}
			
			String accountBookName = etAccountBookName.getText().toString().trim();
			
			boolean checkResult = RegexTools.IsChineseEnglishNum(accountBookName);
			
			if (!checkResult) {
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{etAccountBookName.getHint()}), 1).show();
				setAlertDialogIsClose(dialog, false);
				return;
			}
			else {
				setAlertDialogIsClose(dialog, true);
			}
			
			checkResult = mAccountBookBusiness.isExistAccountBookByAccountBookName(accountBookName, mModelAccountBook.getAccountBookID());
			
			if (checkResult) {
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextAccountBookExist), 1).show();
				
				setAlertDialogIsClose(dialog, false);
				return;
			}
			else {
				setAlertDialogIsClose(dialog, true);
			}
			
			mModelAccountBook.setAccountBookName(etAccountBookName.getText().toString());
			
			mModelAccountBook.setAccountBookName(String.valueOf(etAccountBookName.getText().toString().trim()));
			if(chkIsDefault.isChecked())
			{
				mModelAccountBook.setIsDefault(1);
			}
			else {
				mModelAccountBook.setIsDefault(0);
			}
			
			if(mModelAccountBook.getAccountBookID() > 0)
			{
				mModelAccountBook.setIsDefault(1);
			}
			
			boolean result = false;
			
			if (mModelAccountBook.getAccountBookID() == 0) {
				result = mAccountBookBusiness.insertAccountBook(mModelAccountBook);
			}
			else {
				result = mAccountBookBusiness.updateAccountBookByAccountBookID(mModelAccountBook);
			}
			
			if (result) {
				bindData();
			}
			else {
				Toast.makeText(AccountBookActivity.this, getString(R.string.TipsAddFail), 1).show();
			}
		}
		
	}

	private void delete() {
		String message = getString(R.string.DialogMessageAccountBookDelete,new Object[]{mSelectModelAccountBook.getAccountBookName()});
		showAlertDialog(R.string.DialogTitleDelete,message,new AccountDeleteClickListener());
	}
	
	private class AccountDeleteClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean result = mAccountBookBusiness.deleteAccountBookByAccountBookID(mSelectModelAccountBook.getAccountBookID());
			
			if (result == true) {
				bindData();
			}
		}
	}
}
