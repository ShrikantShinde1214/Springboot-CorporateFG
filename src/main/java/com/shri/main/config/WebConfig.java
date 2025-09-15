package com.shri.main.config;

import com.shri.main.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		logger.info("Registering AuthInterceptor for protected paths");

		registry.addInterceptor(authInterceptor).addPathPatterns("/list", "/dashboard", "/preview", "/download",
				"/send-multiple", "/send", "send-receipt/search", "/send-receipt/form", "/sendInternship", "/sendCourseCompletion",
				"/showStudents", "/showUpdateForm", "/addStudentForm", "/protected/**");

		logger.info("AuthInterceptor registered for paths: "
				+ "/list, /dashboard, /preview, /download, /send-multiple, /send, "
				+ "/search, /form, /sendInternship, /sendCourseCompletion, "
				+ "/showStudents, /showUpdateForm, /addStudentForm, /protected/**");
	}
}
