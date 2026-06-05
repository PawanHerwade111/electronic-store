package com.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	final String securitySchemeName = "bearerAuth";

	@Bean
	public OpenAPI customOpenAPI() {
		OpenAPI openAPI = new OpenAPI();
		openAPI.setInfo(getInfo());
		openAPI.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
						.name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
		return openAPI;

	}

	private Info getInfo() {
		Info info = new Info();
		info.setTitle("Electronic Store API's");
		info.setDescription("This is backend project created by Pawan");
		info.setVersion("1.0.0V");
		info.setContact(new Contact().name("Pawan").email("pawanherwade111@gmail.com").url("https://github.com/PawanHerwade111"));
		return info;
	}
}
