package org.eclipse.codewind.microclimate.cache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.HttpURLConnection;
import java.util.Date;

import org.eclipse.codewind.microclimate.test.util.AbstractMicroclimateTest;
import org.eclipse.codewind.microclimate.test.util.Logger;
import org.eclipse.codewind.microclimate.test.util.MicroclimateTestUtils;
import org.eclipse.codewind.microclimate.test.util.MicroclimateTestUtils.PROJECT_TYPES;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PythonProjectCache extends AbstractMicroclimateTest {
	
	private static String exposedPort;
	private static String projectName = "pythoncache";
	private static String testType = System.getProperty("testType");
	private static PROJECT_TYPES projectType = PROJECT_TYPES.python;
	
	@Test(timeout=60000) //60 seconds timeout
	public void TestAcreate(){
		String urlParameters  ="{\"name\": \"" + projectName + "\",\"extension\": \"templateExample\",\"language\": \"python\"}";
		
		try {
			int HttpResult = MicroclimateTestUtils.projectCreation(urlParameters, testType);
			Logger.println(PythonProjectCache.class, "TestAcreate()", "HttpResult is: " + HttpResult);
			assertTrue(HttpResult == HttpURLConnection.HTTP_ACCEPTED);
		}catch(Exception e) {
			Logger.println(PythonProjectCache.class, "TestAcreate()", "Exception occurred during project creation: " + e.getMessage(),e);
			fail("Exception occurred during project creation.");
		}
		
		return;
	}
	
	@Test(timeout=600000) //10 mins timeout
	public void TestBcheckForContainer() {
		try {
			exposedPort = MicroclimateTestUtils.getexposedPort(projectName, testType, projectType);
			Logger.println(PythonProjectCache.class, "TestCcheckForContainer()", "Exposed Port is " + exposedPort);
			assertNotNull("exposedPort for project " + projectName +" is null", exposedPort);
		}catch(Exception e) {
			Logger.println(PythonProjectCache.class, "TestCcheckForContainer()", "Exception occurred when looking for exposedport: " + e.getMessage(),e);
			fail("Exception occurred when looking for exposedport");
		}
		
		return;
	}
	
	@Test(timeout=600000) //10 mins timeout
	public void TestCcheckEndpoint() {
		assertNotNull("exposedPort for project " + projectName +" is null", exposedPort);
		String expectedString = "Hello World!";
		String api = "/";
		
		try {
			while(true) {
				if(MicroclimateTestUtils.checkEndpoint(expectedString, exposedPort, api, testType)) {
					return;
				} else {
					Thread.sleep(3000);
				}
			}
		}catch(Exception e) {
			Logger.println(PythonProjectCache.class, "TestEcheckEndpoint()", "Exception occurred when checking for endpoint",e);
			fail("Exception occurred when checking for endpoint");
		}	
	}

}
