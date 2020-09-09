package me.oktop.covid19service.web.dto.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    T data;
    String code;

    public CommonResponse(T data) {
        this.data = data;
        this.code = "200";
    }

    public static<T> CommonResponse success(T data) {
        return new CommonResponse<>(data);
    }
}
