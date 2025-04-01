package com.lsh.scheduler_dev.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsh.scheduler_dev.common.filter.SessionExceptionFilter;
import com.lsh.scheduler_dev.common.filter.SignInFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<SignInFilter> signInFilter() {
		FilterRegistrationBean<SignInFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new SignInFilter());
		filterRegistrationBean.setOrder(2);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<SessionExceptionFilter> sessionExceptionFilter() {
		FilterRegistrationBean<SessionExceptionFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new SessionExceptionFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}

}
