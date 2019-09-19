package com.freenow.userblog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.freenow.helpers.ApiHelper;
import com.freenow.userblog.BlogEndpoints;
import io.restassured.response.Response;

public class BlogHelpers extends ApiHelper {

	BlogEndpoints endpoint = new BlogEndpoints();

	public Response getUserByUsername(String username) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("username", username);
		Response response = getRequest(endpoint.generateUrl(BlogEndpoints.users), queryParam);
		return response;

	}

	public Response getUserByUsername() {

		Response response = getRequest(endpoint.generateUrl(BlogEndpoints.users));
		return response;

	}

	public Response getPostsByUser(int userId) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("userId", userId);
		Response response = getRequest(endpoint.generateUrl(BlogEndpoints.postsWithUserId), queryParam);
		return response;
	}

	public List<Map<String, Object>> getEmailsByPostId(List<Integer> postId) {
		Response response = null;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		List<Map<String, Object>> emailsByCommentList = new ArrayList<Map<String, Object>>();

		for (Integer id : postId) {
			queryParam.put("postId", id);
			response = getRequest(endpoint.generateUrl(BlogEndpoints.commentsWithPostId), queryParam);
			List<Map<String, Integer>> comment_id = response.jsonPath().getList("$");

			for (Map<String, Integer> comments : comment_id) {
				Map<String, Object> details = new HashMap<String, Object>();
				details.put("comment_id", comments.get("id"));
				details.put("post_id", id);
				details.put("email", comments.get("email"));

				emailsByCommentList.add(details);
			}

		}
		return emailsByCommentList;

	}

	public boolean validateEmail(String email) {

		boolean isEmailValid = false;
		if (email == null || email.length() > 254 || email.length() < 5)
			return false;
		Pattern p = Pattern.compile("\\A([A-Za-z0-9\\.!#\\$%&'\\*\\+\\-/=" + "\\?\\^_`\\{\\|\\}~]{1,64})@"
				+ "([A-Za-z0-9-]{1,63}\\.){1,}[A-Za-z0-9-]{1,63}\\z");
		Matcher m = p.matcher(email);
		boolean format1 = m.find();
		if (format1 == true)
			isEmailValid = true;
		Pattern q = Pattern.compile(
				"@((\\.)|-|" + "([A-Za-z0-9-]{1,}-\\.[A-Za-z0-9-]{1,}))" + "|(\\.\\z)|(\\A\\.)|(\\.\\.)|(\\.-)|(-\\z)");
		Matcher n = q.matcher(email);
		boolean format2 = n.find();
		if (format2 == true)
			isEmailValid = false;

		return isEmailValid;
	}
}
