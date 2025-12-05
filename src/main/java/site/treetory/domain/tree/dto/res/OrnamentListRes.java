package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.global.dto.PageDto;

@Getter
@Builder
public class OrnamentListRes {

    private PageDto<OrnamentDto> ornaments;

    public static OrnamentListRes toDto(Page<Ornament> ornaments) {

        return OrnamentListRes.builder()
                .ornaments(new PageDto<>(ornaments.map(OrnamentDto::toDto)))
                .build();
    }

    @Getter
    @Builder
    public static class OrnamentDto {

        private Long ornamentId;

        private String name;

        private String imgUrl;

        public static OrnamentDto toDto(Ornament ornament) {
            return OrnamentDto.builder()
                    .ornamentId(ornament.getId())
                    .name(ornament.getName())
                    .imgUrl(ornament.getImgUrl())
                    .build();
        }
    }
}
