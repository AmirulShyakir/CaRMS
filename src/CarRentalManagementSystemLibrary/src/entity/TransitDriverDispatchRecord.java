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
import javax.persistence.ManyToOne;
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
    private Long transitDriverDispatchId;
    @ManyToOne(optional = false)    
    @Column(nullable = true) // unassigned
    private Employee dispatchDriver;
    @ManyToOne(optional = false)    
    @Column(nullable = false)
    private Car car;
    @ManyToOne(optional = false)    
    @Column(nullable = false)
    private Outlet currentOutlet;
    @ManyToOne(optional = false)    
    @Column(nullable = false)
    private Outlet destinationOutlet;

    @Transient
    private static final int TRANSIT_TIME = 2;
    
    public TransitDriverDispatchRecord() {
        
    }   
    
    public TransitDriverDispatchRecord(Car car, Outlet currentOutlet, Outlet destinationOutlet) {
        this();
        
        this.car = car;
        this.currentOutlet = currentOutlet;
        this.destinationOutlet = destinationOutlet;
    }

    public TransitDriverDispatchRecord(Long transitDriverDispatchId, Car car, Outlet currentOutlet, Outlet destinationOutlet) {
        this();
        
        this.transitDriverDispatchId = transitDriverDispatchId;
        this.car = car;
        this.currentOutlet = currentOutlet;
        this.destinationOutlet = destinationOutlet;
    }
    
    public Long getTransitDriverDispatchId() {
        return transitDriverDispatchId;
    }

    public void setTransitDriverDispatchId(Long transitDriverDispatchId) {
        this.transitDriverDispatchId = transitDriverDispatchId;
    }

    public Employee getDispatchDriver() {
        return dispatchDriver;
    }

    public void setDispatchDriver(Employee dispatchDriver) {
        this.dispatchDriver = dispatchDriver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Outlet getCurrentOutlet() {
        return currentOutlet;
    }

    public void setCurrentOutlet(Outlet currentOutlet) {
        this.currentOutlet = currentOutlet;
    }

    public Outlet getDestinationOutlet() {
        return destinationOutlet;
    }

    public void setDestinationOutlet(Outlet destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchId != null ? transitDriverDispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchId fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchId == null && other.transitDriverDispatchId != null) || (this.transitDriverDispatchId != null && !this.transitDriverDispatchId.equals(other.transitDriverDispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitDriverDispatchId + " ]";
    }
    
}
