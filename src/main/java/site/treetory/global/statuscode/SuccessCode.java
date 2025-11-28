package site.treetory.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204,"No Content"),
    ;

    private final int httpStatusCode;

    private final String message;
}

