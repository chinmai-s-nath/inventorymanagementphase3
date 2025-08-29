package com.litmus7.inventorymanagement.app;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.litmus7.inventorymanagement.util.ConnectionUtil;
import com.litmus7.inventorymanagement.fileprocessing.*;
public class InventoryManagementApp {

	 static File inputFolder = new File("E:\\Eclipse workspace\\InventoryManagementPhase2\\inventory feed\\input");
    
	public static void main(String[] args) throws IOException, SQLException {
	
        Connection dbConnect=ConnectionUtil.connection();
        //FileProcessing fileProcessing;
		File[] files = inputFolder.listFiles((dir, name) -> name.endsWith(".csv"));
		try{
			
	        if (files == null || files.length == 0) {
	            System.out.println("No CSV files found in input folder.");
	            return;
	        }
		}catch (Exception e) {}
	        
	        for (File file : files) {
	        	dbConnect.setAutoCommit(false);
	        	Thread thread=new Thread(new FileProcessing(file));
	        	//fileProcessing.fileProcessor(file,dbConnect);
	        	thread.start();
		
	}
	        dbConnect.close();
	}
}


