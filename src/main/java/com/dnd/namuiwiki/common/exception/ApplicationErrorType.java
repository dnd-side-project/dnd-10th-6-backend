package com.dnd.namuiwiki.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
public enum ApplicationErrorType {
    /**
     * Common Error Type
     */
    INVALID_DATA_ARGUMENT(HttpStatus.BAD_REQUEST, "Invalid data argument"),
    FROM_JSON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json parsing error"),
    TO_JSON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json parsing error"),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다"),

    /**
     * Auth Error Type
     */
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid access token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    NOT_FOUND_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 없습니다."),
    NOT_FOUND_AUTHORITY_DATA(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "인가에 실패하였습니다."),
    TOKEN_INTERNAL_ERROR(HttpStatus.UNAUTHORIZED, "토큰 verify 실패 (토큰 내부 값 오류)"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");


    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}
