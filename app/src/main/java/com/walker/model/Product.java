package com.walker.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
//	private Shop shop;
	private String name;
	private Double price;
	private Double discount;
	private Integer number;
	private Integer saleNumber;
	private String summary;
	private Integer state;
	private Integer isRecommend;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String  imgUrl;
	private String imgName;
	private ProductType type;	
	private Set<FileImage> fileImage = new HashSet<FileImage>(0);
	private File image;
	private String imageFileName; // 文件名称	
	private String imageContentType; // 文件类型
	private String fileListId;

	/** default constructor */
	public Product() {
	}

	/** minimal constructor */
	public Product(Timestamp createTime, Timestamp updateTime) {
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	/** full constructor */
	public Product(String name, Double price, Double discount,
			Integer number, Integer saleNumber, String summary,
			Integer state, Integer isRecommend, Timestamp createTime,
			Timestamp updateTime) {
		this.name = name;
		this.price = price;
		this.discount = discount;
		this.number = number;
		this.saleNumber = saleNumber;
		this.summary = summary;
		this.state = state;
		this.isRecommend = isRecommend;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}



	public String getName() {
		return this.name;
	}

	public String getShortName() {
		return this.name!=null && this.name.length() > 8 ? this.name.substring(0,8):this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSaleNumber() {
		return this.saleNumber;
	}

	public void setSaleNumber(Integer saleNumber) {
		this.saleNumber = saleNumber;
	}

	public String getSummary() {
		return this.summary;
	}

	public String getSumma() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsRecommend() {
		return this.isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}


	public File getImage() {
		return image;
	}

	
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	
	

	public String getTypeId() {
		return type.getId();
	}

	public void setTypeId(String typeId) {
		if(type==null){
			type = new ProductType();
		}
		this.type.setId(typeId);
	}

	public String getTypeName() {
		return this.type.getTypeName();
	}

	public void setTypeName(String typeName) {
		if(type==null){
			type = new ProductType();
		}
		this.type.setTypeName(typeName);
	}

	public String getFileListId() {
		return fileListId;
	}

	public void setFileListId(String fileListId) {
		this.fileListId = fileListId;
	}

	public Set<FileImage> getFileImage() {
		return fileImage;
	}

	public void setFileImage(Set<FileImage> fileImage) {
		this.fileImage = fileImage;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof Product){
			return ((Product)o).getId() == id;
		}
		return false;
	}
	
}