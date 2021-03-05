package uz.pdp.springbootfileuploaddownloaddemo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttachmentContent {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private byte[] bytes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment attachment;

    public AttachmentContent(byte[] bytes, Attachment attachment) {
        this.bytes = bytes;
        this.attachment = attachment;
    }
}
