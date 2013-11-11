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

import org.apache.poi.ss.usermodel.Cell;

public class SpreadsheetHelper {
	private static final char FIRST_ALPHA_CHAR = 65;
	private static final int NUM_ALPHA_CHARS = 26;
	
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

	public static int getIntCellValue(Cell cell) {
		int val = (int) Math.round(cell.getNumericCellValue());
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
