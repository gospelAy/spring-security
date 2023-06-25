package africa.semicolon.regcrow.utils;

import africa.semicolon.regcrow.models.Customer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.time.Instant.now;

public class AppUtils {

    public static final int ONE = 1;
    public static final int FOUR = 4;

    public static final String ID = "id";

    public static final int ZERO = 0;

    public static final String CLOUDINARY_API_KEY = "${cloudinary.api.key}";
    public static final String CLOUDINARY_CLOUD_NAME="${cloudinary.cloud.name}";

    public static final String CLOUDINARY_API_SECRET="${cloudinary.api.secret}";

    public static final String CLOUDINARY_IMAGE_URL="secure_url";

    public static final String JSON_PATCH_CONSTANT="application/json-patch+json";

    public static  final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_LIMIT = 10;
    public static final String MAIL_API_KEY="${sendinblue.api.key}";

    public static final String CLOUD_NAME_VALUE = "cloud_name";
    public static final String CLOUD_API_KEY_VALUE = "api_key";

    public static final String API_SECRET_VALUE="api_secret";

    public static final String SENDER = "sender";

    public static final String JWT_SIGNING_SECRET="${jwt.signing.secret}";

    public static final String APP_NAME="regcrow inc.";

    public static final String EMPTY_SPACE_VALUE=" ";

    public static final String APP_EMAIL="noreply@regcrow.africa";


    public static final String EMAIL_URL="https://api.brevo.com/v3/smtp/email";

    public static final String ACTIVATION_LINK_VALUE="Activation Link";

    public static final String FRONTEND_BASE_URL = "https://www.regcrow.com";

    public static final String MAIL_TEMPLATE_LOCATION = "C:\\Users\\semicolon\\Documents\\java_workspace\\regcrow\\src\\main\\resources\\static\\activation.txt";

    public static final String API_KEY_VALUE = "api-key";

    public static final String TO = "to";

    public static final String SUBJECT="subject";

    public static  final String HTML_CONTENT_VALUE = "htmlContent";

    public static final String CLAIMS_VALUE = "ROLES";

    public static final String CLAIM_VALUE = "claim";

    public static final String FIELD_CANNOT_BE_EMPTY_VALUE="field must not be empty";

    public static final String ACCESS_TOKEN_VALUE = "access_token";

    public static Pageable buildPageRequest(int page, int items){
        if (page<=ZERO) page=DEFAULT_PAGE_NUMBER;
        if (items<=ZERO) items = DEFAULT_PAGE_LIMIT;
        page-=ONE;
        return PageRequest.of(page, items);
    }

    public static String generateToken(Customer customer, String secret){
        return JWT.create()
                .withIssuedAt(now())
                .withExpiresAt(now().plusSeconds(200L))
                .withClaim(ID, customer.getId())
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public static List<SimpleGrantedAuthority> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                .toList();
    }
}
