package com.techacademy.repository;






import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query(value = "SELECT * FROM Reports r WHERE r.employee_code = :employeeCode AND r.report_date = :reportDate", nativeQuery = true)
    List<Report> findByEmployeeCodeAndReportDate(@Param("employeeCode") String employeeCode, @Param("reportDate") LocalDate reportDate);

    List<Report> findByEmployee(Employee employee);
}
