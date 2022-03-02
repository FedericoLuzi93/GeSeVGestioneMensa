package it.gesev.mensa.confg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class CORSConfiguration implements WebMvcConfigurer
{
   @Value("${allowed.cors.domains}")
   private String domains;

   @Override
   public void addCorsMappings(CorsRegistry registry) 
   {
	   registry.addMapping("/**").allowedOrigins(domains.split(",")).allowedMethods("PUT", "GET", "PATCH", "UPDATE", "POST");
   }

}
	