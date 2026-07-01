package org.sujalkhot.day24;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.formula.functions.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleReadExcel {

	public static void main(String[] args) throws IOException {
		// 1.Open file
		FileInputStream fis = new FileInputStream("src\\main\\resources\\excel\\ReadExcel.xlsx");

		// 2.Load workbook
		Workbook workbook = new XSSFWorkbook(fis);

		// 3.Get sheet - pass sheet name
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("Data");

//		// 4.Get row (row index start form 0)
//		Row row = sheet.getRow(1);
//		
//		//Row row = sheet.getRow(1);	//for second row below cell code will as it is
//
//		// 5.Get cells
//		Cell nameCell = row.getCell(0);
//		Cell cityCell = row.getCell(1);
//
//		// 6.print Value
//		System.out.println("Name : " + nameCell.toString());
//		System.out.println("City : " + cityCell.toString());

		// Reading All Records
		// Getting Last Row Number
		int lastRow = sheet.getLastRowNum();

		for (int i = 1; i <= lastRow; i++) {
			Row row = sheet.getRow(i);

			String name = row.getCell(0).toString();
			String city = row.getCell(1).toString();

			System.out.println(name + "-" + city);
		}

		// 7.Close
		workbook.close();
		fis.close();

	}

}
