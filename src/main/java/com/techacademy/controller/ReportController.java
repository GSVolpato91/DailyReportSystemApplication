package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());
        return "reports/list";
    }

    // DETAIL

    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));

        return "reports/detail";
    }

    // NEW

    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        model.addAttribute("name", userDetail.getEmployee().getName());
        model.addAttribute("code", userDetail.getUsername());
        return "reports/new";
    }

    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail,
            Model model) {

        if (res.hasErrors()) {
            return create(report, userDetail, model);
        }

        ErrorKinds result = reportService.save(report, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return create(report, userDetail, model);
        }
        return "redirect:/reports";
    }

    // UPDATE

    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable("id") Integer id, Report report, Model model) {
            model.addAttribute("report", reportService.findById(id));
        return "reports/update";
    }

    @PostMapping("/{id}/update")
    public String update(@Validated Report report, BindingResult res, @PathVariable("id") Integer id,
            @AuthenticationPrincipal UserDetail userDetail, Model model) {

        if (res.hasErrors()) {
            return edit(id, report, model);
        }
        ErrorKinds result = reportService.update(report, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return edit(id, report, model);
        }

        reportService.save(report, userDetail);
        return "redirect:/reports";
    }

    // DELETE

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }

}
