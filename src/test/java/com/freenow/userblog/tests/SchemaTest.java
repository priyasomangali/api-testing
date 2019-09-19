package com.freenow.userblog.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.freenow.helpers.CommonUtils;
import com.freenow.userblog.BlogEndpoints;
import io.restassured.RestAssured;

public class SchemaTest extends CommonUtils {

	ExtentReports extent;
	ExtentTest test;
	BlogEndpoints endpoint = new BlogEndpoints();

	@BeforeClass
	public void startReports() {

		String reportName = this.getClass().getSimpleName();
		extent = startReport(reportName);
		test = extent.createTest(reportName, "Test Summary");
	}

	@Test
	public void testJsonUsersSchema() {

		RestAssured.given().when().get(endpoint.generateUrl(BlogEndpoints.users)).then().assertThat()
				.body(matchesJsonSchemaInClasspath("usersSchema.json/"));
		test.log(Status.PASS, "Users schema validation successful");

	}

	@Test
	public void testJsonPostsSchema() {

		RestAssured.given().when().get(endpoint.generateUrl(BlogEndpoints.postsWithUserId)).then().assertThat()
				.body(matchesJsonSchemaInClasspath("postsSchema.json/"));
		test.log(Status.PASS, "Posts schema validation successful");

	}

	@Test
	public void testJsonCommentsSchema() {

		RestAssured.given().when().get(endpoint.generateUrl(BlogEndpoints.commentsWithPostId)).then().assertThat()
				.body(matchesJsonSchemaInClasspath("commentsSchema.json/"));
		test.log(Status.PASS, "Comments schema validation successful");

	}

	@AfterClass
	public void tearDown() {
		extent.flush();
	}
}
