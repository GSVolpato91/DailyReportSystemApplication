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



    @Transactional
    public ErrorKinds save(Report report, UserDetail userDetail) {

        Employee existingEmployee = userDetail.getEmployee();
        report.getEmployeeCode();
        report.setEmployee(existingEmployee);


        ErrorKinds result = validateReport(report, userDetail);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        LocalDateTime now = LocalDateTime.now();
        report.setDeleteFlg(false);
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);

        return ErrorKinds.SUCCESS;
    }

    private ErrorKinds validateReport(Report report, UserDetail userDetail) {
        List<Report> existingReports = reportRepository.findByEmployeeCodeAndReportDate(
                userDetail.getEmployee().getCode(),
                report.getReportDate()
        );

        if (!existingReports.isEmpty()) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        LocalDate reportDate = report.getReportDate();

        if (reportDate == null ) {
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


    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;

    }

@Transactional
public ErrorKinds update(Report updatedReport, Report existingReport) {

    ErrorKinds result = validateReport(updatedReport, null);
        if (ErrorKinds.CHECK_OK != result) {
            return result;

        }

    updatedReport.setDeleteFlg(existingReport.isDeleteFlg());
    updatedReport.setCreatedAt(existingReport.getCreatedAt());
    updatedReport.setUpdatedAt(LocalDateTime.now());
    reportRepository.save(updatedReport);
    return ErrorKinds.SUCCESS;
}
}



