package com.example.summerproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {
    SUCCESS("response.success"),
    FAIL("response.failed"),
    NOT_FOUND("not.found"),
    REQUIRED("required"),
    OUT_OF_STOCK("out.of.stock"),
    MULTIPLE_RENT("multiple.rent.not.allowed"),
    METHOD_INVALID("method.invalid"),
    NOT_FOUND_EXCEPTION("exception.not.found"),
    EXCEPTION("exception.thrown"),
    DOWNLOADED("download.success"),
    DELETED("deleted"),
    EXPORT_EXCEL_SUCCESS("data.exported"),
    SAVE("saved.success"),
    UPDATE("update.success"),
    INVALID_CREDENTIALS("invalid.credentials"),
    AUTHENTICATION_ERROR("authentication.error"),
    MAIL_SENT("mail.sent"),
    RETURN_MAIL_SENT("remainder.mail.sent"),
    EXPIRED("jtw.expired")
    ;
    final String code;
}
