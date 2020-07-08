import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class BackItUp {
    private S3Client s3Client;
    private final static String bucketName = "backitup-adamtrev";

    public void execute() {
        // get files to backup with full structure
        final List<String> filePaths = ImmutableList.of(
                "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Kerbal Space Program\\saves\\default\\Full save.sfs"
        );

        // compress them into a zip
        final Optional<File> zipFileOptional = createZipFile(filePaths);

        if (!zipFileOptional.isPresent()) {
            System.out.println("Unable to create zip file. \nexiting...");
            return;
        }

        // upload zip to s3
        final PutObjectRequest putObjectRequest = PutObjectRequest
                .builder()
                .key(zipFileOptional.get().getName())
                .bucket(bucketName)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(zipFileOptional.get()));

        // verify zip is there
        // specify lifecycle of zips
        // delete temp zip

    }

    private Optional<File> createZipFile(final List<String> files) {
        try {
            final File file = new File(files.get(0));
            final String zipFileName = generateFileName();
            final FileOutputStream fos = new FileOutputStream(zipFileName);
            final ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(3)));

            final byte[] bytes = Files.readAllBytes(Paths.get(files.get(0)));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();
            fos.close();;

            return Optional.of(new File(zipFileName));
        } catch (final FileNotFoundException ex) {
            System.out.println(String.format("The file %s does not exist", files.get(0)));
        } catch (final IOException ex) {
            System.out.println("I/O error: " + ex);
        }

        return Optional.empty();
    }

    private String generateFileName() {
        return "Compressed.zip";
    }
}
