package com.Hajuuu.page.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AladinBookInfo {

    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String isbn;
    private String isbn13;
    private String itemId;
    private String priceSales;
    private String priceStandard;
    private String mallType;
    private String stockStatus;
    private String mileage;
    private String cover;
    private String categoryId;
    private String categoryName;
    private String publisher;
    private String salesPoint;
    private boolean adult;
    private boolean fixedPrice;
    private String customerReviewRank;
    private AladinDetailInfo subInfo;
}
