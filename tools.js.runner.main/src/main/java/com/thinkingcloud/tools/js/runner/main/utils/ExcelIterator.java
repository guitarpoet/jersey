package com.thinkingcloud.tools.js.runner.main.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelIterator implements Iterator<String[]> {

	private Workbook book;
	private int current;
	private int sheetIndex = 0;

	public ExcelIterator(InputStream input) throws IOException {
		book = new HSSFWorkbook(input);
	}

	public void sheet(int i) {
		if (i < book.getNumberOfSheets()) {
			sheetIndex = i;
			current = 0;
		}
	}

	@Override
	public boolean hasNext() {
		return current < book.getSheetAt(sheetIndex).getLastRowNum();
	}

	@Override
	public String[] next() {
		Row row = book.getSheetAt(sheetIndex).getRow(current);
		List<String> str = new ArrayList<String>();
		for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				str.add(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				str.add(String.valueOf(cell.getBooleanCellValue()));
				break;
			case Cell.CELL_TYPE_NUMERIC:
				str.add(String.valueOf(cell.getNumericCellValue()));
				break;
			}
		}
		return str.toArray(new String[str.size()]);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
