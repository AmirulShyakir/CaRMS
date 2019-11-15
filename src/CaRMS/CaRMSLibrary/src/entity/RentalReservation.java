/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
    @Column(nullable = false)
    @NotNull
    private Boolean paid;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;
    @Column(nullable = false)
    @NotNull
    private Boolean isCancelled;
    @Column(nullable = false)
    @NotNull
    private Boolean isComplete;

    @OneToOne(optional = true)
    private Car car;
    @OneToOne(optional = true)
    private CarCategory carCategory;
    @OneToOne(optional = true)
    private Model model;
    @OneToMany(mappedBy = "rentalReservation")
    private List<RentalDay> rentalDays;
    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    private TransitDriverDispatchRecord transitDriverDispatchRecord;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private Partner partner;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Customer customer;
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Outlet pickupOutlet;
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Outlet returnOutlet;

    //optional car
    // choose any car from a category or a particular model (both are optional)
    // both pickup and return outlet are different
    // one to many rental day
    public RentalReservation() {
        this.isCancelled = false;
        this.isComplete = false;
        this.rentalDays = new ArrayList<>();
    }

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlTransient
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @XmlTransient
    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    @XmlTransient
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @XmlTransient
    public List<RentalDay> getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(List<RentalDay> rentalDays) {
        this.rentalDays = rentalDays;
    }

    public void addRentalDay(RentalDay rentalDay) {
        if (!this.rentalDays.contains(rentalDay)) {
            this.rentalDays.add(rentalDay);
        }
    }

    public void removeRentalDay(RentalDay rentalDay) {
        if (this.rentalDays.contains(rentalDay)) {
            this.rentalDays.remove(rentalDay);
        }
    }

    @XmlTransient
    public TransitDriverDispatchRecord getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    public void setTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    @XmlTransient
    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @XmlTransient
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @XmlTransient
    public Outlet getPickupOutlet() {
        return pickupOutlet;
    }

    public void setPickupOutlet(Outlet pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    @XmlTransient
    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
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
