
package stepDefinations;

import static io.restassured.RestAssured.given;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONObject;

import static org.junit.Assert.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import junit.framework.Assert;
import resources.Utils;
import reusables.ReusableMethods;

public class StepDefination extends ReusableMethods {
	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;
	static String place_id;
	JSONObject obj;
	Utils utils = new Utils();
	HashMap<String,String> hs;
	static String bitLink;
	static String bitLinkID;
	static String url;	
	
	
	@SuppressWarnings("unchecked")
	@Given("get the payload for {string}")
	public void getPayload(String testcaseName) throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		try {
			hs = new HashMap<String,String>();
			hs = getData(testcaseName);
			obj = new JSONObject(hs.get("Request").toString());
			System.out.println(obj);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	@Given("Update payload for {string} with {string} as {string}, {string} as {string} and {string} as {string}")
	public void update_payload_with_as_as_and_as(String testcaseName, String longUrlKey, String longUrlValue, String domainKey, String domainValue, 
			String titleKey, String titleValue) throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		
		try {
			hs = new HashMap<String,String>();
			hs = getData(testcaseName);
			obj = new JSONObject(hs.get("Request").toString());
			System.out.println(obj);
			updateJson(obj, longUrlKey, longUrlValue);
			updateJson(obj, domainKey, domainValue);
			obj = updateJson(obj, titleKey, titleValue);
			System.out.println(obj);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@When("user calls the API {string}")
	public void user_calls(String string) throws Exception {
		// Write code here that turns the phrase above into concrete actions
		try {
			//System.out.println(hs.get("RequestURL"));
			if(hs.get("RequestType").equalsIgnoreCase("Post")) {
				response = given().spec(utils.requestSpecification())
						.body(obj.toString()).when().post(hs.get("RequestURL")).then().extract().response();
			}else if(hs.get("RequestType").equalsIgnoreCase("Get")) {
				response = given().spec(utils.requestSpecification())
						.when().get(url).then().extract().response();
			}

			System.out.println("Response is: "+response.asString());
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Then("the API call got executed with status code {int}")
	public void the_API_call_got_executed_with_status_code(int statusCode) throws Exception {
		// Write code here that turns the phrase above into concrete actions

		try {
			System.out.println("Status code is: "+response.getStatusCode());
			assertEquals(statusCode, response.getStatusCode());
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	@Then("verify {string} in response body is {string}")
	public void in_response_body_is(String key, String value) throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		try {
			String actualValue = getJsonValue(response.asString(), key);
			System.out.println(actualValue);
			assertTrue(actualValue.contains(value));	
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Then("fetch Bitlink and BitlinkID from the response")
	public void fetch_Bitlink_and_BitlinkID_from_the_response() throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		try {
			bitLink = getJsonValue(response.asString(), "link");
			System.out.println(bitLink);
			String[] bitLinks = bitLink.split("/");
			System.out.println(bitLinks.length-1);
			System.out.println(bitLinks[3]);
			bitLinkID =bitLinks[bitLinks.length-1];
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	@Given("get and update Url details for {string}")
	public void update_RequestUrl(String testcaseName) throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		
		try {
			hs = new HashMap<String,String>();
			hs = getData(testcaseName);
			url = hs.get("RequestURL");
			if(url.contains("BitlinkID")) {
			url = url.replaceFirst("BitlinkID", bitLinkID);
			}
			System.out.println("Url is: "+url);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Then("verify {string} in response body")
	public void verifyResponse_body(String key) throws Exception {
	    // Write code here that turns the phrase above into concrete actions
		try {
			String actualValue = getJsonValue(response.asString(), key);
			System.out.println(actualValue);
			assertTrue(actualValue.contains(bitLink));
		}
		catch(Exception e) {
			throw e;
		}
	}


}
