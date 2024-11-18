package com.itbys.springboot.controller;


import com.itbys.springboot.dao.DepartmentDao;
import com.itbys.springboot.dao.EmployeeDao;
import com.itbys.springboot.entities.Department;
import com.itbys.springboot.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@Controller
public class EmpsController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    @GetMapping("/emps")
    public String emps(Model model){
        Collection<Employee> all = employeeDao.getAll();
        model.addAttribute("emps",all);

        return "emp/list";

    }

    @GetMapping("/emp")
    public String toAddPage(Model model){
        Collection<Department> departments = departmentDao.getDepartments();

        model.addAttribute("depts",departments);

        return "emp/add";

    }

    @PostMapping("/emp")
    public String addemp(Employee employee){

        System.out.print(employee);

        employeeDao.save(employee);

        return "redirect:/emps";

    }

    @GetMapping("/emp/{id}")
    public String toEditpagr(@PathVariable("id") Integer id,Model model){

        Employee employee = employeeDao.get(id);

        model.addAttribute("emp",employee);

        Collection<Department> departments = departmentDao.getDepartments();

        model.addAttribute("depts",departments);

        return "emp/add";

    }


    @PutMapping("/emp")
    public String updateEmp(Employee employee){

        System.out.print(employee);
        employeeDao.save(employee);

        return "redirect:/emps";
    }

}
