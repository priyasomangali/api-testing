package com.freenow.helpers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.Map;

// This class has all the REST API method definitions

public class ApiHelper {

	public Response getRequest(String endpoint) {
		return given().headers("Content-Type", ContentType.JSON).when().get(endpoint).then()
				.contentType(ContentType.JSON).extract().response();
	}

	public Response getRequest(String endpoint, Map<String, Object> queryParam) {
		return given().headers("Content-Type", ContentType.JSON).queryParams(queryParam).when().get(endpoint).then()
				.contentType(ContentType.JSON).extract().response();
	}

	public Response postRequest(String endpoint, String requestBody) {
		return given().contentType(ContentType.JSON).body(requestBody).post(endpoint);
	}

	public Response deleteRequest(String endpoint) {
		return given().contentType(ContentType.JSON).delete(endpoint);
	}

	public Response putRequest(String endpoint, String requestBody) {
		return given().contentType(ContentType.JSON).body(requestBody).put(endpoint);
	}

	public Response patchRequest(String endpoint, String requestBody) {
		return given().contentType(ContentType.JSON).body(requestBody).patch(endpoint);
	}
}