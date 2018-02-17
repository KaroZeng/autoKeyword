package com.fw.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

public class Log4j {
	
	private static final Logger logger = Logger.getLogger(Log4j.class);  
	
	private static String log4jConfigPath="src/main/java/com/config/Log4j.properties";

	private static boolean flag=false;
	
	static{
	//初始化时候生成log存放路径提供给 log4j.properties调用
		String projectpath = System.getProperty("user.dir");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String date = sdf.format(new Date());
		final String logPath=projectpath+"/Log/runlog/"+date+".log";
		System.setProperty("logPath", logPath);
	}
	
	private static synchronized void getPropertyFile(){	
		PropertyConfigurator.configure(new File(log4jConfigPath).getAbsolutePath());
		flag=true;
	}
	
	private static void getFlag(){
		if (flag==false) {
			Log4j.getPropertyFile();
		}
	}
	
	public static void Info(String message){
		Log4j.getFlag();
		logger.info(message);
	}
	
	public static void Error(String message){
		Log4j.getFlag();
		logger.error(message);
	}
	public static void Warn(String message){
		Log4j.getFlag();
		logger.warn(message);
	}
	public static void Debug(String message){
		Log4j.getFlag();
		logger.debug(message);
	}

	
	@Test
    public void test() {  
		Log4j.Info("输出日志");
		Log4j.Error("错误日志");
		Log4j.Warn("警告日志");	
    }  

}
