/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.RentalReservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.exception.NoAllocatableCarException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(EjbTimerSessionBeanLocal.class)
@Remote(EjbTimerSessionBeanRemote.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;
    @EJB
    private RentalReservationSessionBeanLocal rentalReservationSessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateCarsToCurrentDayReservations")
    public void triggerCarAllocation() throws RentalReservationNotFoundException, NoAllocatableCarException {
        Date date = new Date();
        allocateCarsToCurrentDayReservations(date);
    }

    public void allocateCarsToCurrentDayReservations(Date date) {
        Date start = date; // take in date from the cli
        start.setHours(2);
        start.setMinutes(0);
        start.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(start.getYear() + 1900, start.getMonth(), start.getDate(), start.getHours(), start.getMinutes(), start.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date end = calendar.getTime();
        Query query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.startDate >= :inStartDate AND r.startDate <= :inEndDate");
        query.setParameter("inStartDate", start);
        query.setParameter("inEndDate", end);
        List<RentalReservation> rentalReservationsToBeAllocated = query.getResultList();
        List<RentalReservation> rentalReservationsThatRequiresTransit = new ArrayList<>();
        for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
            boolean isAllocated = false;
            if (rentalReservation.getModel() != null) {
                List<Car> cars = carSessionBeanLocal.retrieveCarsByModelId(rentalReservation.getModel().getModelId());
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE && car.getRentalReservation() == null)
                            && car.getOutlet().getOutletId().equals(rentalReservation.getPickupOutlet().getOutletId())) {
                        rentalReservation.setCar(car);
                        car.setRentalReservation(rentalReservation);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                // current outlet has no cars to fulfill the rental reservation
                for (Car car : cars) {
                    if (car.getCarStatus() == CarStatusEnum.AVAILABLE && car.getRentalReservation() == null) {
                        rentalReservation.setCar(car);
                        car.setRentalReservation(rentalReservation);
                        isAllocated = true;
                        rentalReservationsThatRequiresTransit.add(rentalReservation);
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                // then check those currently on rental returning to same outlet
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT && car.getRentalReservation() == null)
                            && car.getRentalReservation().getReturnOutlet().getOutletName().equals(rentalReservation.getPickupOutlet().getOutletName())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            break;
                        }
                    }
                }
                // then check those currently on rental returning to a different outlet
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation() == null
                            && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getRentalReservation().getEndDate().getYear() + 1900,
                                car.getRentalReservation().getEndDate().getMonth(),
                                car.getRentalReservation().getEndDate().getDate(),
                                start.getHours(), start.getMinutes(), start.getSeconds());
                        transitCalendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = transitCalendar.getTime();
                        if (rentalReservation.getStartDate().after(transitEndTime)) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            rentalReservationsThatRequiresTransit.add(rentalReservation);
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
            } else { // rental reservation by car category
                List<Car> cars = carSessionBeanLocal.retrieveCarsByCarCategoryId(
                        rentalReservation.getCarCategory().getCarCategoryId());
                for (Car car : cars) {
                    if (car.getModel().getCarCategory().getCarCategoryName().equals(
                            rentalReservation.getCarCategory().getCarCategoryName())
                            && car.getRentalReservation() == null
                            && car.getOutlet().getOutletId().equals(rentalReservation.getPickupOutlet().getOutletId())) {
                        rentalReservation.setCar(car);
                        car.setRentalReservation(rentalReservation);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                // current outlet has no cars to fulfil the current rental reservation
                Long carCategoryId = rentalReservation.getCarCategory().getCarCategoryId();
                List<Car> carsOfSameCategory = carSessionBeanLocal.retrieveCarsByCarCategoryId(carCategoryId);
                for (Car car : carsOfSameCategory) {
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE) && car.getRentalReservation() == null) { // already available in outlet
                        rentalReservation.setCar(car);
                        car.setRentalReservation(rentalReservation);
                        isAllocated = true;
                        rentalReservationsThatRequiresTransit.add(rentalReservation);
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                // check returning cars to same outlet
                for (Car car : carsOfSameCategory) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation().getReturnOutlet().getOutletName()
                            .equals(rentalReservation.getPickupOutlet().getOutletName())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
                // then check those currently on rental returning to a different outlet
                for (Car car : carsOfSameCategory) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getRentalReservation().getEndDate().getYear() + 1900,
                                car.getRentalReservation().getEndDate().getMonth(),
                                car.getRentalReservation().getEndDate().getDate(),
                                car.getRentalReservation().getEndDate().getHours(),
                                car.getRentalReservation().getEndDate().getMinutes(),
                                car.getRentalReservation().getEndDate().getSeconds());
                        transitCalendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = transitCalendar.getTime();
                        if (rentalReservation.getStartDate().after(transitEndTime)) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            rentalReservationsThatRequiresTransit.add(rentalReservation);
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
            }
        }
        generateTransitDriverDispatchRecords(date, rentalReservationsThatRequiresTransit);
    }

    public void generateTransitDriverDispatchRecords(Date date, List<RentalReservation> rentalReservationsToBeAllocated) {
        try {
            for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
                Date transitStartDate = date;
                GregorianCalendar transitCalendar = new GregorianCalendar(
                        rentalReservation.getEndDate().getYear() + 1900,
                        rentalReservation.getEndDate().getMonth(),
                        rentalReservation.getEndDate().getDate(),
                        rentalReservation.getEndDate().getHours(),
                        rentalReservation.getEndDate().getMinutes(),
                        rentalReservation.getEndDate().getSeconds());
                transitCalendar.add(Calendar.HOUR, -2);
                transitStartDate = transitCalendar.getTime();
                transitDriverDispatchRecordSessionBeanLocal.
                        createNewTranspatchDriverRecord(rentalReservation.getPickupOutlet().getOutletId(),
                                rentalReservation.getRentalReservationId(), transitStartDate);
            }
        } catch (RentalReservationNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
