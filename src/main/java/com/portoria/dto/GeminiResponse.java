package com.portoria.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GeminiResponse {
    private List<Candidate> candidates;

    @Getter
    @Setter
    @ToString
    public static class Candidate {
        private Content content;
        private String finishReason;
    }

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
