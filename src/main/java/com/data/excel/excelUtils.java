package com.data.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;
import com.fw.utils.Log4j;

public class excelUtils {

	private String projectpath = System.getProperty("user.dir");
	private String excelCaseFloderPath = projectpath + "/excelCase";
	private Workbook Workbook = null;

	public List<Map<String, String>> caseSuiteList;
	public Map<String, Map<String, String>> elementsMap;
	public Map<String, List<Map<String, String>>> caseSuiteMap;
	public List<Map<String, String>> caselist = new ArrayList<Map<String, String>>();

	/**
	 * 通过excel文件名加载excelCase下对应的excel文件
	 * 
	 * @param excelName
	 */
	public void loadCaseExcel(String excelName) {
		Workbook = getWorkbook(excelName);
		caseSuiteList = getSheetData("caseSuite");
		elementsMap = getElementMap();
		caseSuiteMap = getCaseSuiteMap();
		caselist=getCaseList();
	}

	/**
	 * 
	 * @param excelName
	 *            项目excelCase文件夹下excel的名字
	 * @return workbook 指定名字excel WorkBook 对象
	 */
	public Workbook getWorkbook(String excelName) {
		String filePath = excelCaseFloderPath + "/" + excelName;
		FileInputStream inStream = null;
		Workbook workBook = null;
		try {
			inStream = new FileInputStream(new File(filePath));
			workBook = WorkbookFactory.create(inStream);
			return workBook;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param sheetName
	 *            将要获取数据的sheet的名字
	 * @return List<Map> sheet页面的数据
	 */
	public List<Map<String, String>> getSheetData(String sheetName) {
		List<List<String>> listData = new ArrayList<List<String>>();
		List<Map<String, String>> mapData = new ArrayList<Map<String, String>>();
		List<String> columnHeaderList = new ArrayList<String>();
		Sheet sheet = setsheet(sheetName);
		if (sheet!=null) {
			int numOfRows = sheet.getLastRowNum() + 1;
			for (int i = 0; i < numOfRows; i++) {
				Row row = sheet.getRow(i);
				Map<String, String> map = new LinkedHashMap<String, String>();
				List<String> list = new ArrayList<String>();
				if (row != null) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						if (i == 0) {
							columnHeaderList.add(getCellValue(cell));
						} else {
							map.put(columnHeaderList.get(j), getCellValue(cell));
						}
						list.add(getCellValue(cell));
					}
				}
				if (i > 0) {
					mapData.add(map);
				}
				listData.add(list);
			}
		}
		else {
			mapData=null;
		}
		return mapData;
	}

	/**
	 * 
	 * @param sheet
	 *            传入需要获取数据的sheet页面对象
	 * @return list 得到页面所有数据 以list<map>格式返回
	 */

	public List<Map<String, String>> getSheetData(Sheet sheet) {
		List<List<String>> listData = new ArrayList<List<String>>();
		List<Map<String, String>> mapData = new ArrayList<Map<String, String>>();
		List<String> columnHeaderList = new ArrayList<String>();
		int numOfRows = sheet.getLastRowNum() + 1;
		for (int i = 0; i < numOfRows; i++) {
			Row row = sheet.getRow(i);
			Map<String, String> map = new LinkedHashMap<String, String>();
			List<String> list = new ArrayList<String>();
			if (row != null) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					if (i == 0) {
						columnHeaderList.add(getCellValue(cell));
					} else {
						map.put(columnHeaderList.get(j), getCellValue(cell));
					}
					list.add(getCellValue(cell));
				}
			}
			if (i > 0) {
				mapData.add(map);
			}
			listData.add(list);
		}
		return mapData;
	}

	/**
	 * 
	 * @param sheetList
	 *            sheet页面所有的用例数据
	 * @param byName
	 *            进行分类的指定字段名
	 * @return List 将sheet页面数据按CaseID分成List<List<Map<String,String>>>
	 */
	public Map<String, List<Map<String, String>>> listGroupByKeyName(List<Map<String, String>> sheetList,
			String byName) {
		Map<String, String> caseStepMap = new LinkedHashMap<String, String>(); // 数据表中每个步骤数据
		Map<String, List<Map<String, String>>> caseSuiteMap = new LinkedHashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < sheetList.size(); i++) {
			caseStepMap = sheetList.get(i);
			if (caseSuiteMap.containsKey(caseStepMap.get(byName))) {
				caseSuiteMap.get(caseStepMap.get(byName)).add(caseStepMap);
			} else {
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				list.add(caseStepMap);
				caseSuiteMap.put(caseStepMap.get(byName), list);
			}
		}
		return caseSuiteMap;
	}

	private List<Map<String, String>> getCaseList() {
		List<Map<String, String>> caseslit=new ArrayList<Map<String,String>>();
		
		for (Map<String, String> map : caseSuiteList) {
			Map<String, String> caseMap = new LinkedHashMap<String, String>();
			caseMap.put("CaseID", map.get("CaseID"));
			caseMap.put("CaseName", map.get("CaseName"));
			if (caseslit.contains(caseMap)) {
			}
			else {
				caseslit.add(caseMap);
			}
		}
		return caseslit;
	}


	/**
	 * 
	 * @param sheetName
	 *            设置默认sheet对象为指定sheet名字的sheet
	 * @return
	 */
	public Sheet setsheet(String sheetName) {
		return Workbook.getSheet(sheetName);
	}

	/**
	 * 
	 * @param cell
	 *            传入指定单元格
	 * @return string 返回单元格内数据
	 */
	@SuppressWarnings("deprecation")
	private String getCellValue(Cell cell) {
		String cellValue = "";
		DataFormatter formatter = new DataFormatter();
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = formatter.formatCellValue(cell);
				} else {
					double value = cell.getNumericCellValue();
					int intValue = (int) value;
					cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}

	/**
	 * 获取指定excel中 elementsMap(sheet)页面所有数据
	 * 
	 * @return Map（String,Map）格式返回结果
	 */
	private Map<String, Map<String, String>> getElementMap() {
		Map<String, Map<String, String>> elementsMap = new HashMap<String, Map<String, String>>();
		List<Map<String, String>> sheetList = getSheetData("ElementsMap");
		for (Map<String, String> elementMap : sheetList) {
			elementsMap.put(elementMap.get("elementName"), elementMap);
		}
		return elementsMap;
	}

	/**
	 * 获取指定excel中caseSuite中的用例数据
	 * 
	 * @return List(string,list(map))
	 */
	private Map<String, List<Map<String, String>>> getCaseSuiteMap() {
		Map<String, List<Map<String, String>>> caseSuiteMap = listGroupByKeyName(caseSuiteList, "CaseID");
		return caseSuiteMap;
	}

	/**
	 * 根据CaseID获取对应用例的运行数据和检查数据
	 * 
	 * @param caseID
	 * @return
	 */
	public List<Map<String, String>> getRunData(String caseID) {
		return getSheetData(caseID);
	}

	public Map<String, String> getAppiuDriverinitData(String initName) {
		Workbook initWorkbook = getWorkbook("appiumDriverConfig.xls");
		Sheet driverConfig = initWorkbook.getSheet("driverConfig");
		List<Map<String, String>> configList = getSheetData(driverConfig);
		Map<String, String> initMap = new LinkedHashMap<String, String>();
		for (Map<String, String> map : configList) {
			if (map.get("initName").equals(initName)) {
				initMap = map;
				break;
			} else {
				initMap = null;
			}
		}
		if (initMap == null) {
			Log4j.Error("没有找到 initName为：" + initName + "的初始化数据，请检查excelCase/appiumDrierConfig.xls");
		}
		return initMap;
	}

	@Test

	public void printfileinfo() {
		loadCaseExcel("CaseScript_V0.1.xlsx");
		System.out.println("###########################################");
		Map<String, String> elementMap = elementsMap.get("input_loginName");
		System.out.println(elementMap.toString());
		System.out.println("###########################################");
		for (String caseid : caseSuiteMap.keySet()) {
			System.out.println("遍历caseid：" + caseid);
			List<Map<String, String>> casestepList = caseSuiteMap.get(caseid);
			System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
			for (Map<String, String> casestep : casestepList) {
				System.out.println(casestep.toString());
			}
		}
		System.out.println("###########################################");
		for (Map<String, String> map : caselist) {
			System.out.println(map);
		}

	}

	// @Test
	// public void testInitDevices(){
	// Map<String, String> map=getAppiuDriverinitData("HuaWeiMate9_cibmb1");
	// System.out.println(map);
	// }
}
