package com.litmus7.inventorymanagement.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.litmus7.inventorymanagement.dto.*;
import com.litmus7.inventorymanagement.file.FileManagement;

public class InventoryManagementDAO {
	
	FileManagement file=new FileManagement();
	
	public InventoryManagementDTO insertData(Connection dbConnect, InventoryManagementDTO fieldEntry) throws SQLException, IOException {
	
		PreparedStatement statement=null;
		try {
			String query="INSERT INTO inventory VALUES (?,?,?,?)";
			statement=dbConnect.prepareStatement(query);
			statement.setInt(1, fieldEntry.getSKU());
			statement.setString(2, fieldEntry.getProductName());
			statement.setInt(3, fieldEntry.getQuantity());
			statement.setDouble(4, fieldEntry.getPrice());
			statement.execute();
			System.out.println("added field with SKU: "+fieldEntry.getSKU());
		
		}catch (Exception e) {
			//file.writeFile(fieldEntry, "E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\error.csv");
			System.out.println("Error while adding field with SKU: "+fieldEntry.getSKU());
			throw new SQLException();
	
		}
		finally {
		
			statement.close();
		}
		return fieldEntry;
	}
}