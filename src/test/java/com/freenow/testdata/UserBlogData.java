package com.freenow.testdata;

import org.testng.annotations.DataProvider;

public class UserBlogData {

	@DataProvider(name = "SearchUser")
	public static Object[][] getDataFromDataprovider() {

		return new Object[][] { { "Samantha" } };
	}
}
