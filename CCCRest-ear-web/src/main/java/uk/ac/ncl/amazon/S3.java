package uk.ac.ncl.amazon;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

public class S3 {
    private AmazonS3 s3;
    //	private final String bucketName = "rdrepository";
    private final String bucketName = "uk.ac.ncl.rdrepository";

    public S3() {
        s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
        Region euWest1 = Region.getRegion(Regions.US_EAST_1);
        s3.setRegion(euWest1);


    }

    public void upload(File file, String key) {
        s3.putObject(new PutObjectRequest(bucketName, key, file));
    }

    public void upload(InputStream is, String key, ObjectMetadata metadata) {

        s3.putObject(bucketName, key, is, metadata);
    }

    public S3Object download(String key) {
        return s3.getObject(new GetObjectRequest(bucketName, key));
    }

    public void delete(String key) {
        s3.deleteObject(bucketName, key);
    }

}
