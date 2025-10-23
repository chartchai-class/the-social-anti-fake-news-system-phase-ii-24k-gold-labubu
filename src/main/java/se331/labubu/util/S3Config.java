package se331.labubu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    // Access key id will be read from the application.properties file
    // during the application initialization.
    @Value("${supabase.storage.accessKey}")
    private String accessKeyId;

    // Secret access key will be read from the application.properties file
    // during the application initialization.
    @Value("${supabase.storage.secretKey}")
    private String secretAccessKey;

    // Region will be read from the application.properties file during the
    // application initialization.
    @Value("${supabase.storage.region}")
    private String region;

    @Value("${supabase.storage.endpoint}")
    private String endpoint;

    @Bean
    public S3Client getAmazonS3Client() {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Client.builder()
                .credentialsProvider(() -> credentials)
                .region(software.amazon.awssdk.regions.Region.of(region))
                .endpointOverride(java.net.URI.create(endpoint))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(software.amazon.awssdk.regions.Region.of(region))
                .endpointOverride(java.net.URI.create(endpoint))
                .build();
    }
}
