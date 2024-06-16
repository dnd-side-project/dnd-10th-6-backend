package com.dnd.namuiwiki.domain.wiki;

import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.survey.SurveyRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.dto.GetWikisResponse;
import com.dnd.namuiwiki.domain.wiki.dto.WikiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WikiService {
    private final JwtService jwtService;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public GetWikisResponse getWikis(String accessToken) {
        Long namuiWikiQuestionCount = questionRepository.countByWikiType(WikiType.NAMUI);
        Long romanceWikiQuestionCount = questionRepository.countByWikiType(WikiType.RAMANCE);

        WikiDto namuiWiki = WikiDto.builder()
                .wikiType(WikiType.NAMUI)
                .name(WikiType.NAMUI.getTitle())
                .description(WikiType.NAMUI.getDescription())
                .questionCount(namuiWikiQuestionCount)
                .build();
        WikiDto romanceWiki = WikiDto.builder()
                .wikiType(WikiType.RAMANCE)
                .name(WikiType.RAMANCE.getTitle())
                .description(WikiType.RAMANCE.getDescription())
                .questionCount(romanceWikiQuestionCount)
                .build();

        List<WikiDto> wikiList = new ArrayList<>();
        if (accessToken != null) {
            User user = jwtService.getUserByAccessToken(accessToken);
            Long namuiWikiAnswerCount = surveyRepository.countByOwnerAndWikiType(user, WikiType.NAMUI);
            Long romanceWikiAnswerCount = surveyRepository.countByOwnerAndWikiType(user, WikiType.RAMANCE);
            namuiWiki.setAnswerCount(namuiWikiAnswerCount);
            romanceWiki.setAnswerCount(romanceWikiAnswerCount);
        }
        wikiList.add(namuiWiki);
        wikiList.add(romanceWiki);

        return new GetWikisResponse(wikiList);
    }
}
