import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class ApplicationModule extends AbstractModule {
    private static final Region REGION = Region.US_WEST_2;
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public S3Client amazonS3(final AwsCredentialsProvider awsCredentialsProvider) {
        return S3Client
                .builder()
                .region(REGION)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Provides
    @Singleton
    public AwsCredentialsProvider profileCredentialsProvider() {
        return ProfileCredentialsProvider.create();
    }
}
