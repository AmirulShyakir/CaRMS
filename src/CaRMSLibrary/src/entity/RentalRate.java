/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dtjldamien
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String rentalRateName;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal ratePerDay;

    // validity period if applicable
    @Temporal(TemporalType.DATE)
    @Column(nullable = false) // minimum start date is day before
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = true) // no end date
    private Date endDate;

    @ManyToOne(optional = false)
    @Column(nullable = false, length = 64)
    @NotNull
    private CarCategory category;

    @OneToMany
    @Column(nullable = false)
    private List<RentalDay> rentalDays;

    public RentalRate() {

    }

    public RentalRate(String rentalRateName, BigDecimal ratePerDay, Date startDate, Date endDate, CarCategory category) {
        this();

        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    public RentalRate(Long rentalRateId, String rentalRateName, BigDecimal ratePerDay, Date startDate, Date endDate, CarCategory category) {
        this();

        this.rentalRateId = rentalRateId;
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public String getRentalRateName() {
        return rentalRateName;
    }

    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
    }

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public List<RentalDay> getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(List<RentalDay> rentalDays) {
        this.rentalDays = rentalDays;
    }

    public void addRentalDay(RentalDay rentalDay) {
        if (!this.rentalDays.contains(rentalDay)) {
            this.rentalDays.add(rentalDay);
        }
    }

    public void removeRentalDay(RentalDay rentalDay) {
        if (this.rentalDays.contains(rentalDay)) {
            this.rentalDays.remove(rentalDay);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + rentalRateId + " ]";
    }

}
