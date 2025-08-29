package com.litmus7.inventorymanagement.fileprocessing;

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



public class FileProcessing {
	InventoryManagementDTO inventoryDTO;
	InventoryManagementDAO inventoryDAO=new InventoryManagementDAO();
	 static File processedFolder = new File("E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\processed");
     static File errorFolder = new File("E:\\Eclipse workspace\\InventoryManagement\\inventory feed\\error");
     
	
	public void fileProcessor(File file, Connection dbConnect) throws IOException, SQLException {
		processedFolder.mkdirs();
        errorFolder.mkdirs();
	BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
	String line=bufferedReader.readLine();
	String[] details;
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
	Files.move(file.toPath(),
            Paths.get(errorFolder.getAbsolutePath(), file.getName()),
            StandardCopyOption.REPLACE_EXISTING);
	System.out.println("Error while processesing: " + file.getName()+"\nTransaction rolled back");
	
}
}
}
