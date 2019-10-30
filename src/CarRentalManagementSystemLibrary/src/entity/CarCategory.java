/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
//    @Size(max = 64)
    private String carCategoryName;

    @OneToMany(mappedBy = "carCategory")
    private List<Car> cars;
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private List<Model> models;
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private List<RentalReservation> rentalReservations;

    public CarCategory() {
        this.cars = new ArrayList<>();
        this.rentalReservations = new ArrayList<>();
    }

    public CarCategory(String categoryName) {
        this();

        this.carCategoryName = categoryName;
    }

    public CarCategory(Long categoryId, String categoryName) {
        this();

        this.carCategoryId = categoryId;
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

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<RentalReservation> getRentalReservations() {
        return rentalReservations;
    }

    public void setRentalReservations(List<RentalReservation> rentalReservations) {
        this.rentalReservations = rentalReservations;
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

    public void addCar(Car car) {
        if (!this.cars.contains(car)) {
            this.cars.add(car);
        }
    }

    public void removeCar(Car car) {
        if (this.cars.contains(car)) {
            this.cars.remove(car);
        }
    }

    public void addRentalReservation(RentalReservation rentalReservation) {
        if (!this.rentalReservations.contains(rentalReservation)) {
            this.rentalReservations.add(rentalReservation);
        }
    }

    public void removeRentalReservation(RentalReservation rentalReservation) {
        if (this.rentalReservations.contains(rentalReservation)) {
            this.rentalReservations.remove(rentalReservation);
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
