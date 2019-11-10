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

/**
 *
 * @author dtjldamien
 */
@Entity
public class RentalDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalDayId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RentalRate rentalRate;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RentalReservation rentalReservation;

    public RentalDay() {
    }

    public RentalDay(RentalRate rentalRate, RentalReservation rentalReservation) {
        this();

        this.rentalRate = rentalRate;
        this.rentalReservation = rentalReservation;
    }

    public Long getRentalDayId() {
        return rentalDayId;
    }

    public void setRentalDayId(Long rentalDayId) {
        this.rentalDayId = rentalDayId;
    }

    public RentalRate getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(RentalRate rentalRate) {
        this.rentalRate = rentalRate;
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
        hash += (rentalDayId != null ? rentalDayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalDayId fields are not set
        if (!(object instanceof RentalDay)) {
            return false;
        }
        RentalDay other = (RentalDay) object;
        if ((this.rentalDayId == null && other.rentalDayId != null) || (this.rentalDayId != null && !this.rentalDayId.equals(other.rentalDayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalDay[ id=" + rentalDayId + " ]";
    }
}
