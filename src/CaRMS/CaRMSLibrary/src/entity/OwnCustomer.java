/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dtjldamien
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class OwnCustomer extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String username;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String password;

    // anyone of the below unique identifiers
    @Column(nullable = true, length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String email;
    @Column(nullable = true, length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    private String phoneNumber;
    @Column(nullable = true, length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    private String passportNumber;

    public OwnCustomer() {
    }

    public OwnCustomer(String firstName, String lastName, String username, String password, String email, String phoneNumber, String passportNumber) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.customerId != null ? this.customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ownCustomerId fields are not set
        if (!(object instanceof OwnCustomer)) {
            return false;
        }
        OwnCustomer other = (OwnCustomer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OwnCustomer[ id=" + customerId + " ]";
    }
}
