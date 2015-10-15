package com.google.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.R;
import com.google.business.CategoryBusiness;
import com.google.model.ModelCategory;

public class CategoryAdapter extends BaseExpandableListAdapter {
	
	private class GroupHolder
	{
		TextView Name;
		TextView Count;
	}
	
	private class ChildHolder
	{
		TextView Name;
	}

	private Context m_Context;
	private List m_List;
	private CategoryBusiness m_BusinessCategory;
	public List _ChildCountOfGroup;
	
	public CategoryAdapter(Context p_Context)
	{
		m_BusinessCategory = new CategoryBusiness(p_Context);
		m_Context = p_Context;
		m_List = m_BusinessCategory.getNotHideRootCategory();
		_ChildCountOfGroup = new ArrayList();
	}
	
	public Integer GetChildCountOfGroup(int groupPosition)
	{
		return (Integer) _ChildCountOfGroup.get(groupPosition);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ModelCategory _ModelCategory = (ModelCategory) getGroup(groupPosition);
		List _List = m_BusinessCategory.getNotHideCategoryListByParentID(_ModelCategory.getCategoryID());
		return _List.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder _ChildHolder;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.category_children_list_item, null);
			_ChildHolder = new ChildHolder();
			_ChildHolder.Name = (TextView)convertView.findViewById(R.id.tvCategoryName);
			convertView.setTag(_ChildHolder);
		}
		else {
			_ChildHolder = (ChildHolder)convertView.getTag();
		}
		ModelCategory _ModelCategory = (ModelCategory)getChild(groupPosition, childPosition);
		_ChildHolder.Name.setText(_ModelCategory.getCategoryName());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ModelCategory _ModelCategory = (ModelCategory) getGroup(groupPosition);
		List _List = m_BusinessCategory.getNotHideCategoryListByParentID(_ModelCategory.getCategoryID());
		return _List.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return m_List.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return m_List.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder _GroupHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.category_group_list_item, null);
			_GroupHolder = new GroupHolder();
			_GroupHolder.Name = (TextView)convertView.findViewById(R.id.tvCategoryName);
			_GroupHolder.Count = (TextView)convertView.findViewById(R.id.tvCount);
			convertView.setTag(_GroupHolder);
		}
		else {
			_GroupHolder = (GroupHolder)convertView.getTag();
		}
		ModelCategory _ModelCategory = (ModelCategory)getGroup(groupPosition);
		_GroupHolder.Name.setText(_ModelCategory.getCategoryName());
		int _Count = m_BusinessCategory.getNotHideCountByParentID(_ModelCategory.getCategoryID());
		_GroupHolder.Count.setText(m_Context.getString(R.string.TextViewTextChildrenCategory, _Count));
		_ChildCountOfGroup.add(_Count);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

}
