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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dtjldamien
 */
@Entity
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String make;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String model;

    @OneToMany(mappedBy = "model")
    private List<Car> cars;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public Model() {
        this.cars = new ArrayList<>();
    }

    public Model(String make, String model, CarCategory carCategory) {
        this();

        this.make = make;
        this.model = model;
        this.carCategory = carCategory;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
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

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Model[ id=" + modelId + " ]";
    }
}
