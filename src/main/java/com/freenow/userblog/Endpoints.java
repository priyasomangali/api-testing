package com.freenow.userblog;

public class Endpoints {
	private static String baseUrl = "https://jsonplaceholder.typicode.com";
	public static String users = "/users";
	public static String posts = "/posts";
	public static String comments = "/comments";
	public static String commentsWithPostId = "/comments?postId=";
	public static String postsWithUserId = "/posts?userId=";

	public String generateUrl(String api) {

		String url = baseUrl + api;
		return url;
	}
}
