package uz.pdp.springbootfileuploaddownloaddemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springbootfileuploaddownloaddemo.entity.AttachmentContent;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, UUID> {
    AttachmentContent findByAttachmentId(UUID attachment_id);

}
