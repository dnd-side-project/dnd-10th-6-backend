package com.dnd.namuiwiki.domain.wiki;

import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.survey.SurveyRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.dto.GetWikisResponse;
import com.dnd.namuiwiki.domain.wiki.dto.WikiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WikiService {
    private final JwtService jwtService;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public GetWikisResponse getWikis(String accessToken) {
        List<WikiDto> wikiList = Arrays.stream(WikiType.values()).map(wikiType -> {
            Long wikiQuestionCount = questionRepository.countByWikiType(wikiType);
            return WikiDto.builder()
                    .wikiType(wikiType)
                    .name(wikiType.getTitle())
                    .description(wikiType.getDescription())
                    .questionCount(wikiQuestionCount)
                    .build();
        }).collect(Collectors.toList());

        if (accessToken != null) {
            User user = jwtService.getUserByAccessToken(accessToken);
            wikiList.forEach(wikiDto -> {
                WikiType wikiType = wikiDto.getWikiType();
                Long answerCount = surveyRepository.countByOwnerAndWikiType(user, wikiType);
                wikiDto.setAnswerCount(answerCount);
            });
        }

        return new GetWikisResponse(wikiList);
    }
}
