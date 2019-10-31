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
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false, length = 64, unique = true)
    private String partnerName;
    @Column(nullable = false, length = 64)
    private String password;

    @OneToMany(mappedBy = "partner")
    private List<RentalReservation> reservations;
    
    public Partner() {
        reservations = new ArrayList<>();
    }
    
    public Partner(String partnerName, String password) {
        this();
        
        this.partnerName = partnerName;
        this.partnerName = password;
    }

    public Partner(Long partnerId, String partnerName, String password) {
        this();
        
        this.partnerId = partnerId;
        this.partnerName = partnerName;
        this.password = password;
    }    
    
    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RentalReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<RentalReservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(RentalReservation reservation)
    {
        if(!this.reservations.contains(reservation))
        {
            this.reservations.add(reservation);
        }
    }
    
    public void removeReservation(RentalReservation reservation)
    {
        if(this.reservations.contains(reservation))
        {
            this.reservations.remove(reservation);
        }
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }
    
}
