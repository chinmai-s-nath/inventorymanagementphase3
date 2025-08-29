package com.litmus7.inventorymanagement.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;

import com.litmus7.inventorymanagement.dao.InventoryManagementDAO;
import com.litmus7.inventorymanagement.dto.InventoryManagementDTO;
import com.litmus7.inventorymanagement.util.ConnectionUtil;

public class InventoryManagementApp {

	 static File inputFolder = new File("E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\input");
     static File processedFolder = new File("E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\processed");
     static File errorFolder = new File("E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\error");
   

     
     
	public static void main(String[] args) throws IOException, SQLException {
		processedFolder.mkdirs();
        errorFolder.mkdirs();
        Connection dbConnect=ConnectionUtil.connection();
		String line="";
		String [] details;
		BufferedReader bufferedReader=null;
		InventoryManagementDTO inventoryDTO=null;
		InventoryManagementDAO inventoryDAO=new InventoryManagementDAO();
		File[] files = inputFolder.listFiles((dir, name) -> name.endsWith(".csv"));
		File currentFile = null;
		try{
			
	        if (files == null || files.length == 0) {
	            System.out.println("No CSV files found in input folder.");
	            return;
	        }
		}catch (Exception e) {}
	        
	        for (File file : files) {
	        	currentFile=file;
	        	dbConnect.setAutoCommit(false);
				bufferedReader=new BufferedReader(new FileReader(file));
				line=bufferedReader.readLine();
				try {
				while((line=bufferedReader.readLine())!=null) {
					details=line.split(","); 
					int sku = Integer.parseInt(details[0].trim());
			        String name = details[1].trim();
			        int quantity = Integer.parseInt(details[2].trim());
			        double price = Double.parseDouble(details[3].trim());
	
			        inventoryDTO = new InventoryManagementDTO(sku, name, quantity, price);
			        inventoryDAO.insertData(dbConnect,inventoryDTO);
				}
				bufferedReader.close();
				dbConnect.commit();
				Files.move(file.toPath(),
	                    Paths.get(processedFolder.getAbsolutePath(), file.getName()),
	                    StandardCopyOption.REPLACE_EXISTING);
	            System.out.println("Processed successfully: " + file.getName());
				
	        }
			catch (SQLException e) {
				dbConnect.rollback(); // rollback entire file
				bufferedReader.close();
				Files.move(currentFile.toPath(),
	                    Paths.get(errorFolder.getAbsolutePath(), currentFile.getName()),
	                    StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Error while processesing: " + file.getName()+"\nTransaction rolled back");
				
			}
		
	}
	        dbConnect.close();
	}
}


