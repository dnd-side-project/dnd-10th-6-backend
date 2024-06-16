package com.dnd.namuiwiki.domain.wiki;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.wiki.dto.GetWikisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/v1/wikis")
public class WikiController {
    private final WikiService wikiService;

    public ResponseEntity<?> getWikis(
            @RequestHeader(required = false, value = "X-NAMUIWIKI-TOKEN") String accessToken
    ) {
        GetWikisResponse response = wikiService.getWikis(accessToken);
        return ResponseDto.created(response);
    }
}
