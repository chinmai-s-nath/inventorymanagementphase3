package com.litmus7.inventorymanagement.fileprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

public class FileProcessing implements Runnable {
    public InventoryManagementDTO inventoryDTO;
    public InventoryManagementDAO inventoryDAO = new InventoryManagementDAO();

    static File processedFolder = new File("E:\\Eclipse workspace\\InventoryManagementPhase3\\inventory feed\\processed");
    static File errorFolder = new File("E:\\Eclipse workspace\\InventoryManagementPhase3\\inventory feed\\error");

    private final File file;

    public FileProcessing(File file) {
        this.file = file;
    }

    public void run() { 
    Connection dbConnect = null;
    processedFolder.mkdirs();
    errorFolder.mkdirs();
    String line;
	String[] details;
	BufferedReader bufferedReader=null;
	try {
		dbConnect = ConnectionUtil.connection();
		dbConnect.setAutoCommit(false);
		bufferedReader = new BufferedReader(new FileReader(file));
		line = bufferedReader.readLine();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	try {
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
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dbConnect.commit();
		try {
			Files.move(file.toPath(),
			        Paths.get(processedFolder.getAbsolutePath(), file.getName()),
			        StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Processed successfully: " + file.getName());
		
		}
		catch (SQLException e) {
//		try {
//			dbConnect.rollback();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} // rollback entire file
		try {
			bufferedReader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}try {
		Files.move(file.toPath(),
		        Paths.get(errorFolder.getAbsolutePath(), file.getName()),
		        StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Error while processesing: " + file.getName()+"\nTransaction rolled back");
		
		}catch (Exception ea) {}
		
    }
}}