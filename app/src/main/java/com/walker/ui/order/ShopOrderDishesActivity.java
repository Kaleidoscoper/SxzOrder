package com.walker.ui.order;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walker.R;
import com.walker.model.Product;
import com.walker.model.ProductType;
import com.walker.ui.adapter.ShopOrderDishesExpandListViewAdapter;
import com.walker.ui.widget.PinnedHeaderExpandableListView;
import com.walker.ui.widget.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.walker.ui.adapter.ShopOrderDishesListViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 *	点餐
 */
public class ShopOrderDishesActivity extends Activity implements
		OnClickListener, ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener {

	private ListView categoryList;
	private ShopOrderDishesListViewAdapter listViewAdapter;
	private PinnedHeaderExpandableListView expandableListView;
	private ShopOrderDishesExpandListViewAdapter expandListViewAdapter;
	private TextView tvTotalPrice;
	private Button btnFinish;
	// 弹出框的属性
	
	private RelativeLayout relativeBottom;//底部

	private int oldPositiion = 0;
	private double oldPrice;//获取在上次退出时，在该店预定的商品的总价格
	
	private int selectPosition = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shop_order_dishes_activity);
		initView();
		List<ProductType> shopMenuList = initData();
		if(shopMenuList!=null && shopMenuList.size()>0){
			//左侧菜单列表
			listViewAdapter = new ShopOrderDishesListViewAdapter(shopMenuList, ShopOrderDishesActivity.this);
			categoryList.setAdapter(listViewAdapter);
			//右侧ExpandableListView
			expandListViewAdapter = new ShopOrderDishesExpandListViewAdapter(shopMenuList,ShopOrderDishesActivity.this);
			expandableListView.setAdapter(expandListViewAdapter);
			deployExpandableListView(shopMenuList.size());//配置ExpandableListView的相关参数的方法必须等到数据加载完成后执行
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 中间部分
		categoryList = (ListView) findViewById(R.id.lv_category_list);
		expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expand_content_list);
		tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
		if (oldPrice>0) {
			tvTotalPrice.setText(String.valueOf(oldPrice));
		}
		btnFinish = (Button) findViewById(R.id.btn_order_finish);
		relativeBottom=(RelativeLayout) findViewById(R.id.relative_bottom);
		
		btnFinish.setOnClickListener(this);

		//当左侧的分类列表点击的时候的监听事件
		categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectPosition = position;
				expandableListView.setSelectedGroup(position);
			}
		});
	}
	
	
	/**
	 * 配置ExpandableListView的相关信息
	 */
	private void deployExpandableListView(int expandableListSize){
		expandableListView.setOnHeaderUpdateListener(this);// 必须在Adapter实例化之后执行
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this);
		expandableListView.setGroupIndicator(null);// 去掉默认的指示箭头
//		expandableListView.setDivider(null);// 设置分割线样式
		for (int i = 0; i < expandableListSize; i++) {
			expandableListView.expandGroup(i);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_order_finish:// 选购完成，跳到确认订单界面
			
			break;

		default:
			break;
		}
	}

	/**
	 * 在滚动的时候从新绘制头部，即组列表
	 */
	@Override
	public View getPinnedHeader() {
		View headerView = getLayoutInflater().inflate(R.layout.shop_order_dishes_father_item, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	/**
	 * 在重新绘制头部之后，给这个头部设置新的标题
	 */
	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		
		//修改固定头部的标题
		TextView textView = (TextView) headerView.findViewById(R.id.tv_category_name);
		ProductType productType = (ProductType) expandListViewAdapter.getGroup(firstVisibleGroupPos);
		textView.setText(productType.getTypeName());// 更新标题
		//此处不能和上面的调换
		if(firstVisibleGroupPos < selectPosition){
			firstVisibleGroupPos = selectPosition;
		}
		//修改选中的item的背景色
		changeItemBackground(oldPositiion,firstVisibleGroupPos);
		// 保存旧分类的位置编号
		oldPositiion = firstVisibleGroupPos;
		selectPosition = -1;
	}
	
	/**
	 * 修改左边ListView中当前选中的item的背景色，并把原来选中的item的背景色还原成默认背景色
	 * @param oldPosition	原来选中的item的编号
	 * @param newPosition	当前选中的item的编号
	 */
	private void changeItemBackground(int oldPosition,int newPosition){
		/*修改左边分类列表的背景色*/
		View oldCateView = getViewByPosition(oldPositiion, categoryList);// 旧的分类
		View cateView = getViewByPosition(newPosition, categoryList);// 当前所在分类

		// 旧分类的背景色
		oldCateView.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
		// 新分类的背景色
		cateView.setBackgroundResource(R.drawable.selector_choose_eara_ite);
		
		int lastVisibalePosition=categoryList.getLastVisiblePosition();//在ListView中，第一个可见的item的编号
		int firstVisibalePosition=categoryList.getFirstVisiblePosition();//在ListView中，最后一个可见的item的编号
		if(newPosition>=lastVisibalePosition){//向后滚动
			// 把分类列表定位到当前的分类位置,即当前选中的item
			categoryList.setSelection(newPosition);
		}else if (firstVisibalePosition>=newPosition) {//向前滚动
			// 把分类列表定位到当前的分类位置
			categoryList.setSelection(newPosition);
		}
		listViewAdapter.setSelected(newPosition);//把编号为newPosition的item设置为选中
	}

	/**
	 * 获取ListView指定编号的item布局view
	 * @param pos		item编号
	 * @param listView	目标ListView
	 * @return
	 */
	public View getViewByPosition(int pos, ListView listView) {
		//在可见的item当中，第一个item的位置编号
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		//最后一个item的位置编号
		final int lastListItemPosition = firstListItemPosition+ listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	/**
	 * ExpandableListView 在组列表中的点击事件
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return false;
	}

	/**
	 * ExpandableListView 子列表的点击事件 ExpandableListView 的二级列表的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// 数据请求完成之后再弹出框
		return false;
	}

	
	/**
	 * 默认情况下，底部导航是隐藏的，当点餐之后，就显示出来，包括价格和结算按钮
	 */
	public void updateBottomStatus(double totalPrice, int num){
		
		if(totalPrice>0){
			relativeBottom.setVisibility(View.VISIBLE);
		}
		else{
			relativeBottom.setVisibility(View.GONE);
		}
		tvTotalPrice.setText(" 数量"+num+" 共"+totalPrice+"元");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private List<ProductType> initData(){
		Product product = null;
		List<Product> productList = null;
		ProductType type = null;
		List<ProductType> productTypes = new ArrayList<ProductType>();
		for(int i=0;i<5;i++){
			type = new ProductType();
			productList = new ArrayList<Product>();
			type.setId(UUID.randomUUID().toString().replace("-", ""));
			for(int j=0;j<5;j++){
				product = new Product();
				product.setId(UUID.randomUUID().toString().replace("-", ""));
				product.setName("product_"+i+"_"+j);
				product.setPrice(2.1+i);
				productList.add(product);
			}
			type.setProductList(productList);
			type.setTypeName("type_"+i);
			productTypes.add(type);
		}
		return productTypes;
	}
}
