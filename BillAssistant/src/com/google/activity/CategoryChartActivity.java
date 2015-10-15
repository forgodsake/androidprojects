package com.google.activity;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomButton;

import com.google.R;
import com.google.activity.base.FrameActivity;
import com.google.adapter.CategoryAdapter;
import com.google.business.CategoryBusiness;
import com.google.model.ModelCategoryTotal;

public class CategoryChartActivity extends FrameActivity {
	private List<ModelCategoryTotal> mModelCategoryTotal;
	private TextView tvContent;
	private CategoryBusiness mCategoryBusiness;
	private ExpandableListView categoryList;
	private CategoryAdapter mCategoryAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVariable();
		View pieView = CategoryStatistics();
		addMainBody(pieView);
		removeBottomBox();
	}

	private View CategoryStatistics() {
		int[] color = new int[] { Color.parseColor("#FF5552"), Color.parseColor("#2A94F1"), Color.parseColor("#F12792"), Color.parseColor("#FFFF52"), Color.parseColor("#84D911"),Color.parseColor("#5255FF") };
		DefaultRenderer defaultRenderer = BuildCategoryRenderer(color);
		CategorySeries _CategorySeries = _BuildCategoryDataset("消费类别统计", mModelCategoryTotal);
		View pieView = ChartFactory.getPieChartView(this, _CategorySeries, defaultRenderer);
		return pieView;
	}
	
	protected DefaultRenderer BuildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setZoomButtonsVisible(true);
        renderer.setLabelsTextSize(30);
//        _Renderer.setLegendTextSize(60);
        renderer.setShowLegend(false);
        renderer.setLabelsColor(Color.BLUE);
        renderer.setMargins(new int[] { 30, 30, 30, 30 });
        int color = 0;
        for (int i = 0;i<mModelCategoryTotal.size();i++) {
          SimpleSeriesRenderer srenderer = new SimpleSeriesRenderer();
          srenderer.setColor(colors[color]);
          renderer.addSeriesRenderer(srenderer);
          color++;
          if (color > colors.length) {
        	  color = 0;
          }
        }
        return renderer;
      }
    
    protected CategorySeries _BuildCategoryDataset(String title, List<ModelCategoryTotal> values) {
        CategorySeries series = new CategorySeries(title);
        for (ModelCategoryTotal value : values) {
          series.add("数量：" + value.Count, Double.parseDouble(value.Count));
        }

        return series;
      }
	
	private void setTitle() {
		String titel = getString(R.string.ActivityCategoryTotal);
		setTopBarTitle(titel);
	}

	protected void initView() {
//		categoryList = (ExpandableListView) findViewById(R.id.ExpandableListViewCategory);
	}

	protected void initListeners() {
		
	}
	

	protected void initVariable() {
//		mCategoryBusiness = new CategoryBusiness(CategoryChartActivity.this);
		mModelCategoryTotal = (List<ModelCategoryTotal>) getIntent().getSerializableExtra("Total");
	}

	protected void BindData()
	{   
//		mCategoryAdapter = new CategoryAdapter(this);
//		categoryList.setAdapter(mCategoryAdapter);
		setTitle();
		String content = "";
		for (int i = 0; i < mModelCategoryTotal.size(); i++) {
			ModelCategoryTotal modelCategoryTotal = mModelCategoryTotal.get(i);
			content += modelCategoryTotal.CategoryName + "--" + modelCategoryTotal.Count + "--" + modelCategoryTotal.SumAmount + "\r\n";
		}
		tvContent.setText(content);
	}
	
}
