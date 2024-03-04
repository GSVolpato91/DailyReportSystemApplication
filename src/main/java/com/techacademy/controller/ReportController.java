package com.techacademy.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;

@Controller
@RequestMapping("reports")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 従業員一覧画面
    @GetMapping
    public String list(Model model) {
        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());
        return "reports/list";
    }

    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report) {
        return "reports/new";
    }

    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Model model) {
        ErrorKinds result = reportService.save(report);
        if (res.hasErrors()) {
            return create(report);
        }        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return create(report);
        }


        return "redirect:/reports";
    }
}
