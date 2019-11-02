/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dtjldamien
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String outletName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String outletAddress;
    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    @NotNull
    private Date openingHour;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date closingHour;

    @OneToMany(mappedBy = "outlet")
    private List<Car> cars;
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;
    @OneToMany(mappedBy = "destinationOutlet")
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;

    public Outlet() {
        this.cars = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.transitDriverDispatchRecords = new ArrayList<>();
    }

    public Outlet(String outletName, String outletAddress, Date openingHour, Date closingHour) {
        this();

        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public Date getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(Date openingHour) {
        this.openingHour = openingHour;
    }

    public Date getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(Date closingHour) {
        this.closingHour = closingHour;
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }

    public void addTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        if (!this.transitDriverDispatchRecords.contains(transitDriverDispatchRecord)) {
            this.transitDriverDispatchRecords.add(transitDriverDispatchRecord);
        }
    }

    public void removeTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        if (this.transitDriverDispatchRecords.contains(transitDriverDispatchRecord)) {
            this.transitDriverDispatchRecords.remove(transitDriverDispatchRecord);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.NewEntity[ id=" + outletId + " ]";
    }

}
