package com.sales.tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "created_ts", updatable = false)
//    @CreatedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date created;
//
//    @Column(name = "created_user", updatable = false)
//    private String createdBy;


}
