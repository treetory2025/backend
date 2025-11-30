package site.treetory.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400,"Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    INVALID_TOKEN(401, "Invalid Token"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ;

    private final int httpStatusCode;

    private final String message;

}

