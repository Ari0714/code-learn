package com.itbys._02_user_behaviors_analysis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCount {

    private Long itemId;
    private Long startTime;
    private Long EndTime;
    private Long cnt;

}
