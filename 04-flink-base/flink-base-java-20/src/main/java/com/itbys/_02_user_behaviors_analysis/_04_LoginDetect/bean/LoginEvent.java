package com.itbys._02_user_behaviors_analysis._04_LoginDetect.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEvent {
    private Long userId;
    private String ip;
    private String loginState;
    private Long timestamp;

}
