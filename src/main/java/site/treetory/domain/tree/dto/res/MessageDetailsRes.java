package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.global.util.DateFormatter;

@Getter
@Builder
public class MessageDetailsRes {

    private String font;

    private String writer;

    private String message;

    private String createdDate;

    public static MessageDetailsRes toDto(PlacedOrnament placedOrnament) {
        return MessageDetailsRes.builder()
                .font(placedOrnament.getFont().name())
                .writer(placedOrnament.getWriterNickname())
                .message(placedOrnament.getMessage())
                .createdDate(DateFormatter.convertToDate(placedOrnament.getCreatedAt()))
                .build();
    }
}
