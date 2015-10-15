package com.google.business;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.google.business.base.BusinessBase;
import com.google.database.sqlitedal.SQLiteDALAccountBook;
import com.google.model.ModelAccountBook;

public class AccountBookBusiness extends BusinessBase{

	private SQLiteDALAccountBook mSqLiteDALAccountBook;

	public AccountBookBusiness(Context pContext) {
		super(pContext);
		mSqLiteDALAccountBook = new SQLiteDALAccountBook(pContext);
	}

	public Boolean insertAccountBook(ModelAccountBook pInfo) {
		mSqLiteDALAccountBook.beginTransaction();
		try {
			Boolean result = mSqLiteDALAccountBook.insertAccountBook(pInfo);
			Boolean result2 = true;
			if (pInfo.getIsDefault() == 1 && result) {
				result2 = setIsDefault(pInfo.getAccountBookID());
			}

			if (result && result2) {
				mSqLiteDALAccountBook.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteDALAccountBook.endTransaction();
		}
	}

	public Boolean deleteAccountBookByAccountBookID(int pAccountBookID) {
		mSqLiteDALAccountBook.beginTransaction();
		try {
			String condition = " And AccountBookID = " + pAccountBookID;
			Boolean result = mSqLiteDALAccountBook
					.deleteAccountBook(condition);
			Boolean result2 = true;
			if (result) {
				PayoutBusiness _BusinessPayout = new PayoutBusiness(
						getContext());
				result2 = _BusinessPayout
						.deletePayoutByAccountBookID(pAccountBookID);
			}

			if (result && result2) {
				mSqLiteDALAccountBook.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteDALAccountBook.endTransaction();
		}
	}

	public Boolean updateAccountBookByAccountBookID(ModelAccountBook pInfo) {
		mSqLiteDALAccountBook.beginTransaction();
		try {
			String condition = " AccountBookID = " + pInfo.getAccountBookID();
			Boolean result = mSqLiteDALAccountBook.updateAccountBook(
					condition, pInfo);
			Boolean result2 = true;
			if (pInfo.getIsDefault() == 1 && result) {
				result2 = setIsDefault(pInfo.getAccountBookID());
			}

			if (result && result2) {
				mSqLiteDALAccountBook.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSqLiteDALAccountBook.endTransaction();
		}
	}

	public List<ModelAccountBook> getAccountBook(String pCondition) {
		return mSqLiteDALAccountBook.getAccountBook(pCondition);
	}

	public boolean isExistAccountBookByAccountBookName(
			String pAccountBookName, Integer pAccountBookID) {
		String condition = " And AccountBookName = '" + pAccountBookName
				+ "'";
		if (pAccountBookID != null) {
			condition += " And AccountBookID <> " + pAccountBookID;
		}
		List list = mSqLiteDALAccountBook.getAccountBook(condition);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ModelAccountBook getDefaultModelAccountBook() {
		List list = mSqLiteDALAccountBook
				.getAccountBook(" And IsDefault = 1");
		if (list.size() == 1) {
			return (ModelAccountBook) list.get(0);
		} else {
			return null;
		}
	}

	public int getCount() {
		return mSqLiteDALAccountBook.getCount("");
	}

	public Boolean setIsDefault(int pID) {
		String condition = " IsDefault = 1";
		ContentValues contentValues = new ContentValues();
		contentValues.put("IsDefault", 0);
		Boolean result = mSqLiteDALAccountBook.updateAccountBook(condition,
				contentValues);

		condition = " AccountBookID = " + pID;
		contentValues.clear();
		contentValues.put("IsDefault", 1);
		Boolean result2 = mSqLiteDALAccountBook.updateAccountBook(condition,
				contentValues);

		if (result && result2) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getAccountBookNameByAccountId(int pBookId) {
		String conditionString = "And AccountBookID = " + String.valueOf(pBookId);
		List<ModelAccountBook> accountBooks = mSqLiteDALAccountBook.getAccountBook(conditionString);
		return accountBooks.get(0).getAccountBookName();
	}
}
