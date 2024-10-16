package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.exception.NotFoundException;
import com.example.identity_service.repository.UserRepository;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.common.hash.Hashing;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    // Not constructor inject
    @NonFinal
    protected static final String SIGNER_KEY = "vnCY1rsDKUZU+AZ7Rx9eR6Erno0dw8ittihP9vmhAa28VD0e6c3JCxPHQUaL1dgy";

    String header = "{ \" type \" : \" JWT\", \" alg\" : \" HS256\" }";
    String payload = "{ \" username\" : \" %s\", \" password\" : \" %s\" }";
    String salt = "toantom123";
    String jwtTemplate = "{ %s, %s, %s }";

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        String encodingText = "";

        // find username
        IdenUser user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_EXIST));
        //generate a JWT from username and password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean authenticated =  encoder.matches(request.getPassword(), user.getPassword());
        if( !authenticated ) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(request.getUsername());

        return AuthenticationResponse
                .builder()
                .authenticated( true )
                .token( token )
                .build();
    }
    private String generateToken (String username) {

        //Define hash algorithm
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        // De build duoc payload (data trong body goi la claim => body : claim set)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .issuer("identity.com")
                .expirationTime(new Date(Instant.now().plusSeconds(3600).toEpochMilli()))
                .claim("role", "user") //get from database OR defined based on API
                .build();

        Payload jwsPayload = new Payload( jwtClaimsSet.toJSONObject() );

        //token
        JWSObject jwsObject = new JWSObject( jwsHeader, jwsPayload );
        // Using MACSigner to sign header and payload => Signature (3rd part of signature

        // Using MACSigner to sign a symmetric key
        // MACSigner is an implementation of HS512
        // input for it: 32 bytes
        try {
            jwsObject.sign( new MACSigner(SIGNER_KEY.getBytes()) );
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println("Cannot create token!");
            throw new RuntimeException(e);
        }
    }

}
