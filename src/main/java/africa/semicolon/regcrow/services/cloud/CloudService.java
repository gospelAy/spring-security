package africa.semicolon.regcrow.services.cloud;

import africa.semicolon.regcrow.exceptions.ImageUploadFailedException;

public interface CloudService {
    String upload(byte[] image) throws ImageUploadFailedException;
}
