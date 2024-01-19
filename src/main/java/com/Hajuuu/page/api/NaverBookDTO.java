package com.Hajuuu.page.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.Mapping;

@Data
@RequiredArgsConstructor
public class NaverBookDTO {

    private String lastBuildDate;
    private String total;
    private String start;
    private String display;
    private List<NaverBookInfo> items = new ArrayList<>();
}
