package com.litmus7.inventorymanagement.file;

import java.io.FileWriter;
import java.io.IOException;

import com.litmus7.inventorymanagement.dto.InventoryManagementDTO;

import java.io.BufferedWriter;

public class FileManagement {
	
	
	
	public void writeFile(InventoryManagementDTO fieldEntry, String filename) throws IOException {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename,true))) {
			String content=fieldEntry.getSKU()+","+fieldEntry.getProductName()+","+fieldEntry.getQuantity()+","+fieldEntry.getPrice()+"\n";
			bufferedWriter.write(content);
		}catch (Exception e) {}
	}

}
