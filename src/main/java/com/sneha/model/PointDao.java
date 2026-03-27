package com.sneha.model;

import com.sneha.Constant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = Constant.POINT_TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDao {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = Constant.RECORD_ID_COLUMN_NAME)
    private String recordId;

    @Column(name = Constant.RECORD_TYPE_COLUMN_NAME )
    private String recordType;

    @Column(name = Constant.AGGREGATE_POINT_COLUMN_NAME)
    private int aggregatedPoints;

    @CreationTimestamp
    @Column(name = Constant.CREATED_AT_COLUMN_NAME, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = Constant.UPDATED_AT_COLUMN_NAME)
    private LocalDateTime updatedAt;

    public int getAggregatedPoints() {
        return aggregatedPoints;
    }
}
