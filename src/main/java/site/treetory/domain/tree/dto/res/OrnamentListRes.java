package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.tree.entity.Ornament;

import java.util.List;

@Getter
@Builder
public class OrnamentListRes {

    private List<OrnamentDto> ornaments;

    public static OrnamentListRes toDto(List<Ornament> ornaments) {
        List<OrnamentDto> ornamentList = ornaments.stream()
                .map(OrnamentDto::toDto)
                .toList();

        return OrnamentListRes.builder()
                .ornaments(ornamentList)
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
