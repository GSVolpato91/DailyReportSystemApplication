package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

    // ID
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    @Column(columnDefinition = "VARCHAR(100)")
    @Length(max = 100)
    @NotBlank
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @Length(max = 600)
    @NotEmpty
    private String content;

    // Foreign key
    @Column(columnDefinition = "VARCHAR(10)", name = "employee_code", length = 10, insertable = false, updatable = false)
    private String employeeCode;

    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = " code ", nullable = false)
    private Employee employee;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private boolean deleteFlg;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}