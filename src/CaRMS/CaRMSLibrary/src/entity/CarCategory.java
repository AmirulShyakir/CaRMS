/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dtjldamien
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carCategoryId;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    private String carCategoryName;

    @OneToMany(mappedBy = "carCategory")
    private List<Model> models;
    @OneToMany(mappedBy = "carCategory")
    private List<RentalRate> rentalRates;

    public CarCategory() {
        this.models = new ArrayList<>();
        this.rentalRates = new ArrayList<>();
    }

    public CarCategory(String categoryName) {
        this();

        this.carCategoryName = categoryName;
    }

    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    public String getCarCategoryName() {
        return carCategoryName;
    }

    public void setCarCategoryName(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }

    @XmlTransient
    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        if (!this.models.contains(model)) {
            this.models.add(model);
        }
    }

    public void removeModel(Model model) {
        if (this.models.contains(model)) {
            this.models.remove(model);
        }
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    public void addRentalRate(RentalRate rentalRate) {
        if (!this.rentalRates.contains(rentalRate)) {
            this.rentalRates.add(rentalRate);
        }
    }

    public void removeRentalRate(RentalRate rentalRate) {
        if (this.rentalRates.contains(rentalRate)) {
            this.rentalRates.remove(rentalRate);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCategoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Category[ id=" + carCategoryId + " ]";
    }
}
