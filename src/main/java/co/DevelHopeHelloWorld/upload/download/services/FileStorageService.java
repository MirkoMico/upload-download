package co.DevelHopeHelloWorld.upload.download.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;


    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName= UUID.randomUUID().toString();
        String completeFileName=newFileName +"."+ extension;

        File finalFolder= new File(fileRepositoryFolder);
       // file.transferTo(finalDestination);



        if(!finalFolder.exists()) throw new IOException("Final folder does not exist");
        if(!finalFolder.isDirectory()) throw new IOException("Final folder is not a directory");

    File finalDestination = new File(finalFolder + File.separator + completeFileName);
        if(finalDestination.exists()) throw new IOException("Folder conflict");

        file.transferTo(finalDestination);

        return completeFileName;
}

    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(fileRepositoryFolder + File.separator + fileName);
        if(!fileFromRepository.exists())throw new IOException("File does not exist");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }



}
