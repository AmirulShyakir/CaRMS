/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
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
        System.out.println("number of rental reservations: " + rentalReservationsToBeAllocated.size());
        for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
            boolean isAllocated = false;
            List<Car> cars = carSessionBeanLocal.retrieveAllCars();
            System.out.println("number of cars : " + cars.size());
            if (rentalReservation.getModel() != null) {
                for (Car car : cars) {
                    System.out.println(1 + " " + car);
                    if (car.getModel().equals(rentalReservation.getModel())) {
                        rentalReservation.setCar(car);
                        System.out.println("car is " + car);
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
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE) && car.getRentalReservation() == null) {
                        rentalReservation.setCar(car);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    break;
                }
                // then check those currently on rental returning to same outlet
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            break;
                        }
                    }
                }
                // then check those currently on rental returning to a different outlet
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getRentalReservation().getEndDate().getYear() + 1900,
                                car.getRentalReservation().getEndDate().getMonth(),
                                car.getRentalReservation().getEndDate().getDate(),
                                start.getHours(), start.getMinutes(), start.getSeconds());
                        calendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = calendar.getTime();
                        if (rentalReservation.getStartDate().after(transitEndTime)) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
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
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE) && car.getRentalReservation() == null) { // already available in outlet
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
                    if ((car.getCarStatus() == CarStatusEnum.ON_RENT) && car.getRentalReservation().getReturnOutlet().equals(rentalReservation.getPickupOutlet())) {
                        if (car.getRentalReservation().getEndDate().before(rentalReservation.getStartDate())) {
                            rentalReservation.setCar(car);
                            isAllocated = true;
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    break;
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
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    break;
                }
            }
        }
        generateTransitDriverDispatchRecords(rentalReservationsToBeAllocated);
    }

    public void generateTransitDriverDispatchRecords(List<RentalReservation> rentalReservationsToBeAllocated) {
        try {
            for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
                Date transitStartDate = new Date();
                Long rentalReservationId = rentalReservation.getRentalReservationId();
                RentalReservation newRentalReservation = rentalReservationSessionBeanLocal.retrieveRentalReservationByRentalReservationId(rentalReservationId);
                if (rentalReservation.getCar().getOutlet() != null) { // car is currently in outlet
                    if (!(rentalReservation.getPickupOutlet().equals(rentalReservation.getCar().getOutlet()))) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                rentalReservation.getEndDate().getYear() + 1900,
                                rentalReservation.getEndDate().getMonth(),
                                rentalReservation.getEndDate().getDate(),
                                rentalReservation.getEndDate().getHours(),
                                rentalReservation.getEndDate().getMinutes(),
                                rentalReservation.getEndDate().getSeconds());
                        transitCalendar.add(Calendar.HOUR, -2);
                        transitStartDate = transitCalendar.getTime();
                    }
                } else { // car is currently going to be returned to another outlet
                    transitStartDate = rentalReservation.getCar().getRentalReservation().getEndDate();
                }
                try {
                    transitDriverDispatchRecordSessionBeanLocal.
                            createNewTranspatchDriverRecord(rentalReservation.getPickupOutlet().getOutletId(),
                                    rentalReservation.getRentalReservationId(), transitStartDate);
                } catch (OutletNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (RentalReservationNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (RentalReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
