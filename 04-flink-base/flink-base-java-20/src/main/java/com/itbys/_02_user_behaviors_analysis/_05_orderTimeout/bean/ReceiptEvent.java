package com.itbys._02_user_behaviors_analysis._05_orderTimeout.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptEvent {
    private String txId;
    private String payChannel;
    private Long timestamp;

}
