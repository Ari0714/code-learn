package com.itbys._02_user_behaviors_analysis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlCount {

    private String url;
    private Long startTime;
    private Long endTime;
    private Long cnt;

}
