package com.itbys.mvc.controller._05_restfu_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import java.util.Collection;

/**
 * Author xx
 * Date 2023/8/24
 * Desc
 */
@Controller
public class EmployeeController {

    @Autowired
    public EmployeeDao employeeDao;

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index() {
//        return "_05_restful_test/index";
//    }

    //获取所有用户
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String index(Model model) {
        Collection<Employee> all = employeeDao.getAll();
        model.addAttribute("employeeAll", all);
        return "_05_restful_test/employee_list";
    }

    //删除delete
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/employee";
    }

    //添加
    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public String addEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employee";
    }

    /**
     * 修改put
     */
    //回显employee数据
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public String getEmployeeById(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.get(id);
        model.addAttribute("employee", employee);
        return "_05_restful_test/employee_update";
    }

    //更新数据put
    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    public String UpdateEmployee(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/employee";
    }

}
