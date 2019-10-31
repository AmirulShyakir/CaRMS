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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(nullable = false, length = 16, unique = true)
    @NotNull
    @Size(max = 16)
    private String licensePlate;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String colour;
    @Column(nullable = false)
    @NotNull
    private Boolean onRental; // true means on rental, false means in outlet

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Outlet outlet;
    @ManyToMany
    private List<RentalReservation> rentalReservations;

    public Car() {
        this.onRental = false;
        this.rentalReservations = new ArrayList<>();
    }

    public Car(String licensePlate, String colour, Model model, Outlet outlet) {
        this();

        this.licensePlate = licensePlate;
        this.colour = colour;
        this.model = model;
        this.outlet = outlet;
    }

    public Car(Long carId, String licensePlate, String colour, Model model, Outlet outlet) {
        this();

        this.carId = carId;
        this.licensePlate = licensePlate;
        this.colour = colour;
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

    public Boolean getOnRental() {
        return onRental;
    }

    public void setOnRental(Boolean onRental) {
        this.onRental = onRental;
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

    public List<RentalReservation> getRentalReservations() {
        return rentalReservations;
    }

    public void setRentalReservations(List<RentalReservation> rentalReservations) {
        this.rentalReservations = rentalReservations;
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
