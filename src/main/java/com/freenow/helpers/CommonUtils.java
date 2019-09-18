package com.freenow.helpers;

import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class CommonUtils {
	static ExtentHtmlReporter htmlReporter;
	static ExtentReports extent = new ExtentReports();;
	ExtentTest test;
	Properties properties;

	public static ExtentReports startReport(String reportName) {

		htmlReporter = new ExtentHtmlReporter("test-output/" + reportName + ".html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("OS", "Mac Sierra");
		extent.setSystemInfo("Host Name", "local");
		extent.setSystemInfo("Environment", "mac");
		extent.setSystemInfo("User Name", "user");

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("AutomationTesting.in Demo Report");
		htmlReporter.config().setReportName(reportName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		return extent;
	}

}
