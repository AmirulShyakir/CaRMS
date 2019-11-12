/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
import entity.RentalReservation;
import entity.TransitDriverDispatchRecord;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateCarsToCurrentDayReservations")
    public void triggerCarAllocation() throws RentalReservationNotFoundException, NoAllocatableCarException {
        Date date = new Date();
        allocateCarsToCurrentDayReservations(date);
    }

    public void allocateCarsToCurrentDayReservations(Date date) throws RentalReservationNotFoundException, NoAllocatableCarException {
        Date today = date; // take in date from the cli
        today.setHours(2);
        today.setMinutes(0);
        today.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(today.getYear() + 1900, today.getMonth(), today.getDate(), today.getHours(), today.getMinutes(), today.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date nextDay = calendar.getTime();
        Query query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.startDate = :inStartDate");
        query.setParameter("inStartDate", today);
        List<RentalReservation> rentalReservationsToBeAllocated = query.getResultList();
        for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
            boolean isAllocated = false;
            Outlet pickupOutlet = rentalReservation.getPickupOutlet();
            List<Car> cars = pickupOutlet.getCars();
            if (rentalReservation.getModel() != null) {
                for (Car car : cars) {
                    if (car.getModel().equals(rentalReservation.getModel())) {
                        rentalReservation.setCar(car);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    break;
                }
                // current outlet has no cars to fulfill the rental reservation
                List<Car> carsOfSameModel = rentalReservation.getModel().getCars();
                for (Car car : carsOfSameModel) {
                    if ((!car.getOnRental()) && car.getRentalReservation() == null) {
                        rentalReservation.setCar(car);
                        break;
                    }
                }
                if (isAllocated) {
                    break;
                }
                // then check those currently on rental returning to same outlet
                for (Car car : cars) {
                    if ((car.getOnRental()) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            break;
                        }
                    }
                }
                // then check those currently on rental returning to a different outlet
                for (Car car : cars) {
                    if ((car.getOnRental()) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getRentalReservation().getEndDate().getYear() + 1900,
                                car.getRentalReservation().getEndDate().getMonth(),
                                car.getRentalReservation().getEndDate().getDate(),
                                today.getHours(), today.getMinutes(), today.getSeconds());
                        calendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = calendar.getTime();
                        if (rentalReservation.getStartDate().after(transitEndTime)) {
                            rentalReservation.setCar(car);
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    break;
                }
            } else { // rental reservation by car category
                for (Car car : cars) {
                    if (car.getModel().getCarCategory().equals(rentalReservation.getCarCategory())) {
                        rentalReservation.setCar(car);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    break;
                }
                // current outlet has no cars to fulfil the current rental reservation
                List<Model> models = rentalReservation.getCarCategory().getModels();
                List<Car> carsOfSameCategory = new ArrayList<>();
                for (Model model : models) {
                    carsOfSameCategory.addAll(model.getCars());
                }
                for (Car car : carsOfSameCategory) {
                    if ((!car.getOnRental()) && car.getRentalReservation() == null) { // already available in outlet
                        rentalReservation.setCar(car);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    break;
                }
                // check returning cars
                for (Car car : carsOfSameCategory) {
                    if ((car.getOnRental()) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            break;
                        }
                    }
                }
                // then check those currently on rental returning to a different outlet
                for (Car car : carsOfSameCategory) {
                    if ((car.getOnRental()) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
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
                            break;
                        }
                    }
                }
            }
        }
        generateTransitDriverDispatchRecords(rentalReservationsToBeAllocated);
    }

    public void generateTransitDriverDispatchRecords(List<RentalReservation> rentalReservationsToBeAllocated) {
        List<TransitDriverDispatchRecord> transitDriverDispatchRecords = new ArrayList<>();
        for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
            if (!(rentalReservation.getPickupOutlet().equals(rentalReservation.getCar().getOutlet()))) {
                GregorianCalendar transitCalendar = new GregorianCalendar(
                        rentalReservation.getEndDate().getYear() + 1900,
                        rentalReservation.getEndDate().getMonth(),
                        rentalReservation.getEndDate().getDate(),
                        rentalReservation.getEndDate().getHours(),
                        rentalReservation.getEndDate().getMinutes(),
                        rentalReservation.getEndDate().getSeconds());
                transitCalendar.add(Calendar.HOUR, -2);
                try {
                    transitDriverDispatchRecordSessionBeanLocal.
                            createNewTranspatchDriverRecord(rentalReservation.getPickupOutlet().getOutletId(),
                                    rentalReservation.getRentalReservationId(), transitCalendar.getTime());
                } catch (OutletNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (RentalReservationNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
