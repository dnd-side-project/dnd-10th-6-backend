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
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
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
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    /**
     * User Error Type
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    EXISTING_USER(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),

    /**
     * Wiki Error Type
     */
    NOT_FOUND_WIKI(HttpStatus.NOT_FOUND, "존재하지 않는 위키입니다."),
    INVALID_WIKI_TYPE(HttpStatus.CONFLICT, "잘못된 위키 타입입니다."),

    /**
     * Question Error Type
     */
    INVALID_QUESTION_ID(HttpStatus.NOT_FOUND, "문항을 찾을 수 없습니다."),

    /**
     * Option Error Type
     */
    INVALID_OPTION_ID(HttpStatus.NOT_FOUND, "옵션을 찾을 수 없습니다."),

    /**
     * Answer Error Type
     */
    NOT_ALLOWED_ANSWER_TYPE(HttpStatus.CONFLICT, "해당 문항에 허용되지 않은 답변 타입입니다."),
    CONFLICT_OPTION_QUESTION(HttpStatus.CONFLICT, "문항에 해당 옵션이 없습니다."),
    NOT_INTEGER_ANSWER(HttpStatus.CONFLICT, "정수형 답변이 아닙니다."),
    NOT_STRING_ANSWER(HttpStatus.NOT_FOUND, "문자형 답변이 아닙니다."),
    CANNOT_SEND_SURVEY_TO_MYSELF(HttpStatus.BAD_REQUEST, "자신에게 설문을 보낼 수 없습니다."),
    ANSWER_REASON_REQUIRED(HttpStatus.CONFLICT, "reason 필드가 필요한 질문입니다."),
    INVALID_ANSWER_TYPE(HttpStatus.BAD_REQUEST, "답변 타입이 다릅니다."),

    /**
     * Survey Error Type
     */
    NOT_FOUND_SURVEY(HttpStatus.NOT_FOUND, "존재하지 않는 설문입니다."),
    QUESTION_ANSWER_COUNT_NOT_EQUAL(HttpStatus.INTERNAL_SERVER_ERROR, "문항과 답변의 개수가 일치하지 않습니다."),
    INVALID_BORROWING_LIMIT(HttpStatus.BAD_REQUEST, "빌릴 수 있는 돈은 0 이상 10억 이하입니다."),
    INVALID_SURVEY_OWNER(HttpStatus.CONFLICT, "설문 소유자가 아닙니다."),

    /**
     * Dashboard Error Type
     */
    INVALID_DASHBOARD_TYPE(HttpStatus.CONFLICT, "잘못된 대시보드 타입입니다."),

    /**
     * Filter Error Type
     */
    INVALID_FILTER(HttpStatus.BAD_REQUEST, "동시에 두가지 필터를 사용할 수 없습니다."),
    INVALID_QUESTION_WIKI_TYPE(HttpStatus.BAD_REQUEST, "질문의 위키 타입이 다릅니다."),
    ;


    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}
