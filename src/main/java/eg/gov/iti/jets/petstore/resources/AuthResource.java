package eg.gov.iti.jets.petstore.resources;

import eg.gov.iti.jets.petstore.dto.*;
import eg.gov.iti.jets.petstore.entities.User;
import eg.gov.iti.jets.petstore.exceptions.ResourceNotFoundException;
import eg.gov.iti.jets.petstore.exceptions.UserNotFoundException;
import eg.gov.iti.jets.petstore.exceptions.models.ErrorDetails;
import eg.gov.iti.jets.petstore.security.JwtUtil;
import eg.gov.iti.jets.petstore.security.model.CustomUserDetails;
import eg.gov.iti.jets.petstore.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/auth/")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
public class AuthResource {

    @Value("${google.id}")
    private String googleClientId;

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResource(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "SignUp User",
            description = "provide User information to SignUp"
    )
    @ApiResponse(responseCode = "201", description = "Successfully SignUp.")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @ApiResponse(responseCode = "400", description = "Bad request, you must provide all the fields", content = @Content)
    @PostMapping("signUp")
    public ResponseEntity<UserRegistrationDTO> signUp(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User Information ToSign Up", required = true) @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserRegistrationDTO userInformation = authService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userInformation);
    }


    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserLoginDTO userLoginDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail()
                            , userLoginDTO.getPassword(), Collections.emptyList()));
        } catch (BadCredentialsException exception) {
            throw new UserNotFoundException("Incorrect UserName or Password");
        }
        final UserDetails userDetails = authService.loadUserByUsername(userLoginDTO.getEmail());
        Long userId = ((User) userDetails).getId();
        String token = jwtUtil.generateToken(userDetails, userId);
        return ResponseEntity.ok(new AuthenticationResponse(token, userId));
    }

//    @PostMapping(value = "/refresh")
//    public ResponseEntity<AuthenticationResponse> refreshToken(Principal principal) {
//        UserDetails userDetails = authService.loadUserByUsername(principal.getName());
//        String token = jwtUtil.generateToken(userDetails);
//        return  ResponseEntity.ok(new AuthenticationResponse(token));
//    }


    /**
     * @param idToken it's a idToken not authToken from google response in angular
     * @return GoogleIdToken.Payload
     * @throws IOException
     */
    @PostMapping("google")
    public ResponseEntity<AuthenticationResponse> loginWithGoogle(@RequestBody TokenDTO idToken) throws IOException {
        NetHttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder builder = new GoogleIdTokenVerifier
                .Builder(httpTransport, jacksonFactory)
                .setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken = GoogleIdToken.parse(builder.getJsonFactory(),
                idToken.getToken());
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        String email = payload.getEmail();

        boolean exist = authService.isUserEmailExist(email);
        CustomUserDetails userDetails = null;
        if (exist) {
            userDetails = (CustomUserDetails) authService.loadUserByUsername(email);
        } else {
            userDetails = authService.addNewUser(email);
        }
        String token = jwtUtil.generateToken(userDetails, userDetails.getId());
        return ResponseEntity.ok(new AuthenticationResponse(token, userDetails.getId()));

    }


    /**
     * @param authToken from facebook response in angular
     * @return User
     * @throws IOException
     */
    @PostMapping("facebook")
    public ResponseEntity<AuthenticationResponse> loginWithFacebook(@RequestBody TokenDTO authToken) {

        FacebookTemplate facebookTemplate = new FacebookTemplate(authToken.getToken());
        String[] userInformation = {"email", "name", "picture"};
        org.springframework.social.facebook.api.User user = facebookTemplate.fetchObject("me", org.springframework.social.facebook.api.User.class, userInformation);

        String userFacebook = user.getEmail();
        boolean exist = authService.isUserEmailExist(userFacebook);
        CustomUserDetails userDetails = null;
        if (exist) {
            userDetails = (CustomUserDetails) authService.loadUserByUsername(userFacebook);
        } else {
            userDetails = authService.addNewUser(userFacebook);
        }
        String token = jwtUtil.generateToken(userDetails, userDetails.getId());
        return ResponseEntity.ok(new AuthenticationResponse(token, userDetails.getId()));
    }


    @PostMapping("/email")
    public ResponseEntity<?> checkEmailExistOrNot(@RequestBody UserEmailDTO emailDTO) {
        Boolean userEmailExist = authService.isUserEmailExist(emailDTO.getEmail());
        if (userEmailExist) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            throw new ResourceNotFoundException("This Email already Exist");
        }

    }
}
