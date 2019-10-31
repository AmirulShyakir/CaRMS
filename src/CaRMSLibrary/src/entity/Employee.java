/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author dtjldamien
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String firstName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String lastName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String password;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String username;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private EmployeeRoleEnum employeeRole;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    private Outlet outlet;
    @OneToMany(mappedBy = "employee")
    @Column(nullable = true)
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String password, String email, Outlet outlet) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = email;
        this.outlet = outlet;
    }

    public Employee(Long employeeId, String firstName, String lastName, String password, String email, Outlet outlet) {
        this();

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = email;

        setOutlet(outlet);
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public EmployeeRoleEnum getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(EmployeeRoleEnum employeeRole) {
        this.employeeRole = employeeRole;
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
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + employeeId + " ]";
    }

}
