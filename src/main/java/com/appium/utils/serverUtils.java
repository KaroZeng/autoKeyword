package com.appium.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fw.utils.Log4j;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class serverUtils {
	String projectpath = System.getProperty("user.dir");
	private String Appium_Node_Path = "/usr/local/Cellar/node/8.4.0/bin/node";
	private String Appium_JS_Path = "/usr/local/lib/node_modules/appium/build/lib/main.js";
	private String appiumLogfloder=projectpath+"/Log/appiumSerLog";
	private AppiumDriverLocalService service;
	public String service_url;

	
	/**
	 *根据提供的端口号(port)启动对应appiumService
	 *保存日志到appiumSerLog目录下
	 */
	public void appiumStart(int port) {
		
		Map<String, String> androidenv = new HashMap<String, String>(System.getenv());
		androidenv.put("ANDROID_HOME", "/Users/zenghuiming/Desktop/eclipsePlugin/android_sdks");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		String logPath=appiumLogfloder+"/"+port+"_"+date+".log";
		
		service = AppiumDriverLocalService.buildService(
				new AppiumServiceBuilder()
				.withEnvironment(androidenv)
				.usingDriverExecutable(new File(Appium_Node_Path))
				.withAppiumJS(new File(Appium_JS_Path))
				.usingPort(port)
				.withLogFile(new File(logPath))
				);
		service.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service_url = service.getUrl().toString();
		Log4j.Info("√ start appium service with " + service_url + " success！");
		Log4j.Info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
	}

	public void appiumStop() {
		service.stop();

	}

}
