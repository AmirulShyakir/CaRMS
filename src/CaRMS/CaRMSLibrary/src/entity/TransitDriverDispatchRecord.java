/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author dtjldamien
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchRecordId;

    @Column(nullable = true)
    private Boolean isCompleted;
    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date transitDate;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Employee dispatchDriver;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet destinationOutlet;
    @OneToOne(optional = false)
    private RentalReservation rentalReservation;

    @Transient
    private static final double TRANSIT_TIME = 2.0;

    public TransitDriverDispatchRecord() {
        this.isCompleted = false;
    }

    public TransitDriverDispatchRecord(Date transitDate) {
        this();
        
        this.transitDate = transitDate;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Long getTransitDriverDispatchRecordId() {
        return transitDriverDispatchRecordId;
    }

    public void setTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) {
        this.transitDriverDispatchRecordId = transitDriverDispatchRecordId;
    }

    public Employee getDispatchDriver() {
        return dispatchDriver;
    }

    public void setDispatchDriver(Employee dispatchDriver) {
        this.dispatchDriver = dispatchDriver;
    }

    public Outlet getDestinationOutlet() {
        return destinationOutlet;
    }

    public void setDestinationOutlet(Outlet destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    public RentalReservation getRentalReservation() {
        return rentalReservation;
    }

    public void setRentalReservation(RentalReservation rentalReservation) {
        this.rentalReservation = rentalReservation;
    }

    public Date getTransitDate() {
        return transitDate;
    }

    public void setTransitDate(Date transitDate) {
        this.transitDate = transitDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchRecordId != null ? transitDriverDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchRecordId == null && other.transitDriverDispatchRecordId != null) || (this.transitDriverDispatchRecordId != null && !this.transitDriverDispatchRecordId.equals(other.transitDriverDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitDriverDispatchRecordId + " ]";
    }

}
