package com.example.identity_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final String [] PUBLIC_ENDPOINT = {
             "/auth/token", "/auth/introspect", "/users"
    };
    private final String ADMIN = "ROLE_ADMIN";
    private final String USER = "ROLE_USER";
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
//        protect endpoint: register, login
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
//                .requestMatchers(HttpMethod.GET, "/users").hasAuthority(ADMIN)
                //or use hasRole(Role.ADMIN.name)
                        .anyRequest().authenticated()

        );
//        disbale seasurf
        httpSecurity.csrf(request -> request.disable());
// utilize oauth2 resource server => register a ProviderManager
        //cmt di chay lai thi van duoc
// Figure: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-architecture
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigure ->
//      Ta can dang ki mot Authentication Provider (cu the la JWT Authentication Provider)
//      voi Provider Manager => Tiep tuc config JWT Authentication Provider
//      overriding default JWTDecoder
//      3 jobs: decodes, verifies and validates JWT
                        jwtConfigure.decoder( jwtDecoder() )
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
//        JWTAuthenticationConverter
//        => convert JWT to Collection of Authorities
        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
//        request s secret key => (KEY used to generate token)
//        Use MAC algorithm to sign verify this key
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(),"HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder;
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
//      customize SCOPE_ADMIN to Role or sth looks familiar
//        AuthenticationConverter => Granted converter + PrincipalClaimName (issuer)
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
