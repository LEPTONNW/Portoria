package com.portoria.controller;

import com.portoria.dto.GeminiRequest;
import com.portoria.dto.GeminiResponse;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/wineai")
public class WineController {

    @Value("${gemini.api.key}")
    private String apiKey;

    //질문시작
    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody String answer) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // Google AI API URL
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
            
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // DTO 사용하여 요청 구성
            GeminiRequest request = new GeminiRequest();
            GeminiRequest.Content content = new GeminiRequest.Content();
            GeminiRequest.Part part = new GeminiRequest.Part();
            
            String prompt = "당신은 Portoria의 와인 전문가 AI입니다. " +
                    "다음 지침을 따라 답변해주세요:\n\n" +
                    "1. 다음 주제들에 대해서는 친절하고 전문적으로 답변해주세요:\n" +
                    "   - 와인 추천, 와인 종류, 와인 맛과 향\n" +
                    "   - 음식과 와인 페어링 (모든 음식 포함)\n" +
                    "   - 와인 보관법, 와인 서빙법\n" +
                    "   - 계절별 와인 추천 (봄, 여름, 가을, 겨울)\n" +
                    "   - 날씨와 와인의 관계 (습한 날씨, 더운 날씨, 추운 날씨, 비 오는 날 등)\n" +
                    "   - 모든 건강상태와 몸상태에 따른 와인 추천 (질병, 불편함, 개인적 상황 등 포함)\n" +
                    "   - 음식점, 레스토랑에서의 와인 선택\n" +
                    "   - 포르투갈 와인, 와인 투어\n" +
                    "   - 와인 관련 일반적인 질문\n" +
                    "   - 사용자가 어떤 상태나 상황을 말하더라도 와인과 연결지어 답변해주세요\n" +
                    "   - 건강상태나 개인적 상황을 언급해도 와인과 관련된 조언을 해주세요\n\n" +
                    "2. 오직 정치, 기술, 법률, 금융 투자 등 완전히 와인과 무관한 전문 분야에 대해서만 다음과 같이 답변해주세요:\n" +
                    "   '죄송합니다. 저는 와인 전문가로서 와인과 관련된 질문에만 답변할 수 있습니다. 와인 추천, 음식 페어링, 와인 보관법 등에 대해 질문해주시면 도움을 드릴 수 있습니다.'\n\n" +
                    "3. 답변은 친절하고 전문적인 톤으로 작성해주세요.\n\n" +
                    "사용자 질문: " + answer;
            
            part.setText(prompt);
            content.setParts(Arrays.asList(part));
            request.setContents(Arrays.asList(content));
            
            HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
            
            // DTO로 응답 받기
            ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(url, entity, GeminiResponse.class);
            
            // 응답 파싱
            GeminiResponse responseBody = response.getBody();
            
            // 디버깅을 위한 응답 구조 출력
            System.out.println("API Response: " + responseBody);
            
            if (responseBody != null && responseBody.getCandidates() != null && !responseBody.getCandidates().isEmpty()) {
                GeminiResponse.Candidate candidate = responseBody.getCandidates().get(0);
                if (candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                    String result = candidate.getContent().getParts().get(0).getText();
                    return ResponseEntity.ok().body(result);
                }
            }
            
            // 응답 구조가 예상과 다를 경우 전체 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("AI 응답을 파싱할 수 없습니다. 응답: " + responseBody);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("AI 응답 오류: " + e.getMessage());
        }
    }
}
