package com.google.business;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.content.Context;

import com.google.R;
import com.google.business.base.BusinessBase;
import com.google.model.ModelPayout;

public class StatisticsBusiness extends BusinessBase {
	
	private PayoutBusiness mPayoutBusiness;
	private UserBusiness mUserBusiness;
	private AccountBookBusiness mAccountBookBusiness;
	
	public StatisticsBusiness(Context pContext)
	{
		super(pContext);
		mPayoutBusiness = new PayoutBusiness(pContext);
		mUserBusiness = new UserBusiness(pContext);
		mAccountBookBusiness = new AccountBookBusiness(pContext);
	}
	
	public String GetPayoutUserIDByAccountBookID(int pAccountBookID)
	{
		String result = "";
		List<ModelStatistics> listModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + pAccountBookID);
		
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);
			if(modelStatistics.getPayoutType().equals("个人")) {
				result += modelStatistics.PayerUserID + "个人消费" + modelStatistics.Cost.toString() + "元\r\n";
			} else if(modelStatistics.getPayoutType().equals("均分")) {
				if(modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					result += modelStatistics.PayerUserID + "个人消费" + modelStatistics.Cost.toString() + "元\r\n";
				} else {
					result += modelStatistics.PayerUserID + "借给" + modelStatistics.ConsumerUserID + modelStatistics.Cost + "元\r\n";
				}
			}else if(modelStatistics.getPayoutType().equals("借贷")) {
				result += modelStatistics.PayerUserID + "借给" + modelStatistics.ConsumerUserID + modelStatistics.Cost + "元\r\n";
			}
		}
		
		return result;
	}
	
	public List<ModelStatistics> GetPayoutUserID(String pCondition)
	{
		List<ModelStatistics> listModelStatistics = GetModelStatisticsList(pCondition);
		List<ModelStatistics> listModelStatisticsTemp = new ArrayList<ModelStatistics>();
		List<ModelStatistics> listModelStatisticsTotal = new ArrayList<ModelStatistics>();

		for (int i = 0; i < listModelStatistics.size(); i++) {
			ModelStatistics modelStatistics = listModelStatistics.get(i);

			String currentPayerUserID = modelStatistics.PayerUserID;
			
			ModelStatistics modelStatisticsTemp = new ModelStatistics();
			modelStatisticsTemp.PayerUserID = modelStatistics.PayerUserID;
			modelStatisticsTemp.ConsumerUserID = modelStatistics.ConsumerUserID;
			modelStatisticsTemp.Cost = modelStatistics.Cost;
			modelStatisticsTemp.setPayoutType(modelStatistics.getPayoutType());
			listModelStatisticsTemp.add(modelStatisticsTemp);
			
			int nextIndex;
			if((i+1) < listModelStatistics.size())
			{
				nextIndex = i+1;
			}
			else {
				nextIndex = i;
			}
			
			if (!currentPayerUserID.equals(listModelStatistics.get(nextIndex).PayerUserID) || nextIndex == i) {
				
				for (int j = 0; j < listModelStatisticsTemp.size(); j++) {
					ModelStatistics modelStatisticsTotal = listModelStatisticsTemp.get(j);
					int index = GetPostionByConsumerUserID(listModelStatisticsTotal,modelStatisticsTotal.PayerUserID, modelStatisticsTotal.ConsumerUserID);
					if(index != -1)
					{
						listModelStatisticsTotal.get(index).Cost = listModelStatisticsTotal.get(index).Cost.add(modelStatisticsTotal.Cost);
					}
					else {
						listModelStatisticsTotal.add(modelStatisticsTotal);
					}
				}
				listModelStatisticsTemp.clear();
			}
			
		}
		
		return listModelStatisticsTotal;
	}
	
	private int GetPostionByConsumerUserID(List<ModelStatistics> pListModelStatisticsTotal,String pPayerUserID,String pConsumerUserID)
	{
		int Index = -1;
		for (int i = 0; i < pListModelStatisticsTotal.size(); i++) {
			if (pListModelStatisticsTotal.get(i).PayerUserID.equals(pPayerUserID) && pListModelStatisticsTotal.get(i).ConsumerUserID.equals(pConsumerUserID)) {
				Index = i;
			}
		}
		
		return Index;
	}

	private List<ModelStatistics> GetModelStatisticsList(String pCondition) {
		List<ModelPayout> ListPayout = mPayoutBusiness.getPayoutOrderByPayoutUserID(pCondition);
		
		String PayoutTypeArr[] = getContext().getResources().getStringArray(R.array.PayoutType);
		
		List<ModelStatistics> listModelStatistics = new ArrayList<ModelStatistics>();
		
		if(ListPayout != null)
		{
			for (int i = 0; i < ListPayout.size(); i++) {
				ModelPayout ModelPayout = ListPayout.get(i);
				String PayoutUserName[] = mUserBusiness.getUserNameByUserID(ModelPayout.getPayoutUserID()).split(",");
				String PayoutUserID[] = ModelPayout.getPayoutUserID().split(",");
				
				String PayoutType = ModelPayout.getPayoutType();
				
				BigDecimal cost;
				
				if(PayoutType.equals(PayoutTypeArr[0]))
				{
					int payoutTotal = PayoutUserName.length;
					
					cost = ModelPayout.getAmount().divide(new BigDecimal(payoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
				}
				else {
					cost = ModelPayout.getAmount();
				}
				
				for (int j = 0; j < PayoutUserID.length; j++) {
					
					if (PayoutType.equals(PayoutTypeArr[1]) && j == 0) {
						continue;
					}
					
					ModelStatistics modelStatistics = new ModelStatistics();
					modelStatistics.PayerUserID = PayoutUserName[0];
					modelStatistics.ConsumerUserID = PayoutUserName[j];
					modelStatistics.setPayoutType(PayoutType);
					modelStatistics.Cost = cost;
					
					listModelStatistics.add(modelStatistics);
				}
			}
		}
		
		return listModelStatistics;
	}
	
	public String ExportStatistics(int pAccountBookID) throws Exception {
		String Result = "";
		String AccountBookName = mAccountBookBusiness.getAccountBookNameByAccountId(pAccountBookID);
		Date Date = new Date();
		String FileName = AccountBookName + String.valueOf(Date.getYear()) + String.valueOf(Date.getMonth()) + String.valueOf(Date.getDay()) + ".xls";
		File FileDir = new File("/sdcard/GOD/Export/");
		if (!FileDir.exists()) {
			FileDir.mkdirs();
		}
		File File = new File("/sdcard/GOD/Export/" + FileName);
		if(!File.exists()) {
			File.createNewFile();
		}
		
		WritableWorkbook wBookData;
		wBookData = Workbook.createWorkbook(File);
		WritableSheet wsAccountBook = wBookData.createSheet(AccountBookName, 0);
		
		String[] Titles = {"编号", "姓名", "金额", "消费信息", "消费类型"};
		Label Label;
		for (int i = 0; i < Titles.length; i++) {
			Label = new Label(i, 0, Titles[i]);
			wsAccountBook.addCell(Label);
		}
		
		/*
		 */
		List<ModelStatistics> ListModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + pAccountBookID);
		
		for(int i = 0;i < ListModelStatisticsTotal.size(); i++) {
			ModelStatistics ModelStatistics = ListModelStatisticsTotal.get(i);
			
			jxl.write.Number IdCell = new Number(0, i+1, i+1);
			wsAccountBook.addCell(IdCell);
			
			Label lblName = new Label(1, i+1, ModelStatistics.PayerUserID);
			wsAccountBook.addCell(lblName);
			
			NumberFormat nfMoney = new NumberFormat("#.##");
			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);
			
			Number CostCell = new Number(2, i+1, ModelStatistics.Cost.doubleValue(), wcfFormat);
			wsAccountBook.addCell(CostCell);
			
			String Info = "";
			if(ModelStatistics.getPayoutType().equals("个人")) {
				Info = ModelStatistics.PayerUserID + "个人消费" + ModelStatistics.Cost.toString() + "元";
			} else if(ModelStatistics.getPayoutType().equals("均分")) {
				if(ModelStatistics.PayerUserID.equals(ModelStatistics.ConsumerUserID)) {
					Info = ModelStatistics.PayerUserID + "个人消费" + ModelStatistics.Cost.toString() + "元";
				} else {
					Info = ModelStatistics.ConsumerUserID + "应支付给" + ModelStatistics.PayerUserID + ModelStatistics.Cost + "元";
				}
			} 
			Label lblInfo = new Label(3, i+1, Info);
			wsAccountBook.addCell(lblInfo);
			//消费类型
			Label lblPayoutType = new Label(4, i+1, ModelStatistics.getPayoutType());
			wsAccountBook.addCell(lblPayoutType);
		}
		
		wBookData.write();
		wBookData.close();
		Result = "数据已导出，位置在：/sdcard/GOD/Export/" + FileName;
		return Result;
	}
	
	public class ModelStatistics
	{
		public String PayerUserID;
		public String ConsumerUserID;
		private String mPayoutType;
		public BigDecimal Cost;
		
		public String getPayoutType() {
			return mPayoutType;
		}
		
		public void setPayoutType(String pValue) {
			mPayoutType = pValue;
		}
	}
}
