package com.Hajuuu.page.api;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class AladinBookDTO {

    private String version;
    private String logo;
    private String title;
    private String link;
    private String pubDate;
    private String totalResults;
    private String startIndex;
    private String itemsPerPage;
    private String query;
    private String searchCategoryId;
    private String searchCategoryName;
    private List<AladinBookInfo> item = new ArrayList<>();

}
