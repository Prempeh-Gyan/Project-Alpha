package org.isag_ghana.alpha.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebMvc
@EnableSpringDataWebSupport
@Configuration
@ComponentScan({ "org.isag_ghana.alpha" })
@PropertySource(value = { "classpath:application.properties" })
public class SpringAppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment environment;

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		log.info("SpringAppConfig triggered...");
		registry.addResourceHandler("/viewResources/**").addResourceLocations("/viewResources/");
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

		builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(builder.build());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		jsonConverter.setObjectMapper(objectMapper);

		converters.add(jsonConverter);
		converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));

		ByteArrayHttpMessageConverter byteConverter = new ByteArrayHttpMessageConverter();
		converters.add(byteConverter);
		super.configureMessageConverters(converters);
	}

	// with token verification implementation

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		return sessionLocaleResolver;
	}

	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(environment.getRequiredProperty("spring.mail.host"));
		javaMailSender.setPort(Integer.parseInt(environment.getRequiredProperty("spring.mail.port")));
		javaMailSender.setUsername(environment.getRequiredProperty("spring.mail.username"));
		javaMailSender.setPassword(environment.getRequiredProperty("spring.mail.password"));
		javaMailSender.setJavaMailProperties(javaMailProperties());
		return javaMailSender;
	}

	private Properties javaMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", environment.getRequiredProperty("spring.mail.properties.mail.smtps.auth"));
		properties.put("mail.smtp.starttls.enable", environment.getRequiredProperty("spring.mail.properties.mail.smtps.starttls.enable"));
		return properties;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(0);
		return messageSource;
	}
}
