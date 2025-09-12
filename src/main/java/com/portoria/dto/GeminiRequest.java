package com.portoria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GeminiRequest {
    private List<Content> contents;

    @Getter
    @Setter
    @ToString
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    @ToString
    public static class Part {
        private String text;
    }
}
