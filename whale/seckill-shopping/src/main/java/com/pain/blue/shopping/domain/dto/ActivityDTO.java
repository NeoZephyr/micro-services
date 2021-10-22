package com.pain.blue.shopping.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivityDTO {

    private String activityName;

    private String productId;

    private Date dateStart;

    private Date dateEnd;

    private Integer limitNum;

    private Integer stockNum;

    private Byte status;

    private String productUrl;

    private BigDecimal price;
}
