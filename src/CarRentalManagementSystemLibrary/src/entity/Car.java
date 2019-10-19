/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author dtjldamien
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, length = 64, unique = true)
    private String licensePlate;
    @Column(nullable = false, length = 64)
    private String colour;
    @Column(nullable = false)
    private Boolean rentalStatus; // true means on rental, false means in outlet
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = true) // nullable means on rental
    private Outlet outlet;

    public Car() {
        this.rentalStatus = false;
    }

    public Car(String licensePlate, String colour, Category category, Model model, Outlet outlet) {
        this();
        
        this.licensePlate = licensePlate;
        this.colour = colour;
        this.category = category;
        this.model = model;
        this.outlet = outlet;
    }
    
    public Car(Long carId, String licensePlate, String colour, Category category, Model model, Outlet outlet) {
        this();
        
        this.carId = carId;
        this.licensePlate = licensePlate;
        this.colour = colour;
        this.category = category;
        this.model = model;
        this.outlet = outlet;
    }
    
    public Long getCarId() {
        return carId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Boolean getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(Boolean rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    
    
    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }
    
}
