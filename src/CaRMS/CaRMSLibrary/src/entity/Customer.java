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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dtjldamien
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long customerId;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    protected String firstName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    protected String lastName;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    protected String email;

    @Column(nullable = true, length = 32)
    @Size(max = 32)
    protected String creditCardNumber;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Partner partner;
    @OneToMany(mappedBy = "customer")
    protected List<RentalReservation> rentalReservations;

    public Customer() {
        rentalReservations = new ArrayList<>();
    }

    public Customer(String firstName, String lastName, String creditCardNumber) {
        this();
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCardNumber = creditCardNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
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
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
