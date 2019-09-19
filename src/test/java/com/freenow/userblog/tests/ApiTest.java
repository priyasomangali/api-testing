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
import com.freenow.userblog.BlogHelpers;
import io.restassured.response.Response;

public class ApiTest {

	public class UserBlogTest extends CommonUtils {
		ExtentReports extent;
		ExtentTest test;
		BlogHelpers helper = new BlogHelpers();

		@BeforeClass
		public void startReports() {

			String reportName = this.getClass().getSimpleName();
			extent = startReport(reportName);
			test = extent.createTest(reportName, "Test Summary");
		}

//		To use this test if the API supports query param, 
		@Test(enabled = true, description = "Verify if the given user exists by passing query param", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyUserExistsWithQueryParam(String username) {
			Response userDetails = helper.getUserByUsername(username);
			Assert.assertEquals(userDetails.jsonPath().getString("username[0]"), username);
			test.log(Status.PASS, "User with username " + username + " exists");
		}

//  	To use this test if the API does not have support for query param
		@Test(enabled = true, description = "Verify if the given user exists by parsing the response", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyUserExists(String username) {
			String responseUserName = "";
			Response allUsers = helper.getUserByUsername();
			List<Map<String, Object>> users = allUsers.jsonPath().getList("$");

			for (Map<String, Object> userDetails : users) {
				if (userDetails.get("username").toString().equalsIgnoreCase(username)) {
					responseUserName = userDetails.get("username").toString();
					break;
				}
			}
			Assert.assertEquals(responseUserName, username);
			test.log(Status.PASS, "User with username " + username + " exists");

		}

//		Test disabled since the expected results are dependent on the API response which currently returns results for "Samantha" - name given in TASK
//		This test case is specifically to test the response in case of an invalid / non-existing username
		@Test(enabled = false, description = "Verify if the given user does not exist", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyUserDoesNotExistWithQueryParam(String username) {
			Response userDetails = helper.getUserByUsername(username);
			Assert.assertTrue(userDetails.jsonPath().getList("$").isEmpty());

			test.log(Status.PASS, "User with username " + username + " not found : "
					+ Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		@Test(enabled = true, description = "Verify if the email format of the comments are valid", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyEmailformat(String username) {

			Response userDetails = helper.getUserByUsername(username);
			int userId = userDetails.jsonPath().getInt("id[0]");

			Response userPosts = helper.getPostsByUser(userId);
			List<Map<String, Integer>> posts = userPosts.jsonPath().getList("$");

//			Assert if only the posts added by the given user are fetched
			for (Map<String, Integer> postDetails : posts) {
				Assert.assertEquals(postDetails.get("userId").intValue(), userId);
			}
			List<Integer> postId = new ArrayList<Integer>();
			for (Map<String, Integer> postByUser : posts) {
				postId.add(postByUser.get("id"));
			}

			List<Map<String, Object>> comments = helper.getEmailsByPostId(postId);

//			Soft assertion has been used to ensure entire test executes to check all the email addresses.
			SoftAssert softAssertion = new SoftAssert();
			for (Map<String, Object> eachComment : comments) {

//				Calling the validateEmail method to validate each email format
				boolean isEmailValid = helper.validateEmail(eachComment.get("email").toString());

				softAssertion.assertTrue(isEmailValid, "Invalid email");
				if (isEmailValid)
					test.log(Status.PASS, "Email address format: " + eachComment.get("email").toString() + " is valid");
				else {
					String message = "Email address " + eachComment.get("email").toString() + " for the comment id "
							+ eachComment.get("comment_id") + " posted for post id " + eachComment.get("post_id")
							+ " is invalid: ";
					test.log(Status.FAIL, message + Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
//			Used to handle previously caught assertion failures as well to determine the status of test case.
			softAssertion.assertAll();
		}

//		Test disabled since the expected results are dependent on the API response which currently returns results for "Samantha" - name given in TASK
//		This test case is specifically to test the response in case of NO POSTS ADDED BY USER
		@Test(enabled = false, description = "Verify for the case where user has no posts", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyNoPostByuser(String username) {

			Response userDetails = helper.getUserByUsername(username);
			int userId = userDetails.jsonPath().getInt("id[0]");

			Response userPosts = helper.getPostsByUser(userId);
			List<Map<String, Integer>> posts = userPosts.jsonPath().getList("$");

			Assert.assertTrue(posts.isEmpty());
		}

//		Test disabled since the expected results are dependent on the API response which currently returns results for "Samantha" - name given in TASK
//		This test case is specifically to test the response in case where POSTS HAVE NO COMMENTS
		@Test(enabled = false, description = "Verify for the case where post has no comments", dataProvider = "SearchUser", dataProviderClass = UserBlogData.class)
		public void verifyNoCommentsOnPosts(String username) {

			Response userDetails = helper.getUserByUsername(username);
			int userId = userDetails.jsonPath().getInt("id[0]");

			Response userPosts = helper.getPostsByUser(userId);
			List<Map<String, Integer>> posts = userPosts.jsonPath().getList("$");

//			Assert if only the posts added by the given user are fetched
			for (Map<String, Integer> postDetails : posts) {
				Assert.assertEquals(postDetails.get("userId").intValue(), userId);
			}
			List<Integer> postId = new ArrayList<Integer>();
			for (Map<String, Integer> postByUser : posts) {
				postId.add(postByUser.get("id"));
			}

			List<Map<String, Object>> comments = helper.getEmailsByPostId(postId);
			Assert.assertTrue(comments.isEmpty());
		}

		@AfterClass
		public void tearDown() {
			extent.flush();
		}
	}

}
