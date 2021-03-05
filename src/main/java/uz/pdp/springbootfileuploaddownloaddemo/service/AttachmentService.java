package uz.pdp.springbootfileuploaddownloaddemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.springbootfileuploaddownloaddemo.entity.Attachment;
import uz.pdp.springbootfileuploaddownloaddemo.entity.AttachmentContent;
import uz.pdp.springbootfileuploaddownloaddemo.payload.ApiResponse;
import uz.pdp.springbootfileuploaddownloaddemo.repository.AttachmentContentRepository;
import uz.pdp.springbootfileuploaddownloaddemo.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentContentRepository attachmentContentRepository;

    public ApiResponse dbSave(MultipartHttpServletRequest request, String email) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            List<UUID> savedAttachmentIds = new ArrayList<>();
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                Attachment attachment = new Attachment(
                        file.getOriginalFilename(),
                        file.getContentType() != null ? file.getContentType() : "unknown",
                        (int) file.getSize()
                );
                attachment.setServer(false);
                attachment.setEmail(email);
                Attachment savedAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent(
                        file.getBytes(),
                        savedAttachment
                );
                attachmentContentRepository.save(attachmentContent);
                savedAttachmentIds.add(savedAttachment.getId());
            }
            return new ApiResponse("Saved", true, savedAttachmentIds);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    public ApiResponse serverSave(MultipartHttpServletRequest request, String email) {


        try {
            Iterator<String> fileNames = request.getFileNames();
            List<UUID> savedAttachmentIds = new ArrayList<>();
            while (fileNames.hasNext()) {
                MultipartFile multipartFile = request.getFile(fileNames.next());


                Attachment attachment = new Attachment();
                System.out.println(multipartFile);
                System.out.println(multipartFile.getName());
                System.out.println(multipartFile.getOriginalFilename());
                attachment.setName(multipartFile.getOriginalFilename());
                attachment.setServer(true);
                attachment.setEmail(email);
                attachment.setContentType(multipartFile.getContentType());
                attachment.setSize((int) multipartFile.getSize());
                Date now = new Date();
                File uploadFolder = new File(
                        String.format(
//                this.uploadFolder+
                                (1900 + now.getYear()) +
                                        "/"
                                        + (1 + now.getMonth()) +
                                        "/"
                                        + now.getDate()
                        )
                );

                if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
                    System.out.println("Papkalar yaratildi!");
                }
                attachment.setPath(
                        String.format(
                                (1900 + now.getYear()) +
                                        "/"
                                        + (1 + now.getMonth()) +
                                        "/"
                                        + now.getDate() +
                                        "/"
                                        + attachment.getName()
                        )
                );
                attachmentRepository.save(attachment);
                uploadFolder = uploadFolder.getAbsoluteFile();
                File file = new File(uploadFolder, String.format(attachment.getName()));

                multipartFile.transferTo(file);


            }
            return new ApiResponse("Saved", true, savedAttachmentIds);


        } catch (Exception e) {
            return new ApiResponse("Saved", true);
        }
    }



    public ResponseEntity getFromDBOrServer(UUID id) throws MalformedURLException {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("getAttachment"));

        if (attachment.isServer()){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename\""+ attachment.getName())
                    .contentType(MediaType.parseMediaType(attachment.getContentType()))
                    .contentLength(attachment.getSize())
                    .body(new FileUrlResource(String.format(attachment.getPath())));
        }
        else {
            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
            return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; fileName=\"" + attachment.getName() + "\"")
                    .body(attachmentContent.getBytes());
        }

    }

    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }
}
