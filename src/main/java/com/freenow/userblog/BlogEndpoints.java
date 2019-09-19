package com.freenow.userblog;

public class BlogEndpoints {
	private static String baseUri = "https://jsonplaceholder.typicode.com";
	public static String users = "/users";
	public static String posts = "/posts";
	public static String comments = "/comments";
	public static String commentsWithPostId = "/comments?postId=";
	public static String postsWithUserId = "/posts?userId=";

	public String generateUrl(String api) {

		String url = baseUri + api;
		return url;
	}
}
