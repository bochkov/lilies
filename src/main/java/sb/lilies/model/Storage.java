package sb.lilies.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "storage")
public final class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "storage_id_seq")
    @Column(name = "storage_id")
    private Long id;

    private String filename;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] src;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] pdf;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] mp3;

}
