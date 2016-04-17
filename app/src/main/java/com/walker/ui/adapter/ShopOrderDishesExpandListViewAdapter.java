package com.walker.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.walker.R;
import com.walker.model.Product;
import com.walker.model.ProductType;
import com.walker.ui.order.ShopOrderDishesActivity;
import com.walker.util.NumberUtils;

import net.tsz.afinal.FinalBitmap;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;

import java.util.Iterator;
import java.util.List;


/**
 * @author Administrator
 *	点餐界面中的右侧列表ExpandableListView的Adapter
 */
@SuppressLint("InflateParams")
public class ShopOrderDishesExpandListViewAdapter extends
		BaseExpandableListAdapter {
	
	private ShopOrderDishesActivity orderActivity;
	private LayoutInflater fatherInflater,sonInflater;
	/** 菜单分类，及分类下的菜单 */
	private List<ProductType> categoryList;
	private FinalBitmap bitmap;
	/**
	 * 保存商品的数量
	 */

	public ShopOrderDishesExpandListViewAdapter(List<ProductType> categoryList,ShopOrderDishesActivity baseActivity){
		this.categoryList = categoryList;
		this.orderActivity = baseActivity;
		fatherInflater = this.orderActivity.getLayoutInflater();
		sonInflater = this.orderActivity.getLayoutInflater();
		this.bitmap = FinalBitmap.create(baseActivity);
		bitmap.configLoadingImage(R.drawable.noimg);
		bitmap.configLoadfailImage(R.drawable.noimg);
	}

	@Override
	public int getGroupCount() {
		return this.categoryList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(categoryList.get(groupPosition).getProductList()!=null){
			return categoryList.get(groupPosition).getProductList().size();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.categoryList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ProductType productMenu=this.categoryList.get(groupPosition);//获取分类
		return productMenu.getProductList().get(childPosition);//该分类下的商品
	}

	@Override
	public long getGroupId(int groupPosition) {
//		ProductType productMenu=this.categoryList.get(groupPosition);//获取分类
//		return productMenu.getId();
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
//		ProductType productMenu=this.categoryList.get(groupPosition);//获取分类
//		return productMenu.getProductList().get(childPosition).getId();//商品的id
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TypeViewHolder holder = null;
		if(convertView==null){
			convertView=fatherInflater.inflate(R.layout.shop_order_dishes_father_item, null);
			holder = new TypeViewHolder();
			holder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_category_name);
			convertView.setTag(holder);
		}else{
			holder = (TypeViewHolder) convertView.getTag();
		}
		holder.tvTypeName.setText(categoryList.get(groupPosition).getTypeName());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ProductType productMenu=categoryList.get(groupPosition);
		final Product product = productMenu.getProductList().get(childPosition);
//		String imageUrl = Contants.SERVER_PHOTO_URL_BASE+ product.getImgUrl();
		String imageUrl = product.getImgUrl();
		
		final ViewHolder holder;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=sonInflater.inflate(R.layout.shop_order_dishes_son_item, null);
			holder.imgIcon=(ImageView) convertView.findViewById(R.id.img_product_icon);
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_order_product_title);
			holder.tvSinglePrice=(TextView) convertView.findViewById(R.id.tv_product_single_price);
			holder.imgOrderSub=(ImageView) convertView.findViewById(R.id.img_order_sub);
			holder.tvOrderNum=(TextView) convertView.findViewById(R.id.tv_order_num);
			holder.imgOrderAdd=(ImageView) convertView.findViewById(R.id.img_order_add);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.imgOrderAdd.setVisibility(View.VISIBLE);
		if(imageUrl!=null)
			bitmap.display(holder.imgIcon, imageUrl);
		holder.tvTitle.setText(product.getName());
		holder.tvSinglePrice.setText("￥"+ NumberUtils.format(product.getPrice()));
		final int pnum = productList.getCount(product);
		if(pnum == 0){
			isShow(holder, View.GONE);//在没有选中的商品的时候，隐藏减少按钮
		}else if (pnum>0) {
			isShow(holder, View.VISIBLE);//显示减少按钮
		}
		holder.tvOrderNum.setText(pnum+"");
		//减少按钮
		holder.imgOrderSub.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				productList.remove(product, 1);
				orderActivity.updateBottomStatus(getTotalPrice(),getTotalNumber());
				ShopOrderDishesExpandListViewAdapter.this.notifyDataSetChanged();
			}
		});
		//增加
		holder.imgOrderAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				productList.add(product);
				orderActivity.updateBottomStatus(getTotalPrice(),getTotalNumber());
				ShopOrderDishesExpandListViewAdapter.this.notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	
	private Bag productList = new HashBag();
	/**
	 * 获取总金额
	 * @return
	 */
	public double getTotalPrice(){
		double totalProce = 0;
		if(productList.size() == 0){
			return totalProce;
		}
		for(Iterator<?> itr = productList.uniqueSet().iterator();itr.hasNext();){
			Product product = (Product)itr.next();
			totalProce += product.getPrice()*productList.getCount(product);
		}
		return totalProce;
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	public int getTotalNumber(){
		int totalNumber = 0;
		if(productList.size() == 0){
			return totalNumber;
		}
		for(Iterator<?> itr = productList.uniqueSet().iterator();itr.hasNext();){
			Product product = (Product)itr.next();
			totalNumber +=productList.getCount(product);
		}
		return totalNumber;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private final class TypeViewHolder{
		public TextView tvTypeName;//分类名称
	}
	
	private final class ViewHolder{
		public ImageView imgIcon;//产品图标
		public TextView tvTitle;//产品名称
		public TextView tvSinglePrice;//产品单价
		public ImageView imgOrderSub;//减少
		public TextView tvOrderNum;//产品预定数量
		public ImageView imgOrderAdd;//增加
	}

	
	/**
	 * 是否显示减少按钮和商品数量
	 * @param holder
	 */
	private void isShow(ViewHolder holder,int isVisible){
		holder.tvOrderNum.setVisibility(isVisible);
		holder.imgOrderSub.setVisibility(isVisible);
	}

}
