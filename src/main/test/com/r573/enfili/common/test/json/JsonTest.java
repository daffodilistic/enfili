package com.r573.enfili.common.test.json;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r573.enfili.common.json.JsonUtil;

public class JsonTest {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(JsonTest.class);
	
	private static String TEST_JSON = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"title\":\"Project Manager\",\"phoneNumber\":\"123412312\",\"email\":\"jsmith@acme.com\"}";
	private static Employee TEST_OBJ = new Employee("John", "Smith", "Project Manager", "123412312", "jsmith@acme.com");
	private static Map<String,Object> TEST_MAP;
	static{
		TEST_MAP = new HashMap<String, Object>();
		TEST_MAP.put("firstName", TEST_OBJ.getFirstName());
		TEST_MAP.put("lastName", TEST_OBJ.getLastName());
		TEST_MAP.put("title", TEST_OBJ.getTitle());
		TEST_MAP.put("phoneNumber", TEST_OBJ.getPhoneNumber());
		TEST_MAP.put("email", TEST_OBJ.getEmail());
	}
	
	@Test
	public void toJson(){
		String json = JsonUtil.toJson(TEST_OBJ);
		Assert.assertEquals(TEST_JSON, json);
	}
	@Test
	public void fromJson(){
		Employee employee = JsonUtil.fromJson(TEST_JSON, Employee.class);
		Assert.assertEquals(TEST_OBJ, employee);
	}
	@Test
	public void toMap(){
		Map<String,Object> map = JsonUtil.convertToMap(TEST_OBJ);
		Assert.assertEquals(TEST_MAP, map);
	}
}
