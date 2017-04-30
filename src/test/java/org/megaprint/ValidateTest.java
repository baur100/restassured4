package org.megaprint;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

/**
 * Created by Baurz on 4/16/2017.
 */
public class ValidateTest {

    Properties prop= new Properties();
    Response resp;

    @BeforeMethod
    public void init() throws FileNotFoundException {
        FileInputStream fis= new FileInputStream("data\\env.properties");
        try {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RestAssured.baseURI=prop.getProperty("HOST");
    }

    @Test
    public void test1(){
        resp=given().
                param("key",prop.getProperty("KEY")).
                param("radius","5000").
                param("location","-33.8670522,151.1957362").
        when().
                get("/maps/api/place/nearbysearch/json").
        then().assertThat().
                statusCode(200).
        extract().response();
    }
    @Test
    public void test2(){
        JsonPath jpath=new JsonPath(resp.asString());
        int count=jpath.get("results.size()");
        for(int i=0;i<count;i++){
            System.out.println(jpath.getString("results["+i+"].name"));
        }

    }
}
