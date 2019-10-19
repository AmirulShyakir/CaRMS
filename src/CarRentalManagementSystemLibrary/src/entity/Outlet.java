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
import javax.persistence.OneToMany;

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
    @Column(nullable = false, length = 64)
    private String outletName;
    @Column(nullable = false, length = 64)
    private String outletAddress;
    // need to think how to implement opening hours
    
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;
    @OneToMany(mappedBy = "outlet")
    private List<Reservation> reservations;
    
    public Outlet() {
        employees = new ArrayList<>();
        reservations = new ArrayList<>();
    }
    
    public Outlet(String outletName, String outletAddress) {
        this();
        
        this.outletName = outletName;
        this.outletAddress = outletAddress;
    }

    public Outlet(Long outletId, String outletName, String outletAddress) {
        this();
        
        this.outletId = outletId;
        this.outletName = outletName;
        this.outletAddress = outletAddress;
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    public void addEmployee(Employee employee)
    {
        if(!this.employees.contains(employee))
        {
            this.employees.add(employee);
        }
    }
    
    public void removeEmployee(Employee employee)
    {
        if(this.employees.contains(employee))
        {
            this.employees.remove(employee);
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
