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
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

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
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Employee dispatchDriver;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet destinationOutlet;

    @Transient
    private static final double TRANSIT_TIME = 2.0;
    
    public TransitDriverDispatchRecord() {
        
    }   
    
    public TransitDriverDispatchRecord(Employee dispatchDriver, Outlet destinationOutlet) {
        this();
        
        this.dispatchDriver = dispatchDriver;
        this.destinationOutlet = destinationOutlet;
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
