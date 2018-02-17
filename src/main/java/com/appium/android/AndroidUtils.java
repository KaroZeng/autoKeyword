package com.appium.android;

import static org.testng.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.data.excel.excelUtils;
import com.fw.utils.Log4j;
import com.fw.utils.booleanUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;

public class AndroidUtils {

	public MobileDriver<?> driver = null;

	public excelUtils excelUtils;

	public void initExcel(excelUtils excelUtils) {
		this.excelUtils = excelUtils;
	}


	public String platformName = "";
	public String platformVersion = "";

	/**
	 * 通过提供的键值对生成对应的android DesiredCapabilities对应提供初始化driver使用
	 * 
	 * @param deviceMap
	 * @return DesiredCapabilities(android)
	 */
	public DesiredCapabilities getandroidCapabilities(Map<String, String> deviceMap) {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		if (deviceMap.get("g_platformName").equals("") || deviceMap.get("g_platformName") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[platformName]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("g_platformVersion").equals("") || deviceMap.get("g_platformVersion") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[platformVersion]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("g_devicesName").equals("") || deviceMap.get("g_devicesName") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[devicesName]是否配置正确！！！");
			capabilities = null;
		}

		else if (deviceMap.get("android_appPackage").equals("") || deviceMap.get("android_appPackage") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[appPackage]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("android_appActivity").equals("") || deviceMap.get("android_appActivity") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[appActivity]是否配置正确！！！");
			capabilities = null;
		} else {
			if (deviceMap.get("g_app") == null || deviceMap.get("g_app").equals("")) {
				Log4j.Warn("您未设置测试app的路径请确认手机已安装app测试包!");
			} else {
				capabilities.setCapability(MobileCapabilityType.APP, deviceMap.get("g_app"));
			}
			// 设备信息配置部分
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, deviceMap.get("g_platformName"));
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceMap.get("g_platformVersion"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceMap.get("g_devicesName"));
			capabilities.setCapability("udid", deviceMap.get("g_udid"));
			// 测试app配置部分

			capabilities.setCapability("appPackage", deviceMap.get("android_appPackage"));
			capabilities.setCapability("appActivity", deviceMap.get("android_appActivity"));

			// 测试过程中手机相关配置
			capabilities.setCapability("unicodeKeyboard",booleanUtil.returnBoolean((String)deviceMap.get("android_unicodeKeyboard")));
			capabilities.setCapability("resetKeyboard",booleanUtil.returnBoolean((String)deviceMap.get("android_resetKeyboard")));
			capabilities.setCapability("noReset", booleanUtil.returnBoolean((String)deviceMap.get("g_noReset")));
		}
		Log4j.Info(capabilities.toString());
		return capabilities;
	}

	/**
	 * 通过提供的键值对生成对应的IOS DesiredCapabilities对应提供初始化driver使用
	 * 
	 * @param deviceMap
	 * @return DesiredCapabilities(IOS)
	 */
	public DesiredCapabilities getiosCapabilities(Map<String, String> deviceMap) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (deviceMap.get("g_platformname").equals("") || deviceMap.get("g_platformname") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[platformName]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("g_platformversion").equals("") || deviceMap.get("g_platformversion") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[g_platformversion]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("g_devicesname").equals("") || deviceMap.get("g_devicesname") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[g_devicesname]是否配置正确！！！");
			capabilities = null;
		} else if (deviceMap.get("ios_bundleid").equals("") || deviceMap.get("ios_bundleid") == null) {
			Log4j.Error("请查看mobile_appdriverconfig表中的[ios_bundleid]是否配置正确！！！");
			capabilities = null;
		} else {
			// 设备信息配置部分
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, deviceMap.get("g_platformname"));
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceMap.get("g_platformversion"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceMap.get("g_devicesname"));
			capabilities.setCapability(MobileCapabilityType.UDID, deviceMap.get("g_udid"));
			capabilities.setCapability("bundleId", deviceMap.get("ios_bundleid"));
			if (deviceMap.get("g_app") == null || deviceMap.get("g_app").equals("")) {
				Log4j.Warn("您未设置测试app的路径请确认手机已安装app测试包!");
			} else {
				capabilities.setCapability(MobileCapabilityType.APP, deviceMap.get("g_app"));
			}
		}
		return capabilities;
	}

	/**
	 * 通过初始化键值对生成一个androiddriver
	 * 
	 * @param devicesMap
	 * @return AndroidDriver
	 */
	public AndroidDriver<?> getAndroidDriver(String appiumSerUrl, Map<String, String> devicesMap) {
		AndroidDriver<?> driver = null;
		DesiredCapabilities capabilities = getandroidCapabilities(devicesMap);
		try {
			driver = new AndroidDriver<WebElement>(new URL(appiumSerUrl), capabilities);
		} catch (MalformedURLException e) {
			Log4j.Error("初始化androidDriver失败！！！！！");
			e.printStackTrace();
		}
		return driver;
	}

	/**
	 * 通过初始化键值对生成一个iosdriver
	 * 
	 * @param devicesMap
	 * @return IOSDriver
	 */
	public IOSDriver<?> getIOSDriver(String appiumSerUrl, Map<String, String> devicesMap) {
		IOSDriver<WebElement> driver = null;
		DesiredCapabilities capabilities = getiosCapabilities(devicesMap);
		try {
			driver = new IOSDriver<WebElement>(new URL(appiumSerUrl), capabilities);
		} catch (MalformedURLException e) {
			Log4j.Error("初始化iosDriver失败！！！！！");
			e.printStackTrace();
		}
		return driver;
	}

	/**
	 * 通过初始化信息名获取初始化数据并生成对应平台的driver
	 * 
	 * @param initName
	 * @return init AppiumDriver(AndroidDriver/IOSDriver)
	 */
	public WebDriver initAppiumDriver(String appiumSerUrl, String initName) {
		Map<String, String> devicesMap = excelUtils.getAppiuDriverinitData(initName);
		Log4j.Info(devicesMap.toString());
		platformName = devicesMap.get("g_platformName");
		Log4j.Info(platformName);
		if (platformName.toLowerCase().equals("android")) {
			driver = getAndroidDriver(appiumSerUrl, devicesMap);
		} else if (platformName.toLowerCase().equals("ios")) {
			driver = getIOSDriver(appiumSerUrl, devicesMap);
		} else {
			Log4j.Error("platformName为初始化必填查询，请确认配置表配置参数！appdriverconfig");
			driver = null;
		}
		return driver;
	}

	// ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
	// 元素定位部分
	// ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆

	/**
	 * @方法功能描述:通过定位信息生成By定位对象
	 * @方法名:getByObj
	 * @返回类型:By
	 * @时间:2017-08-14下午23:40:21
	 * @author huizeng
	 */
	public By getByObj(String elementbyway, String elementvalue) throws Exception {

		By eBy = null;
		if (elementbyway.toLowerCase().equals("xpath")) {
			eBy = By.xpath(elementvalue);
		}
		if (elementbyway.toLowerCase().equals("id")) {
			eBy = By.id(elementvalue);
		}
		if (elementbyway.toLowerCase().equals("name")) {
			eBy = By.name(elementvalue);
		}
		if ((elementbyway.toLowerCase().equals("classname")) || (elementbyway.toLowerCase().equals("class"))) {
			eBy = By.className(elementvalue);
		}
		if ((elementbyway.toLowerCase().equals("linktext")) || (elementbyway.toLowerCase().equals("link"))) {
			eBy = By.linkText(elementvalue);
		}

		if ((elementbyway.toLowerCase().equals("tagname")) || (elementbyway.toLowerCase().equals("tag"))) {
			eBy = By.tagName(elementvalue);
		}
		if (elementbyway.toLowerCase().equals("partiallinktext")) {
			eBy = By.partialLinkText(elementvalue);
		}
		if ((elementbyway.toLowerCase().equals("cssselector")) || (elementbyway.toLowerCase().equals("css"))) {
			eBy = By.cssSelector(elementvalue);
		}
		if (elementbyway.toLowerCase().equals("text")) {
			String xpath="//*[contains(@text,'"+elementvalue+"')]";
			eBy = By.xpath(xpath);
		}
		return eBy;
	}

	/**
	 * @方法功能描述:通过元素名生成By定位对象
	 * @方法名:getByObjByelmentName
	 * @返回类型:By
	 * @时间:2017-08-14下午23:40:21
	 * @author huizeng
	 */
	public By getByObjByelmentName(String elementName) {
		By by = null;
		Map<String, String> elementInfoMap = excelUtils.elementsMap.get(elementName);
		String elementbyway = elementInfoMap.get("byType");
		String elementvalue = elementInfoMap.get("byData");
		Log4j.Info(elementName + " ByInfo:" + elementbyway + "|" + elementvalue);
		try {
			by = getByObj(elementbyway, elementvalue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return by;
	}

	/**
	 * @方法描述:通过by对象在自定义时间内定位元素
	 * @param By
	 *            elementByObject
	 * @param int
	 *            WaitTime
	 * @return WebElement
	 */
	public WebElement findWithWaitByYourTime(final By elementbyobj, int waitTime) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementbyobj));
		return element;
	}

	public WebElement findWith30s(final By elementbyobj) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementbyobj));
		return element;
	}

	/**
	 * @方法描述:通过by对象在自定义时间内定位元素
	 * @param elementName，waitTime
	 * @return boolean
	 */
	public boolean isElementExist(String elementName, int waitTime) {
		WebElement element = null;
		By byObj = getByObjByelmentName(elementName);
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(byObj));
		} catch (Exception e) {
			e.printStackTrace();
			Log4j.Info("Can not found element:" + elementName);
		}
		if (element == null) {
			return false;
		}
		return true;
	}

	/**
	 * @方法描述:在规定30s内通过元素名获取移动元素对象
	 * @param elementName
	 * @return MobieElement
	 */

	public MobileElement getMobileElement(String elementName) {
		MobileElement element = null;
		By byObj = getByObjByelmentName(elementName);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			element = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(byObj));
			Log4j.Info("Get MobileElement:[" + elementName + "]success!");
		} catch (Exception e) {
			e.printStackTrace();
			Log4j.Info("Can not found MobileElement:" + elementName);
		}
		return element;
	}
	/**
	 * 往指定的输入框中输入对应的数据
	 * 
	 * @param element
	 * @param inputdata
	 */
	public void inputText(String elementName, String inputdata) {
		MobileElement element = getMobileElement(elementName);
		String txt = element.getText();
		if (!(txt.length() == 0)) {
			Log4j.Info("Inputbox has text:" + txt);
			element.clear();
			Log4j.Info("Clear inputbox text");
		}
		try {
			element.setValue(inputdata);
			Log4j.Info("setValue [" + inputdata + "] to [" + elementName + "] succcess");
		} catch (Exception e) {
			element.sendKeys(inputdata);
			Log4j.Info("sendKeys [" + inputdata + "] to [" + elementName + "] succcess");
		}
	}

	/**
	 * 点击指定元素名的元素
	 * 
	 * @param elementName
	 */
	public void click(String elementName) {
		MobileElement element = getMobileElement(elementName);
		element.click();
		
		Log4j.Info("click [" + elementName + "] success");
	}

	/**
	 * 根据选择文本进行选择对应的选项
	 * 
	 * @param elementName
	 * @param selectText
	 */

	public void selectbyText(String elementName, String selectText) {
		MobileElement element = getMobileElement(elementName);
		// TODO:实现下拉框选择
		element.click();
	}

	/**
	 * 往下滑动屏幕直到元素显示
	 * 
	 * @param elementName
	 */
	public void scrollToElement(String elementName) {
		boolean flag = false;
		// get phone screen width and height
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		String platformName = driver.getPlatformName();
		Log4j.Info("phone's  size: " + width + "x" + height);
		// platformName is android
		if (platformName.toLowerCase().trim().equals("android")) {
			for (int i = 0; i < 10; i++) {
				Log4j.Info("the " + (i + 1) + " scroll phone screen ");
				((AndroidDriver<?>) driver).swipe(width / 5, height * 7 / 10, width / 5, height * 3 / 10, 1000);
				flag = isElementExist(elementName, 2);
				if (flag) {
					break;
				}
			}
			if (!flag) {
				Log4j.Error("can not find [" + elementName + "] after scroll screen 10 times");
			}
		}
		// platformName is ios
		else if (platformName.toLowerCase().trim().equals("ios")) {
			for (int i = 0; i < 10; i++) {
				Log4j.Info("the " + (i + 1) + " scroll phone screen ");
				((IOSDriver<?>) driver).swipe(width / 5, height * 7 / 10, width / 5, height * 3 / 10, 1000);
				flag = isElementExist(elementName, 2);
				if (flag) {
					break;
				}
			}
			if (!flag) {
				Log4j.Error("can not find [" + elementName + "] after scroll screen 10 times");
			}
		} else {
			Log4j.Error("phone's platformName is[" + platformName + "]");
		}
	}

	/**
	 * 滑动屏幕，按照提供开始和结束位置与屏幕高宽的百分比进行滑动
	 * 
	 * @param startxp
	 *            width *(startxp/100)，屏幕的宽度*(与宽度的比例/100)
	 * @param startyp
	 * @param endxp
	 * @param endyp
	 */
	public void sliding(String runData) {
		System.out.println(runData);
		String[] b = runData.split("\\|");
		if (b.length != 4) {
			Log4j.Info("sliding data is wrong！");
		} else {
			double startxp, startyp, endxp, endyp;
			startxp = (Integer.parseInt(b[0]));;
			startyp = (Integer.parseInt(b[1]));
			endxp = (Integer.parseInt(b[2]));
			endyp = (Integer.parseInt(b[3]));
			
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			Log4j.Info("screensize:"+width+"X"+height);
			
			int startx = (int) (width * startxp/100);
			int starty = (int) (height * startyp/100);
			int endx = (int) (width * endxp/100);
			int endy = (int) (height * endyp/100);
			
			Log4j.Info("滑动数据："+startx + "x" + starty + "→" + endx + "x" + endy);
			if (startx > endx && starty > endy) {
				Log4j.Info("start sliding: [↖]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx == endx && starty > endy) {
				Log4j.Info("start sliding: [↑]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx < endx && starty > endy) {
				Log4j.Info("start sliding:[↗]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx < endx && starty == endy) {
				Log4j.Info("start sliding:[→]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx < endx && starty < endy) {
				Log4j.Info("start sliding:[↘]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx == endx && starty < endy) {
				Log4j.Info("start sliding:[↓]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx > endx && starty < endy) {
				Log4j.Info("start sliding:[↙]" + startx + "x" + starty + "→" + endx + "x" + endy);
			} else if (startx > endx && starty == endy) {
				Log4j.Info("start sliding:[←]" + startx + "x" + starty + "→" + endx + "x" + endy);
			}
			try {
				if (driver.getPlatformName().toLowerCase().equals("android")) {
					((AndroidDriver<?>) driver).swipe(startx, starty, endx, endy, 1000);
				} else if (driver.getPlatformName().toLowerCase().equals("ios")) {
					((AndroidDriver<?>) driver).swipe(startx, starty, endx, endy, 1000);
				} else {
					Log4j.Error("PlatformName:" + driver.getPlatformName().toLowerCase() + ". please check!");
				}
			} catch (Exception e) {
				Log4j.Error("sliding screen fail!");
			}
		}
	}

	

	
	/**
	 * 截取屏幕功能
	 * 
	 * @return
	 */
	public String captureScreenshot() {
		String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String imagesLocation = "ExceptionPic/";
		new File(imagesLocation).mkdirs();
		String filename = time + imagesLocation + ".jpg";
		try {
			Thread.sleep(2000);
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filename), true);
			Log4j.Info("Capturing screenshot successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return "Error capturing screenshot of test failure";
		}
		return filename;
	}

	public final String CURRENT_PAGESOURSE = "./PageSource" + File.separator + "CurrentPageSourse.xml";

	/**
	 * 获取webview当前页面source信息并保存
	 * 
	 */
	public void GetCurrentPageSourse() {
		long starttimestamp = System.currentTimeMillis();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log4j.Info("尝试获取当前页面信息");
		String currentPageSourse = driver.getPageSource();
		FileWriter mFileWriter;
		BufferedWriter mBufferedWriter = null;
		try {
			mFileWriter = new FileWriter(new File(CURRENT_PAGESOURSE), false);
			mBufferedWriter = new BufferedWriter(mFileWriter);
			mBufferedWriter.write(currentPageSourse);
			mBufferedWriter.close();
			Log4j.Info("  -- OK(耗时" + (System.currentTimeMillis() - starttimestamp) / 1000 + ")");
		} catch (IOException e) {
			e.printStackTrace();
			Log4j.Info("  -- Fail");
		}
	}

	/**
	 * 获取指定元素的Text属性与预期结果进行对比，返回对比结果
	 * 
	 * @param elementName
	 * @param checkData
	 * @return
	 */
	public void checkText(String elementName, String checkData) {
		MobileElement element = getMobileElement(elementName);
		String txt = element.getText();
		Log4j.Info(txt);
		boolean flag = false;
		if (txt.equals(checkData)) {
			flag = true;
		} else {
			Log4j.Error(elementName + "'s  text is [" + txt + "] and checkData is[" + checkData + "]");
		}
		assertEquals(true, flag);
	}

	/**
	 * 根据提供的元素名和检查数据 返回对比结果
	 * 
	 * @param elementName
	 * @param checkData
	 */
	public void checkValue(String elementName, String checkData) {
		MobileElement element = getMobileElement(elementName);
		String value = element.getAttribute("value");
		Log4j.Info(value);
		boolean flag = false;
		if (value.equals(checkData)) {
			flag = true;
		} else {
			Log4j.Error(elementName + "'s  value is [" + value + "] and checkData is[" + checkData + "]");
		}
		assertEquals(flag, true);
	}

	/**
	 * 判断图片是否加载完整
	 * 
	 * @param imageElementName
	 */
	public void checkImageVisible(String imageElementName) {
		MobileElement imageElement = getMobileElement(imageElementName);
		boolean imageLoaded1 = (boolean) ((JavascriptExecutor) driver).executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
				imageElement);
		if (imageLoaded1) {
			Log4j.Info("image is visible");
		} else {
			Log4j.Error("image is not present");
		}
		assertEquals(imageLoaded1, true);
	}

	/**
	 * 判断定义的时间内元素是否有展示在页面上
	 * 
	 * @param elementName
	 * @param waitTime
	 */
	public void checkIsDisplay(String elementName, int waitTime) {
		boolean isDisplay = false;
		WebElement element = null;
		try {
			By by = getByObjByelmentName(elementName);
			element = findWithWaitByYourTime(by, waitTime);
			isDisplay = element.isDisplayed();
			if (isDisplay) {
				Log4j.Info("[" + elementName + "] element is exist and display");
			} else {
				Log4j.Error("[" + elementName + "] element is exist but is not display");
			}
			assertEquals(isDisplay, true);
		} catch (Exception e) {
			Log4j.Error("can not find element[" + elementName + "],it's not display");
		}
	}

	/**
	 * 根据提供的CaseStepMap根据步骤信息分类执行
	 * 
	 * @param stepMap
	 */
	public void runStep(Map<String, String> stepMap,Map<String, String>runDataMap) {
		Log4j.Info("Start Step:" + stepMap.get("StepName")+" : "+runDataMap);
		String elementName = stepMap.get("ElementName");
		// 说去操作方式全部转成小写去除空格防止匹配
		String operateMode = stepMap.get("OperateMode").toLowerCase().trim();

		//根据 runDataMap是否为空，判断步骤数据是否参数化并取得正确数据
		String stepRunData = stepMap.get("RunData");
		String stepCheckData = stepMap.get("CheckData");
		String runData=null;
		String checkData=null;
		if (runDataMap!=null) {
			runData=runDataMap.get(stepRunData);
			checkData=runDataMap.get(stepCheckData);
		}else {
			runData=stepRunData;
			checkData=stepCheckData;
		}
		
		//根据操作类型区分调用方法
		switch (operateMode) {
		// 往对象中输入文本操作
		case "inputtext":
			inputText(elementName, runData);
			break;
		// 点击对象操作
		case "click":
			click(elementName);
			break;
		// 获取对象文本并与预期进行对比检查
		case "checktext":
			checkText(elementName, checkData);
			break;
		// 获取对象Value值并与预期进行对比检查
		case "checkvalue":
			checkValue(elementName, checkData);
			break;
		// 往下滑动屏幕直到元素显示
		case "scrolltoelement":
			scrollToElement(elementName);
			break;
		// 按照选项名字完成下拉选择操作
		case "selectbytext":
			selectbyText(elementName, runData);
			break;
		// 截图功能
		case "capturescreenshot":
			captureScreenshot();
			break;
		// 获取webview页面代码
		case "getcurrentpagesourse":
			GetCurrentPageSourse();
			break;
		//根据提供的滑动数据进行屏幕滑动
		case "sliding":
			sliding(runData);
			break;
		//检查元素在30秒内是否有显示出来
		case "checkisdisplay":
			checkIsDisplay(elementName,30);
			break;
		default:
			Log4j.Info("can not runStep with operateMode:[" + operateMode + "], please call dev to expend.");
			break;
		}
	}

}
