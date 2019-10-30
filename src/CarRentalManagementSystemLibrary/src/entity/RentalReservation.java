/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dtjldamien
 */
@Entity
public class RentalReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalReservationId;
    @Column(nullable = false)
    @NotNull
    private Car car;
    @Column(nullable = false)
    @NotNull
    private Customer customer;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
    @Column(nullable = false)
    @NotNull
    private Boolean paid;
    @Column(nullable = false, length = 32)
    @NotNull
    private Outlet pickupOutlet;
    @Column(nullable = false)
    @NotNull
    private Outlet returnOutlet;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
//    @DecimalMin("0.00")
//    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;
    @Column(nullable = true)
    private TransitDriverDispatchRecord transitDriverDispatchRecord;
    @Column(nullable = true)
    private Partner partner;

    @ManyToOne
    @Column(nullable = false)
    private List<RentalDay> rentalDays;

    public RentalReservation() {
        this.rentalDays = new ArrayList<>();
    }

    public RentalReservation(Car car, Customer customer, Date startDate, Date endDate, Outlet pickupOutlet, Outlet returnOutlet) {
        this();

        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupOutlet = pickupOutlet;
        this.returnOutlet = returnOutlet;
    }

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Outlet getPickupOutlet() {
        return pickupOutlet;
    }

    public void setPickupOutlet(Outlet pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalReservationId != null ? rentalReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalReservationId fields are not set
        if (!(object instanceof RentalReservation)) {
            return false;
        }
        RentalReservation other = (RentalReservation) object;
        if ((this.rentalReservationId == null && other.rentalReservationId != null) || (this.rentalReservationId != null && !this.rentalReservationId.equals(other.rentalReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + rentalReservationId + " ]";
    }
}
