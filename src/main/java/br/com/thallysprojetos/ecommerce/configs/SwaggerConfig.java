package br.com.thallysprojetos.ecommerce.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
//        final String securitySchemeName = "bearerAuth";
        Contact contato = new Contact()
                .name("Thallys Cézar")
                .url("https://github.com/thallysCezar");

        return new OpenAPI().info(
                new Info()
                        .title("Projeto Ecommerce Monolito")
                        .version("1.0")
                        .description("Este projeto é uma solução de e-commerce completa," +
                                " desenvolvida com uma arquitetura monolítica. A plataforma foi concebida para integrar " +
                                "todas as funcionalidades essenciais, como catálogo de produtos, carrinho de compras, " +
                                "gestão de usuários e processamento de pedidos, em uma única e coesa base de código.")
                        .contact(contato)
        );
    }

}