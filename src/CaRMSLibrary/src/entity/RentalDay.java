/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dtjldamien
 */
@Entity
public class RentalDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    private RentalDay rentalDay;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    private RentalReservation rentalReservation;

    public RentalDay() {
    }
    
    public RentalDay(RentalDay rentalDay, RentalReservation rentalReservation) {
        this();
        
        this.rentalDay = rentalDay;
        this.rentalReservation = rentalReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentalDay getRentalDay() {
        return rentalDay;
    }

    public void setRentalDay(RentalDay rentalDay) {
        this.rentalDay = rentalDay;
    }

    public RentalReservation getRentalReservation() {
        return rentalReservation;
    }

    public void setRentalReservation(RentalReservation rentalReservation) {
        this.rentalReservation = rentalReservation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentalDay)) {
            return false;
        }
        RentalDay other = (RentalDay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalDay[ id=" + id + " ]";
    }
}
