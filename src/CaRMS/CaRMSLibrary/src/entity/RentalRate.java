/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

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
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String rentalRateName;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal ratePerDay;

    @Column(nullable = false)
    @NotNull
    private Boolean isEnabled;

    // validity period if applicable
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true) // minimum start date is day before
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date endDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public RentalRate() {
        this.isEnabled = true;
    }

    public RentalRate(String rentalRateName, BigDecimal ratePerDay) {
        this();

        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
    }

    public RentalRate(String rentalRateName, BigDecimal ratePerDay, Date startDate, Date endDate) {
        this();
        
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
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

    @XmlTransient
    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
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
