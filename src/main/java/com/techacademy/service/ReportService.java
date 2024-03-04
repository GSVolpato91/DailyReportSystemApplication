package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
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
        return reportRepository.findById(id).get();
    }

    @Transactional
    public ErrorKinds save(Report report) {

        ErrorKinds result = validateReport(report);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        report.setId(null);

        reportRepository.save(report);

        return ErrorKinds.SUCCESS;
    }

    private ErrorKinds validateReport(Report report) {
        LocalDate reportDate = report.getReportDate();
        if (reportDate == null) {
            return ErrorKinds.BLANK_ERROR;
        }   String abc = report.getTitle();
        if (abc.isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }   String zxc = report.getContent();
        if (zxc.isEmpty()) {
            return ErrorKinds.BLANK_ERROR;
        }
        return ErrorKinds.CHECK_OK;
    }


}
