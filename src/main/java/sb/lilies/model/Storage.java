package sb.lilies.model;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "storage")
public final class Storage {

    @Id
    @Column(name = "storage_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storage_id_gen")
    @SequenceGenerator(name = "storage_id_gen", sequenceName = "storage_id_seq", allocationSize = 1)
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
