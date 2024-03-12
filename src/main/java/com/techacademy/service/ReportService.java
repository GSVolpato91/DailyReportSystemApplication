package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;

    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report findById(Integer id) {

        Optional<Report> option = reportRepository.findById(id);
        Report report = option.orElse(null);
        return report;
    }
    public List <Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

    // SAVE
    @Transactional
    public ErrorKinds save(Report report, UserDetail userDetail) {

        Employee existingEmployee = userDetail.getEmployee();
        report.setEmployee(existingEmployee);
        report.getEmployeeCode();

        ErrorKinds result = validateReport(report);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }
        List<Report> existingReports = reportRepository
                .findByEmployeeCodeAndReportDate(userDetail.getEmployee().getCode(), report.getReportDate());

        for (Report existingReport : existingReports) {
            if (existingReport.getEmployeeCode().equals(userDetail.getEmployee().getCode())
                    && existingReport.getReportDate().isEqual(report.getReportDate())) {
                return ErrorKinds.DATECHECK_ERROR;
            }
        }

        LocalDateTime now = LocalDateTime.now();
        report.setDeleteFlg(false);
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);

        return ErrorKinds.SUCCESS;
    }

    // VALIDATE
    private ErrorKinds validateReport(Report report) {
        LocalDate reportDate = report.getReportDate();
        if (reportDate == null) {
            return ErrorKinds.BLANK_ERROR;
        }
        String abc = report.getTitle();
        if (abc.isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }
        String zxc = report.getContent();
        if (zxc.isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }
        return ErrorKinds.CHECK_OK;
    }

    // DELETE
    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;

    }

    // UPDATE
    @Transactional
    public ErrorKinds update(Report report, UserDetail userDetail) {

        ErrorKinds result = validateReport(report);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        List<Report> existingReports = reportRepository.findByEmployee(userDetail.getEmployee());

        for (Report existingReport : existingReports) {
            if (!existingReport.getId().equals(report.getId()) && existingReport.getReportDate().isEqual(report.getReportDate())) {
                return ErrorKinds.DATECHECK_ERROR;
            }
        }


            LocalDateTime now = LocalDateTime.now();
            report.setDeleteFlg(false);
            report.setCreatedAt(now);
            report.setUpdatedAt(now);
            report.setEmployee(userDetail.getEmployee());
            reportRepository.save(report);

        return ErrorKinds.SUCCESS;
    }
}
