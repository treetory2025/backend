package site.treetory.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.treetory.global.statuscode.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    ErrorCode errorCode;
}
