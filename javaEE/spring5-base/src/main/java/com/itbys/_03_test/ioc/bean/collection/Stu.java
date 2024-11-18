package com.itbys._03_test.ioc.bean.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stu {

    public String[] course;
    public List<String> list;
    public Map<String,String> map;
    public Set<String> set;

    public List<Course> courses;

}
