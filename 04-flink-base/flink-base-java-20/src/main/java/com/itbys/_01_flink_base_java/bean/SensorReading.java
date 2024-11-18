package com.itbys._01_flink_base_java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorReading {

    private String id;
    private Long timestamp;
    private Double temp;

}
