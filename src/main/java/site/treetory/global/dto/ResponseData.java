package site.treetory.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData<T> {

    private ResponseHeader header;
    private T body;
}
