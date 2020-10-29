package RestAssured;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

public class TC002_JsonSchemaValidation {
	
	@Test
	public void jsonSchemaValidation() throws FileNotFoundException {
	        
	   //schema data file
		
	    File schemaFile=new File("C:\\eclipse-workspace\\IQVIAPracticalAssessment\\target\\schema.json");
	    JSONTokener schemaData= new JSONTokener(new FileInputStream(schemaFile));
	    JSONObject jsonSchema=new JSONObject(schemaData);
	    
	    // json data file
	    File jsonData=new File("C:\\eclipse-workspace\\IQVIAPracticalAssessment\\target\\jsondata.json");
	    JSONTokener jsonDataFile= new JSONTokener(new FileInputStream(jsonData));
	    JSONObject jsonobject=new JSONObject(jsonDataFile);
	    
	    //validation Schema
	    Schema schemaValidator=SchemaLoader.load(jsonSchema);
	    schemaValidator.validate(jsonobject );
	    
	    System.out.println(jsonobject.getString("code"));
	    System.out.println(jsonobject.getString("name"));
	    System.out.println(jsonobject.getString("symbol"));
	}
	    
	    
}
