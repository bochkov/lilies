package sb.lilies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import sb.lilies.cfg.StaticConfig;

@Data
@NoArgsConstructor
public final class StorageLink {

    private Long id;
    private String src;
    private String pdf;
    private String mp3;

    @JsonIgnore
    private boolean hasSrc;
    @JsonIgnore
    private boolean hasPdf;
    @JsonIgnore
    private boolean hasMp3;

    public StorageLink(StorageView sv) {
        this.id = sv.getId();
        this.hasSrc = sv.hasSrc();
        this.src = StaticConfig.MEDIA_URL + sv.srcFn();
        this.hasPdf = sv.hasPdf();
        this.pdf = StaticConfig.MEDIA_URL + sv.pdfFn();
        this.hasMp3 = sv.hasMp3();
        this.mp3 = StaticConfig.MEDIA_URL + sv.mp3Fn();
    }

}
