package com.Hajuuu.page.api;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NaverBookDTO {

    private String lastBuildDate;
    private String total;
    private String start;
    private String display;
    private List<NaverBookInfo> items = new ArrayList<>();
}
