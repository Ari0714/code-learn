package com.itbys.mvc.controller.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author xx
 * Date 2023/8/14
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    String usermame;
    String passwd;
    String hobby;

}
