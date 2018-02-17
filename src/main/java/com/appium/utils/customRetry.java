package com.appium.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.log4testng.Logger;

public class customRetry implements IRetryAnalyzer {
	public static Logger logger=Logger.getLogger(customRetry.class);
	private int retryCount = 0;
	private int maxRetryCount = 4; // retry a failed test 2 additional times

	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
            String message="方法<"+result.getName()+">执行失败，重试第"+retryCount+"次";
            logger.info(message);
            Reporter.setCurrentTestResult(result);
            Reporter.log(message);
			retryCount++;
			return true;
		}
		return false;
	}

}
