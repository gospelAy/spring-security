package africa.semicolon.regcrow.config;

import africa.semicolon.regcrow.utils.JwtUtil;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static africa.semicolon.regcrow.utils.AppUtils.*;

@Configuration
public class BeanConfig {
    @Value(CLOUDINARY_API_KEY)
    private String apiKey;
    @Value(CLOUDINARY_API_SECRET)
    private String apiSecret;
    @Value(CLOUDINARY_CLOUD_NAME)
    private String cloudName;
   @Value(MAIL_API_KEY)
   private String mailApiKey;

   @Value(JWT_SIGNING_SECRET)
   private String jwt_secret;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME_VALUE, cloudName,
                CLOUD_API_KEY_VALUE, apiKey,
                API_SECRET_VALUE, apiSecret
        ));
    }

    @Bean
    public MailConfig mailConfig(){
        return new MailConfig(mailApiKey);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(jwt_secret);
    }
}
