package com.itbys._03_test.ioc.bean.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    List<String> books;

}
