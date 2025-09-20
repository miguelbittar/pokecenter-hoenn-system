package com.hoenn.pokecenter.entity;

import com.hoenn.pokecenter.enums.PokemonCondition;
import com.hoenn.pokecenter.enums.PokemonStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String pokemonId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Species is required")
    @Size(max = 50, message = "Species must be at most 50 characters")
    private String species;

    @NotBlank(message = "Trainer ID is required")
    private String trainerId;

    @NotNull(message = "Condition is required")
    @Enumerated(EnumType.STRING)
    private PokemonCondition condition;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private PokemonStatus status;

    @NotNull(message = "Responsible Joy is required")
    @ManyToOne
    @JoinColumn(name = "nurse_joy_id")
    private NurseJoy responsibleJoy;

    @CreationTimestamp
    @Column(name = "admission_date", nullable = false)
    private LocalDateTime admissionDate;

    private LocalDateTime releaseDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public Pokemon() {}

    public Pokemon(String name, String species, String trainerId,
                   PokemonCondition condition, PokemonStatus status, NurseJoy responsibleJoy) {
        this.name = name;
        this.species = species;
        this.trainerId = trainerId;
        this.condition = condition;
        this.status = status;
        this.responsibleJoy = responsibleJoy;
    }

    public Pokemon(String name, String species, String trainerId, PokemonCondition condition, NurseJoy responsibleJoy) {
        this.name = name;
        this.species = species;
        this.trainerId = trainerId;
        this.condition = condition;
        this.responsibleJoy = responsibleJoy;
    }

    public void softDelete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void restore(){
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

    public String getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public PokemonCondition getCondition() {
        return condition;
    }

    public void setCondition(PokemonCondition condition) {
        this.condition = condition;
    }

    public PokemonStatus getStatus() {
        return status;
    }

    public void setStatus(PokemonStatus status) {
        this.status = status;
    }

    public NurseJoy getResponsibleJoy() {
        return responsibleJoy;
    }

    public void setResponsibleJoy(NurseJoy responsibleJoy) {
        this.responsibleJoy = responsibleJoy;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
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
