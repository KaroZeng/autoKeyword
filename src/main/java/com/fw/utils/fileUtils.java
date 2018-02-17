package com.fw.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class fileUtils {

	/**
	 * 获取指定路径下properties文件并返回其对象
	 * @param propPath
	 * @return
	 */
	public static Properties getProperties(String propPath){
		Properties prop = new Properties();
		try {
			prop.load(new FileReader(propPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	
	/**
	 * @param path 指定文件夹路径
	 * @return List<String>	返回excel文件路径列表
	 */
	public static List<String> getExcelList(String path){   
		List<String> excelList=new ArrayList<String>();
		File file = new File(path);   
		File[] array = file.listFiles();
		for(int i=0;i<array.length;i++){   
			if(array[i].isFile()){            	
				if (array[i].getPath().endsWith(".xls")||array[i].getPath().endsWith(".xlsx")) {
					excelList.add(array[i].getPath());
				}
			}
		}
		return excelList;
	} 
	
	
}
