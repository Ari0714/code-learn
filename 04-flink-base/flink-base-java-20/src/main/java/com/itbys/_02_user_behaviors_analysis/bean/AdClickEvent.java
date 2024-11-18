package com.itbys._02_user_behaviors_analysis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdClickEvent {
    private Long userId;
    private Long adId;
    private String province;
    private String city;
    private Long timestamp;

}
