package africa.semicolon.regcrow.services.cloud;


import africa.semicolon.regcrow.exceptions.ImageUploadFailedException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static africa.semicolon.regcrow.utils.AppUtils.CLOUDINARY_IMAGE_URL;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryCloudService implements CloudService{
   private final Cloudinary cloudinary;

    @Override
    public String upload(byte[] image) throws ImageUploadFailedException{
        try {
            Map<?,?> uploadResult = cloudinary.uploader().upload(image, ObjectUtils.emptyMap());
            String imageUrl =  (String) uploadResult.get(CLOUDINARY_IMAGE_URL);
            return imageUrl;
        } catch (IOException e) {
            throw new ImageUploadFailedException(e.getMessage());
        }
    }
}
