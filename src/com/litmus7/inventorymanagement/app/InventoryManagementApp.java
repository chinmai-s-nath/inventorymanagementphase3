package com.litmus7.inventorymanagement.app;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.litmus7.inventorymanagement.util.ConnectionUtil;
import com.litmus7.inventorymanagement.fileprocessing.*;
public class InventoryManagementApp {

	 static File inputFolder = new File("E:\\Eclipse workspace\\InventoryManagementPhase3\\inventory feed\\input");
    
	public static void main(String[] args) throws IOException, SQLException, InterruptedException {
		
		ExecutorService executor=Executors.newFixedThreadPool(5);
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
	        	try {
	        	executor.submit(new FileProcessing(file));
	        	dbConnect.commit();
	        	}
	        	catch (Exception e) {
	        		dbConnect.rollback();
	        	}
		
	}
	        executor.shutdown();
	        executor.awaitTermination(10, TimeUnit.SECONDS);
	        dbConnect.close();
	}
}


