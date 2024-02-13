package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    @Value("${setting.password}")
    private String SETTING_PASSWORD;

    public List<QuestionDto> getQuestions() {
        return questionRepository.findAll().stream()
                .sorted(Comparator.comparing(Question::getSurveyOrder))
                .map(QuestionDto::from)
                .toList();
    }

    public void setDefaultDocuments(String pwd) {
        if (!SETTING_PASSWORD.equals(pwd)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_PERMISSION);
        }

        try {
            JSONObject json = readJsonFile("json/base-document.json");
            JSONArray options = (JSONArray) json.get("options");
            JSONArray questions = (JSONArray) json.get("questions");

            setDefaultOptions(options);
            setDefaultQuestions(options, questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject readJsonFile(String path) {
        try {
            JSONParser parser = new JSONParser();
            ClassPathResource resource = new ClassPathResource(path);
            Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setDefaultQuestions(JSONArray options, JSONArray questions) {
        questionRepository.deleteAll();
        var allQuestions = questions.stream().map(q -> {
            JSONObject qq = (JSONObject) q;
            QuestionType type = QuestionType.of(qq.get("type").toString());
            Question.QuestionBuilder questionBuilder = Question.builder()
                    .title(qq.get("title").toString())
                    .type(type);

            if (type.isChoice()) {
                JSONArray keys = (JSONArray) qq.get("key");
                var questionOptions = keys.stream().map(optionNum -> {
                    int n = Integer.parseInt(optionNum.toString());
                    var optionContent = options.get(n);
                    return optionRepository.findByContent(optionContent)
                            .orElseThrow(() -> new RuntimeException("Option not found"));
                }).toList();
                questionBuilder.options(questionOptions);
            }
            return questionBuilder.build();
        }).toList();
        questionRepository.saveAll(allQuestions);
    }

    private void setDefaultOptions(JSONArray options) {
        optionRepository.deleteAll();
        var allOptions = options.stream().map(option -> Option.builder().content(option).build()).toList();
        optionRepository.saveAll(allOptions);
    }

}
