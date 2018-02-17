package com.cases.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestCase {

	private String ModuleName;
	private String CaseID;
	private String CaseName;
	private String Precondition;
	private List<TestStep> CaseSteps;
	private List<Map<String, String>> TestData;
	
	private String CaseResult;
	public static final String PASS="Pass";
	public static final String FAILED="Failed";
	
	public TestCase(List<Map<String,String>> caseList) {
		setModuleName(caseList.get(0).get("ModuleName"));
		setCaseID(caseList.get(0).get("CaseID"));
		setCaseName(caseList.get(0).get("CaseName"));
		setPrecondition(caseList.get(0).get("Precondition"));
		setCaseSteps(caseList);
		//TODO:未实现测试数据初始化
		
	}

	
	public String getModuleName() {
		return ModuleName;
	}

	public void setModuleName(String moduleName) {
		ModuleName = moduleName;
	}

	public String getCaseID() {
		return CaseID;
	}

	public void setCaseID(String caseID) {
		CaseID = caseID;
	}

	public String getCaseName() {
		return CaseName;
	}

	public void setCaseName(String caseName) {
		CaseName = caseName;
	}

	public String getPrecondition() {
		return Precondition;
	}


	public void setPrecondition(String precondition) {
		Precondition = precondition;
	}


	public List<Map<String, String>> getTestData() {
		return TestData;
	}


	public void setTestData(List<Map<String, String>> testData) {
		//TODO:实现测试数据获取方式
		TestData = testData;
	}


	public List<TestStep> getCaseSteps() {
		return CaseSteps;
	}

	public void setCaseSteps(List<Map<String, String>> caseSteps) {
		List<TestStep> testSteps=new ArrayList<TestStep>();
		for (Map<String, String> stepMap : caseSteps) {
			TestStep ts=new TestStep(stepMap);
			testSteps.add(ts);
		}
		CaseSteps = testSteps;
	}

	public String getCaseResult() {
		return CaseResult;
	}
	
	public void setCaseResult(String caseResult) {
		CaseResult = caseResult;
	}
	
	
	
	
}
