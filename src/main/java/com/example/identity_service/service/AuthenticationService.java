package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.entity.IdenUser;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.exception.NotFoundException;
import com.example.identity_service.exception.UnauthenticatedException;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    // Not constructor inject
    @NonFinal
    protected String token;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        String encodingText = "";

        // find username
        IdenUser user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_EXIST));
        //generate a JWT from username and password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean authenticated =  encoder.matches(request.getPassword(), user.getPassword());
        if( !authenticated ) {
            throw new UnauthenticatedException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user);

        return AuthenticationResponse
                .builder()
                .authenticated( true )
                .token( token )
                .build();
    }

    /**
     *
     * @param user
     * @alg JWSObject => object contains JWSHeader, Payload
     * JWSHeader stores hash algorithm information
     * Payload => JWTClaimsSet
     * object sign using hash algorithm stored in JWSHeader
     * Serialize object => string
     * @return token
     */
    private String generateToken (IdenUser user) {
        //Define hash algorithm => this is ver 1.0 => gen key from username
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        // De build duoc payload (data trong body goi la claim => body : claim set)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .issuer("identity.com") //issuer: domain
                .claim("scope", generateScope(user))
                .expirationTime(new Date(
                        Instant.now().plus(2, ChronoUnit.MINUTES).toEpochMilli()
                ))
               //Other: get from database OR defined based on API
                .build();
        System.out.println("send: " +  jwtClaimsSet.getIssueTime()+" to "+ jwtClaimsSet.getExpirationTime());
        Payload payload = new Payload( jwtClaimsSet.toJSONObject() );

        //token
        JWSObject jwsObject = new JWSObject( jwsHeader, payload );
        // Using MACSigner to sign header and payload => Signature (3rd part of signature

        // Using MACSigner to sign a symmetric key
        // MACSigner is an implementation of HS512 ???
        // input for it: 32 bytes
        try {
            jwsObject.sign( new MACSigner(SIGNER_KEY.getBytes()) );
            this.token = jwsObject.serialize();
            return token;
        } catch (JOSEException e) {
            System.out.println("Cannot create token!");
            throw new RuntimeException(e);
        }
    }
    private String generateScope(IdenUser user){
        Set<String> roles = user.getRoles();
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (! CollectionUtils.isEmpty( roles )){
            roles.forEach(role -> stringJoiner.add(role));
        }
        return stringJoiner.toString();
    }
    public IntrospectResponse introspectToken (IntrospectRequest request) throws JOSEException, ParseException {
        String userToken = request.getToken();
        System.out.println(userToken);
        boolean verified;
        // create a JWSVerifier => for checking this token was generated by what (key) and has not edited yet by anyone (
        // => prepare a symmetric key to verify that digital signature whether it initially was generated by that symmetric key (only the owner know)
       JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

       // convert token String => token object typed SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(userToken);

        // check the expiration time
        // get it in claimsSet
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        System.out.println("receive" +  issueTime+" to "+expiryTime);
/**
 * WHY my EXpiration time getting from JWTClaimsSet is not similar to the original
 */
        // after expiration time => it does not valid
        verified = signedJWT.verify(verifier) && expiryTime.after(new Date());

        return IntrospectResponse
               .builder()
               .valid(verified)
               .build();
    }
}
