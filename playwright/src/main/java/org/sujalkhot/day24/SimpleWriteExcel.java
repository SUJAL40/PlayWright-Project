package org.sujalkhot.day24;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleWriteExcel {

	public static void main(String[] args) throws IOException {

		// 1. Create workbook
		Workbook workbook = new XSSFWorkbook();

		// 2. Create Sheet
		Sheet sheet = workbook.createSheet("Data");

		// 3. Create Row
		Row row = sheet.createRow(0);

		// 4. Create cells and set vales
		row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("City");

		Row row1 = sheet.createRow(1);

		// 4. Create cells and set vales
		row1.createCell(0).setCellValue("Sujal");
		row1.createCell(1).setCellValue("Ichal");

		// 5. Write to file
		FileOutputStream fos = new FileOutputStream("src\\main\\resources\\excel\\ReadExcel.xlsx");
		workbook.write(fos);

		// 6. Close
		workbook.close();
		fos.close();

		System.out.println("Data excel file created Succesfully..............");

	}

}
