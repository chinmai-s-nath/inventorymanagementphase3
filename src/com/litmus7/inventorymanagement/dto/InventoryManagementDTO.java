package com.litmus7.inventorymanagement.dto;

public class InventoryManagementDTO {
	
	private int SKU;
	private String productName;
	private int quantity;
	private double price;
	public InventoryManagementDTO(int sku, String productName, int quantity, double price) {
		super();
		SKU = sku;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		setSKU(SKU);
		setProductName(productName);
		setPrice(price);
		setQuantity(quantity);
	}
	public int getSKU() {
		return SKU;
	}
	public void setSKU(int sKU) {
		SKU = sKU;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
