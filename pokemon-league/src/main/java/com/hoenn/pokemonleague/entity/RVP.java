package com.hoenn.pokemonleague.entity;

import com.hoenn.pokemonleague.enums.RVPStatus;
import com.hoenn.pokemonleague.enums.TrainerRegion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rvps")
public class RVP {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Trainer ID is required")
    private String trainerId;

    @NotNull(message = "Target region is required")
    @Enumerated(EnumType.STRING)
    private TrainerRegion targetRegion;

    @NotBlank(message = "Issuing city is required")
    private String issuingCityId;

    @NotNull(message = "RVP status is required")
    @Enumerated(EnumType.STRING)
    private RVPStatus status;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private LocalDate revokedDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public RVP() {}

    public RVP(String trainerId, TrainerRegion targetRegion, String issuingCityId) {
        this.trainerId = trainerId;
        this.targetRegion = targetRegion;
        this.issuingCityId = issuingCityId;
        this.status = RVPStatus.ACTIVE;
        this.issueDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusDays(90);
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public TrainerRegion getTargetRegion() {
        return targetRegion;
    }

    public void setTargetRegion(TrainerRegion targetRegion) {
        this.targetRegion = targetRegion;
    }

    public String getIssuingCityId() {
        return issuingCityId;
    }

    public void setIssuingCityId(String issuingCityId) {
        this.issuingCityId = issuingCityId;
    }

    public RVPStatus getStatus() {
        return status;
    }

    public void setStatus(RVPStatus status) {
        this.status = status;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getRevokedDate() {
        return revokedDate;
    }

    public void setRevokedDate(LocalDate revokedDate) {
        this.revokedDate = revokedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}