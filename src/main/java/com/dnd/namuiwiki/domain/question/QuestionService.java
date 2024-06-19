package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.wiki.WikiType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    @Value("${setting.password}")
    private String SETTING_PASSWORD;

    public List<QuestionDto> getQuestions(QuestionType questionType) {
        return questionRepository.findAll().stream()
                .filter(q -> questionType == null || q.getType().equals(questionType))
                .sorted(Comparator.comparing(Question::getSurveyOrder))
                .map(QuestionDto::from)
                .toList();
    }

    public void setDefaultQuestions(String pwd, WikiType wikiType) {
        validatePassword(pwd);

        try {
            JSONObject json;
            switch (wikiType) {
                case ROMANCE:
                    json = readJsonFile("json/romance-questions.json");
                    break;
                case NAMUI:
                    json = readJsonFile("json/base-document.json");
                    break;
                default:
                    throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_WIKI);
            }
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
        var allQuestions = questions.stream().map(q -> {
            JSONObject qq = (JSONObject) q;
            QuestionType type = QuestionType.valueOf(qq.get("type").toString());
            QuestionName name = QuestionName.valueOf(qq.get("name").toString());
            Question.QuestionBuilder questionBuilder = Question.builder()
                    .title(qq.get("title").toString())
                    .surveyOrder((Long) qq.get("surveyOrder"))
                    .dashboardType(DashboardType.valueOf(qq.get("dashboardType").toString()))
                    .name(name)
                    .reasonRequired((boolean) qq.get("reasonRequired"))
                    .type(type);

            if (type.isChoiceType()) {
                JSONArray keys = (JSONArray) qq.get("key");
                Map<String, Option> questionOptions = new HashMap<>();

                keys.forEach(optionNum -> {
                    int n = Integer.parseInt(optionNum.toString());
                    JSONObject optionContent = (JSONObject) options.get(n);
                    Option option = optionRepository.findByText(optionContent.get("text").toString())
                            .orElseThrow(() -> new RuntimeException("Option not found"));
                    questionOptions.put(option.getId(), option);
                });

                questionBuilder.options(questionOptions);
            }

            return questionBuilder.build();
        }).toList();
        questionRepository.saveAll(allQuestions);
    }

    private void setDefaultOptions(JSONArray options) {
        var allOptions = options.stream().map(opt -> {
            JSONObject option = (JSONObject) opt;
            var optionBuilder = Option.builder()
                    .order(Integer.parseInt(option.get("order").toString()))
                    .value(option.get("value"))
                    .text(option.get("text").toString());

            if (option.get("description") != null) {
                optionBuilder.description(option.get("description").toString());
            }
            return optionBuilder.build();
        }).toList();
        optionRepository.saveAll(allOptions);
    }

    private void validatePassword(String pwd) {
        if (!SETTING_PASSWORD.equals(pwd)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_PERMISSION);
        }
    }
}
