package excelToJson.ExcelToJson;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {
	
	public static void main(String[] args)throws IOException {
		// Step 1: Read Excel File into Java List Objects
		List customers = readExcelFile("customers.xlsx");
		
		// Step 2: Convert Java Objects to JSON String
		String jsonString = convertObjects2JsonString(customers);
		
		System.out.println(jsonString);
	}
	
	/**
	 * Read Excel File into Java List Objects
	 * 
	 * @param filePath
	 * @return
	 */
	private static List readExcelFile(String filePath)throws IOException{
		try {
			FileInputStream excelFile = new FileInputStream(new File(filePath));
    		Workbook workbook = new XSSFWorkbook(excelFile);
     
    		Sheet sheet = workbook.getSheet("Customers");
    		Iterator rows = sheet.iterator();
    		
    		List lstCustomers = new ArrayList();
    		
    		int rowNumber = 0;
    		while (rows.hasNext()) {
    			Row currentRow = (Row) rows.next();
    			
    			// skip header
    			if(rowNumber == 0) {
    				rowNumber++;
    				continue;
    			}
    			
    			Iterator cellsInRow = currentRow.iterator();
 
    			User cust = new User();
    			
    			int cellIndex = 0;
    			while (cellsInRow.hasNext()) {
    				Cell currentCell = (Cell) cellsInRow.next();
    				
    				if(cellIndex==0) { // ID
    					cust.setId(String.valueOf(currentCell.getNumericCellValue()));
    				} else if(cellIndex==1) { // Name
    					cust.setName(currentCell.getStringCellValue());
    				} else if(cellIndex==2) { // Address
    					cust.setAddress(currentCell.getStringCellValue());
    				} else if(cellIndex==3) { // Age
    					cust.setAge((int) currentCell.getNumericCellValue());
    				}
    				
    				cellIndex++;
    			}
    			
    			lstCustomers.add(cust);
    		}
    		
    		// Close WorkBook
    		workbook.close();
    		
    		return lstCustomers;
        } catch (IOException e) {
        	throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
	}
	
	/**
	 * Convert Java Objects to JSON String
	 * 
	 * @param customers
	 * @param fileName
	 */
	private static String convertObjects2JsonString(List customers) {
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString = "";
    	
    	try {
    		jsonString = mapper.writeValueAsString(customers);
    	} catch (JsonProcessingException e) {
    		e.printStackTrace();
    	}
    	
    	return jsonString; 
	}
}