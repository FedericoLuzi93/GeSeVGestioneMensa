package it.gesev.mensa.confg;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GeneralConfig implements WebMvcConfigurer 
{
	public MultipartResolver multipartResolver() 
	{
		return new CommonsMultipartResolver();
	}
}
