package com.freenow.userblog.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.freenow.helpers.CommonUtils;
import com.freenow.testdata.UserBlogData;
import com.freenow.userblog.Helpers;
import io.restassured.response.Response;

public class ApiTest {

	public class UserBlogTest extends CommonUtils {
		ExtentReports extent;
		ExtentTest test;
		Helpers helper = new Helpers();

		@BeforeClass
		public void startReports() {

			String reportName = this.getClass().getSimpleName();
			extent = startReport(reportName);
			test = extent.createTest(reportName, "Test Summary");
		}

		@Test(enabled = true, description = "Verify if the given user exists by passing query param", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyUserExistsWithQueryParam(String username) {
			String resString = "";
			Response userDetails = helper.getUserByUsername(username);
			if (userDetails.jsonPath().getList("$").isEmpty()) {
				test.log(Status.FAIL, "User with username " + username + " not found : "
						+ Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			resString = userDetails.jsonPath().getString("username[0]");
			Assert.assertEquals(resString, username);
			test.log(Status.PASS, "User with username " + username + " exists");
		}

		@Test(enabled = true, description = "Verify if the given user exists by parsing the response", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyUserExists(String username) {
			Object userId;
			String responseUserName = "";
			Response allUsers = helper.getUserByUsername();
			List<Map<String, Object>> users = allUsers.jsonPath().getList("$");

			for (Map<String, Object> userDetails : users) {
				if (userDetails.get("username").toString().equalsIgnoreCase(username)) {
					userId = userDetails.get("id");
					responseUserName = userDetails.get("username").toString();
					break;
				}
			}
			if (responseUserName.equals("")) {

				test.log(Status.FAIL, "User with username " + username + " not found : "
						+ Thread.currentThread().getStackTrace()[1].getMethodName());

			}
			Assert.assertEquals(responseUserName, username);
			test.log(Status.PASS, "User with username " + username + " exists");

		}

		@Test(enabled = true, description = "Verify if the email format of the comments is valid", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyEmailformat(String username) {
			Response userDetails = helper.getUserByUsername(username);
			int userId = userDetails.jsonPath().getInt("id[0]");
			Response userPosts = helper.getPostsByUser(userId);
			List<Map<String, Integer>> posts = userPosts.jsonPath().getList("$");

			for (Map<String, Integer> postDetails : posts) {
				Assert.assertEquals(postDetails.get("userId").intValue(), userId);
			}
			List<Integer> postId = new ArrayList<Integer>();
			for (Map<String, Integer> postByUser : posts) {
				postId.add(postByUser.get("id"));
			}

			List<Map<String, Object>> comments = helper.getEmailsByPostId(postId);
			SoftAssert softAssertion = new SoftAssert();
			for (Map<String, Object> eachComment : comments) {

				boolean isEmailValid = helper.validateEmail(eachComment.get("email").toString());

				softAssertion.assertTrue(isEmailValid, "");
				if (isEmailValid)
					test.log(Status.PASS, "Email address format: " + eachComment.get("email").toString() + " is valid");
				else {
					String message = "Email address " + eachComment.get("email").toString() + " for the comment id "
							+ eachComment.get("comment_id") + " posted for post id " + eachComment.get("post_id")
							+ " is invalid: ";
					test.log(Status.FAIL, message + Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
			softAssertion.assertAll();
		}

		@AfterClass
		public void tearDown() {
			extent.flush();
		}
	}

}
