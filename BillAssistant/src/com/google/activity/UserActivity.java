package com.google.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.UserAdapter;
import com.google.business.UserBusiness;
import com.google.controls.SlideMenuItem;
import com.google.controls.SlideMenuView.OnSlideMenuListener;
import com.google.model.ModelUser;
import com.google.utility.RegexTools;

public class UserActivity extends FrameActivity implements OnSlideMenuListener {
	private ListView mUserList;
	private UserAdapter mUserAdapter;
	private UserBusiness mUserBusiness;
	private ModelUser modelUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.user);
		initVariable();
		initView();
		bindData();
		initListeners();
		createSlideMenu(R.array.SlideMenuUser);
	}

	public void initVariable() {

		mUserBusiness = new UserBusiness(this);
	}

	public void initView() {
		mUserList = (ListView) findViewById(R.id.lvUserList);
	}

	public void initListeners() {
		registerForContextMenu(mUserList);
	}

	public void bindData() {
		mUserAdapter = new UserAdapter(this);
		mUserList.setAdapter(mUserAdapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo mmenuInfo = (AdapterContextMenuInfo) menuInfo;
		ListAdapter adapter = mUserList.getAdapter();
		modelUser = (ModelUser) adapter.getItem(mmenuInfo.position);
		menu.setHeaderIcon(R.drawable.user_small_icon);
		menu.setHeaderTitle(modelUser.getUserName());
		CreateMenu(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showUserAddOrEditDialog(modelUser);
			break;
		case 2:
			delete();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		SlideMenuToggle();
		if (slideMenuItem.getItemID() == 0) {
			showUserAddOrEditDialog(null);
		}

	}

	public void showUserAddOrEditDialog(ModelUser pModelUser) {
		View view = getLayoutInflater().inflate(R.layout.user_add_edit, null);
		EditText etUserName = (EditText) view.findViewById(R.id.etUserName);
		if (pModelUser!= null) {
			etUserName.setText(pModelUser.getUserName());
		}

		String title;
		if (pModelUser == null) {
			title = getString(R.string.DialogTitleUser,
					new Object[] { getString(R.string.TitleAdd) });
		} else {
			title = getString(R.string.DialogTitleUser,
					new Object[] { getString(R.string.TitleEdit) });
		}

		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(title)
				.setView(view)
				.setIcon(R.drawable.user_big_icon)
				.setNeutralButton(R.string.ButtonTextSave,
						new UserAddOrEditListener(pModelUser, etUserName, true))
				.setNegativeButton(R.string.ButtonTextCancel,
						new UserAddOrEditListener(null, null, false)).show();
	}

	private class UserAddOrEditListener implements
			DialogInterface.OnClickListener {
		private EditText etUserName;
		private ModelUser mModelUser;
		private boolean mIsSaveButton;

		public UserAddOrEditListener(ModelUser pModelUser, EditText pUserName,
				Boolean pIsSaveButton) {
			mModelUser = pModelUser;
			etUserName = pUserName;
			mIsSaveButton = pIsSaveButton;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			if (mModelUser == null) {
				mModelUser = new ModelUser();
			}

			String userName = etUserName.getText().toString().trim();
			boolean checkout = RegexTools.IsChineseEnglishNum(userName);
			if (!checkout) {
				Toast.makeText(
						UserActivity.this,
						getString(R.string.CheckDataTextChineseEnglishNum,
								new Object[] { etUserName.getHint() }), 1)
						.show();
				setAlertDialogIsClose(dialog, false);
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}

			boolean checkResult = mUserBusiness.isExistUserByUserName(userName,
					mModelUser.getUserID());
			if (checkResult) {
				Toast.makeText(UserActivity.this,
						getString(R.string.CheckDataTextUserExist), 1).show();
				setAlertDialogIsClose(dialog, false);
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}

			mModelUser.setUserName(userName);

			boolean result = false;
			if (mModelUser.getUserID() == 0) {
				result = mUserBusiness.insertUser(mModelUser);
			} else {
				result = mUserBusiness.updateUserByUserID(mModelUser);
			}

			if (result) {
				bindData();
			} else {
				Toast.makeText(UserActivity.this,
						getString(R.string.TipsAddFail), 1).show();
			}
		}

	}

	private void delete() {
		String message = getString(R.string.DialogMessageUserDelete,
				new Object[] { modelUser.getUserName() });
		showAlertDialog(R.string.DialogTitleDelete, message,
				new UserDeleteClickListener());
	}

	private class UserDeleteClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean result = mUserBusiness.HideUserByUserID(modelUser
					.getUserID());
			if (result) {
				bindData();
			}
		}

	}
}
