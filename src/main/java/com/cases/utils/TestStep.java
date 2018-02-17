package com.cases.utils;

import java.util.Map;

public class TestStep {

	private String StepID;
	private String StepName;
	private String OperateMode;
	private String OperateElement;
	private String OperateData;
	private String CheckData;
	
	private String StepResult;
	public static final String PASS="Pass";
	public static final String FAILED="Failed";
	
	private String ErrInfo;
	private String ScreenShotPath;
	
	public TestStep(Map<String, String> stepMap) {
		setStepID(stepMap.get("StepID"));
		setStepName(stepMap.get("StepName"));
		setOperateMode(stepMap.get("OperateMode"));
		setOperateElement(stepMap.get("ElementName"));
		setOperateData(stepMap.get("RunData"));
		setCheckData(stepMap.get("CheckData"));
	}

	public String getStepID() {
		return StepID;
	}

	public void setStepID(String stepID) {
		StepID = stepID;
	}

	public String getStepName() {
		return StepName;
	}

	public void setStepName(String stepName) {
		StepName = stepName;
	}

	public String getOperateMode() {
		return OperateMode;
	}

	public void setOperateMode(String operateMode) {
		OperateMode = operateMode;
	}

	public String getOperateElement() {
		return OperateElement;
	}

	public void setOperateElement(String operateElement) {
		OperateElement = operateElement;
	}

	public String getOperateData() {
		return OperateData;
	}

	public void setOperateData(String operateData) {
		OperateData = operateData;
	}

	public String getCheckData() {
		return CheckData;
	}

	public void setCheckData(String checkData) {
		this.CheckData = checkData;
	}

	public String getStepResult() {
		return StepResult;
	}

	public void setStepResult(String stepResult) {
		StepResult = stepResult;
	}

	public String getErrInfo() {
		return ErrInfo;
	}

	public void setErrInfo(String errInfo) {
		ErrInfo = errInfo;
	}

	public String getScreenShotPath() {
		return ScreenShotPath;
	}

	public void setScreenShotPath(String screenShotPath) {
		ScreenShotPath = screenShotPath;
	}
	
	
	
}
