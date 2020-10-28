package RestAssured;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class TC001_APITest {
	static String capitalName;
	static String capCurrCode;
	static String counCurrCode;
	static int statusCodeCountry;
	static int statusCodeCapital;
	ArrayList alCoun=new ArrayList();
	ArrayList alCap=new ArrayList();

	public class RestTest {

		public Response doGetRequest(String endpoint)
		{
			RestAssured.defaultParser = Parser.JSON;

			return
					given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
					when().get(endpoint).
					then().contentType(ContentType.JSON).extract().response();
		}


		public void getCapitalDetailsCountryAPI(String countryAPIURL)
		{
			
			Response response=doGetRequest(countryAPIURL);
			//List<String> jsonResponse = response.jsonPath().getList("$");

			// System.out.println(jsonResponse.size());
			// String capitalNames=response.jsonPath().getString("capital");
			// System.out.println(capitalNames);
			capitalName=response.jsonPath().getString("capital[0]");
			counCurrCode=response.jsonPath().getString("currencies[0].code[0]");
			
			int statusCodeCountry=response.getStatusCode();
			alCoun.add(statusCodeCountry);
			alCoun.add(counCurrCode);
			alCoun.add(capitalName);
			
			//System.out.println(alCoun.get(0)+"-"+alCoun.get(1)+"-"+alCoun.get(2));
			//return al;


		}

		public void getDetailsCapitalAPI(String capitalAPIURL)

		{
			Response response1=doGetRequest(capitalAPIURL);
			List<String> jsonResponse1 = response1.jsonPath().getList("$");
			capCurrCode=response1.jsonPath().getString("currencies[0].code[0]");
			int statusCodeCapital=response1.getStatusCode();
			alCap.add(statusCodeCapital);
			alCap.add(capCurrCode);
			
			//System.out.println(statusCodeCapital+"-"+capCurrCode );


		}
		
	
		@Test(priority=1)
		public void APITestPositive()
		{

			getCapitalDetailsCountryAPI("https://restcountries.eu/rest/v2/all?fields=name;capital;currencies;latlng");

			getDetailsCapitalAPI("https://restcountries.eu/rest/v2/capital/"+alCoun.get(2)+"?fields=name;capital;currencies;latlng;regionalBlocs");

			//Status code validation_Capital API
			
			
			Assert.assertEquals(alCap.get(0), 200);
			System.out.println("validating Status code of Capital API is Success-"+alCap.get(0));
						

			//Status code validation_Country API
			
			Assert.assertEquals(alCoun.get(0), 200);
			System.out.println("validating Status code of Country API is success-"+alCoun.get(0));

			//Currency code matching
			Assert.assertEquals(alCap.get(1), alCoun.get(1));
			System.out.println("Comparing capital currency code against country currency code-"+alCap.get(1));


			//System.out.println("first capital name:"+capitalName +" "+ "First currency code:"+counCurrCode);

		}
		
		@Test(priority=2)
		public void APITestNegative() {
			
			//validating any capital should not repeating twice
			
			String capitalURI="https://restcountries.eu/rest/v2/all?fields=name;capital;currencies;latlng";
			Response response2=doGetRequest(capitalURI);
			ResponseBody body=response2.getBody();
			String bodyAsString = body.asString();
			List<Object> al2=new ArrayList();
			List<Object> al3=new ArrayList();
			
			int capitalcount=response2.jsonPath().getList("capital").size();
			//System.out.println(capitalcount);
			
			for(int i=0;i<capitalcount;i++)
			{
				al2.add(response2.jsonPath().getString("capital["+i+"]"));
			}
			
			//get the number of times each capital repeated
			Map<Object, Integer> counts = new HashMap<Object, Integer>();
			for (Object str : al2) {
			    if (counts.containsKey(str)) {
			        counts.put(str, counts.get(str) + 1);
			    } else {
			        counts.put(str, 1);
			    }
			}

			
			// converting arraylist to HashSet removes the duplicates		
			Set<Object> set = new HashSet<>(al2);
			al3.addAll(set);
			al3.removeAll(Collections.singleton(null));
			
			System.out.println("number of capitals present in API response_without duplicate filtering- " + al2.size());
			System.out.println("number of capitals present in API response_After filtering duplicate- " + al3.size());
			if(al2.size()!=al3.size())
			{
				System.out.println("Duplicates are present");
			}
			Assert.assertEquals(al3.size(), al2.size());
			//Assert.assertEquals(actual, expected);
			
		}


	}
}
