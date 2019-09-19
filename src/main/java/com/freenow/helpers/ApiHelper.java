package com.freenow.helpers;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

import java.util.Map;

// This class has all the REST API method definitions and ResponseSpecification

public class ApiHelper {

	public ResponseSpecification checkStatusCodeAndContentType(int statusCode) {
		ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(statusCode)
				.expectContentType(ContentType.JSON).build();
		return checkStatusCodeAndContentType;
	}

	public Response getRequest(String endpoint) {
		return given().headers("Content-Type", ContentType.JSON).when().get(endpoint).then().assertThat()
				.spec(checkStatusCodeAndContentType(200)).and().contentType(ContentType.JSON).extract().response();
	}

	public Response getRequest(String endpoint, Map<String, Object> queryParam) {
		return given().headers("Content-Type", ContentType.JSON).queryParams(queryParam).when().get(endpoint).then()
				.assertThat().spec(checkStatusCodeAndContentType(200)).and().contentType(ContentType.JSON).extract()
				.response();
	}

	// Below http methods are not used in the project currently. Hence commented.
	/*
	 * public Response postRequest(String endpoint, String requestBody) { return
	 * given().contentType(ContentType.JSON).body(requestBody).post(endpoint).then()
	 * .assertThat()
	 * .spec(checkStatusCodeAndContentType(201)).and().contentType(ContentType.JSON)
	 * .extract().response(); }
	 * 
	 * public Response patchRequest(String endpoint, String requestBody) { return
	 * given().contentType(ContentType.JSON).body(requestBody).patch(endpoint).then(
	 * ).assertThat()
	 * .spec(checkStatusCodeAndContentType(200)).and().contentType(ContentType.JSON)
	 * .extract().response(); }
	 * 
	 * public Response deleteRequest(String endpoint) { return
	 * given().contentType(ContentType.JSON).delete(endpoint).then().assertThat()
	 * .spec(checkStatusCodeAndContentType(204)).and().contentType(ContentType.JSON)
	 * .extract().response(); }
	 */

}