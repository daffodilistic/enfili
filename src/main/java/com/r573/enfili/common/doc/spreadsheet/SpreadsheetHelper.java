/*
 * Enfili
 * Project hosted at https://github.com/ryanhosp/enfili/
 * Copyright 2013 Ho Siaw Ping Ryan
 *    
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.r573.enfili.common.doc.spreadsheet;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetHelper {
	private static final char FIRST_ALPHA_CHAR = 65;
	private static final int NUM_ALPHA_CHARS = 26;
	
	/**
	 * Creates a XSSFWorkbook with 1 default sheet "Sheet1" and returns the Worksheet
	 * This is for simple temporary spreadsheets for calculation purpose
	 * 
	 * @return
	 */
	public static Sheet createSimpleWorksheet(int numCols, int numRows){
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		for(int i=0;i<numRows;i++){
			Row row = sheet.createRow(i);
			for(int j=0;j<numCols;j++){
				row.createCell(j);
			}
		}
		return sheet;
	}
	
	public static void setCellFormula(Sheet sheet, int colId, int rowId, String formula){
		getCell(sheet, colId, rowId).setCellFormula(formula);
	}
	public static void setCellValue(Sheet sheet, int colId, int rowId, int value){
		getCell(sheet, colId, rowId).setCellValue(value);
	}
	public static void setCellValue(Sheet sheet, int colId, int rowId, double value){
		getCell(sheet, colId, rowId).setCellValue(value);
	}
	
	public static Cell getCell(Sheet sheet, int colId, int rowId){
		Row row = sheet.getRow(rowId);
		return row.getCell(colId);
	}
	
	public static void calculate(Sheet sheet){
        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext();)  {
        	Row row = rowIterator.next();
    		for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
    			Cell cell = cellIterator.next();
    			if (cell != null && cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
    				evaluator.evaluateInCell(cell);                 
    			}
    		}
        }
	}
	
	public static boolean isCellEmpty(Cell cell) {
		if (cell == null) {
			return true;
		} else {
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return true;
			} else {
				return false;
			}
		}
	}
	public static int getCellValueAsInt(Sheet sheet, int colId, int rowId) {
		return getCellValueAsInt(getCell(sheet, colId, rowId));
	}
	public static long getCellValueAsLong(Sheet sheet, int colId, int rowId) {
		return getCellValueAsInt(getCell(sheet, colId, rowId));
	}
	public static double getCellValueAsDouble(Sheet sheet, int colId, int rowId) {
		return getCellValueAsDouble(getCell(sheet, colId, rowId));
	}
	public static float getCellValueAsFloat(Sheet sheet, int colId, int rowId) {
		return getCellValueAsFloat(getCell(sheet, colId, rowId));
	}
	public static int getCellValueAsInt(Cell cell) {
		int val = (int) Math.round(cell.getNumericCellValue());
		return val;
	}
	public static long getCellValueAsLong(Cell cell) {
		long val = Math.round(cell.getNumericCellValue());
		return val;
	}
	public static double getCellValueAsDouble(Cell cell) {
		double val = cell.getNumericCellValue();
		return val;
	}
	public static float getCellValueAsFloat(Cell cell) {
		float val = (float)cell.getNumericCellValue();
		return val;
	}

	public static String getSpreadsheetCellReference(Cell cell){
		return getSpreadsheetCellReference(cell.getColumnIndex(), cell.getRowIndex());
	}
	public static String getSpreadsheetCellReference(int col, int row){
		String columnId = getSpreadSheetColumnId(col);
		String rowId = String.valueOf(row+1);
		return columnId + rowId;
	}
	
	/**
	 * Max of 2 char column ID for now (i.e. ~676 spreadsheet columns).
	 * 
	 * @param column
	 * @return
	 */
	private static String getSpreadSheetColumnId(int col) {
		StringBuilder sb = new StringBuilder();

		char minorChar = (char) ((col % NUM_ALPHA_CHARS) + FIRST_ALPHA_CHAR);
		int y = col / NUM_ALPHA_CHARS;
		if (y > 0) {
			char majorChar = (char) (y - 1 + FIRST_ALPHA_CHAR);
			sb.append(majorChar);
		}
		sb.append(minorChar);
		return sb.toString();
	}

}
