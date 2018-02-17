package com.test.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.cases.utils.TestCase;

public class testCase {

	@Test
	public void debugcase(){
		
		List<Map<String, String>> casemap=new ArrayList<Map<String,String>>();
		
		TestCase testCase=new TestCase(casemap);
		
		
	}
	
}
