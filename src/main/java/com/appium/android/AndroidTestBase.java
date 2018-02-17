package com.appium.android;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.appium.utils.serverUtils;
import com.data.excel.excelUtils;
import com.fw.utils.Log4j;

public class AndroidTestBase {

	private serverUtils appiumSer = new serverUtils();
	private AndroidUtils driverUtils = new AndroidUtils();
	private excelUtils excelUtils = new excelUtils();
	public WebDriver driver = null;

	public Map<String, Map<String, String>> elementsMap;
	public Map<String, List<Map<String, String>>> caseSuiteMap;

	/**
	 * 通过xml配置的参数或者默认参数初始化 appiumServer，appiumDriver和将要执行的用例集合
	 * @param serverPort
	 *		端口设置配置
	 * @param testDevice
	 * 		测试设备配置
	 * @param CaseExcelName
	 * 		测试用例集合excel文件名
	 */

	@Parameters({ "serverPort", "testDevice", "CaseExcelName" })
	@BeforeClass
	public void init(
			@Optional("4724") String serverPort, 
			@Optional("emulator-5554") String testDevice,
			@Optional("CaseScript_V0.1.xlsx") String CaseExcelName) {
		Log4j.Info(serverPort+"|"+testDevice+"|"+CaseExcelName);
		int port = Integer.parseInt(serverPort);
		appiumSer.appiumStart(port);
		System.out.println(appiumSer.service_url);
		excelUtils.loadCaseExcel(CaseExcelName);
		driverUtils.initExcel(excelUtils);
		driver = driverUtils.initAppiumDriver(appiumSer.service_url, testDevice);
	}

	
	/**
	 * 将excel里用例列表转化成 dataprovider对象
	 * @return List
	 * 		List(Map)
	 */
	@DataProvider
	public Object[][] casesList() {
		Iterator<Map<String, String>> it = excelUtils.caselist.iterator();
		int i = 0;
		Object[][] caseslist = new Object[excelUtils.caselist.size()][];
		while (it.hasNext()) {
			Map<String, String> tmpMap = it.next();
			Object[] tempObj = new Object[] { tmpMap };
			caseslist[i] = tempObj;
			i++;
		}
		return caseslist;
	}
	/**
	 *通过CaseID获取用例步骤并按照操作类型区分执行
	 * @param caseMap
	 */
	@Test(dataProvider = "casesList")
	public void runCase(Map<String, String> caseMap) {
		Log4j.Info("RunCase:" + caseMap);
		List<Map<String, String>> stepsList = excelUtils.caseSuiteMap.get(caseMap.get("CaseID"));
		List<Map<String, String>> runDataList=excelUtils.getRunData(caseMap.get("CaseID"));
		//没有参数化用例
		if (runDataList==null) {
			for (Map<String, String> stepMap : stepsList) {
				driverUtils.runStep(stepMap,null);
			}
		}
		//参数化用例遍历数据并使用数据完成步骤遍历
		else {
			for (Map<String, String> rundataMap : runDataList) {
				for (Map<String, String> stepMap : stepsList) {
					driverUtils.runStep(stepMap,rundataMap);
				}
			}
		}
	}
	
	@AfterClass
	public void close() {
		excelUtils = null;
		driver.quit();
		driverUtils = null;
		// appiumSer.appiumStop();
		appiumSer = null;
	}

}
