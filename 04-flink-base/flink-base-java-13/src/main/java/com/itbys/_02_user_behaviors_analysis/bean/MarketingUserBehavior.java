package com.itbys._02_user_behaviors_analysis.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingUserBehavior {

    private Long userId;
    private String behavior;
    private String channel;
    private Long timestamp;

}
