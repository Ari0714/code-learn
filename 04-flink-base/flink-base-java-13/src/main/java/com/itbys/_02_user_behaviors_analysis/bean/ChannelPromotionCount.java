package com.itbys._02_user_behaviors_analysis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

 /**
 * Author chenj
 * Date 2021/9/1
 * Desc ChannelPromotionCount
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelPromotionCount {
    private String channel;
    private String behavior;
    private String windowEnd;
    private Long count;
}
