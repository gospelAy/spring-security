package africa.semicolon.regcrow.config;

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


}
