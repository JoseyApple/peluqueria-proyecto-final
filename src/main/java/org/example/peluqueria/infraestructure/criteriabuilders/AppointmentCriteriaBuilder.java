package org.example.peluqueria.infraestructure.criteriabuilders;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.peluqueria.domain.AppointmentStatus;
import org.example.peluqueria.domain.OrderStatus;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.domain.models.Appointment;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentCriteriaBuilder {

    private final EntityManager em;

    public List<Appointment> findAllWithFilters(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startHour,
            LocalTime endHour,
            AppointmentStatus status,
            String clientEmail,
            OrderStatus orderStatus,
            BigDecimal minOrderTotal,
            int pageNumber,
            int pageSize
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        Join<Appointment, AppUser> clientJoin = root.join("client", JoinType.LEFT);
        Join<Appointment, Order> orderJoin = root.join("order", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, clientJoin, orderJoin,
                startDate, endDate, startHour, endHour, status, clientEmail, orderStatus, minOrderTotal);

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Appointment> query = em.createQuery(cq)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize);

        return query.getResultList();
    }

    public long countWithFilters(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startHour,
            LocalTime endHour,
            AppointmentStatus status,
            String clientEmail,
            OrderStatus orderStatus,
            BigDecimal minOrderTotal
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Appointment> root = countQuery.from(Appointment.class);

        Join<Appointment, AppUser> clientJoin = root.join("client", JoinType.LEFT);
        Join<Appointment, Order> orderJoin = root.join("order", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, clientJoin, orderJoin,
                startDate, endDate, startHour, endHour, status, clientEmail, orderStatus, minOrderTotal);

        countQuery.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

        return em.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Appointment> root,
            Join<Appointment, AppUser> clientJoin,
            Join<Appointment, Order> orderJoin,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startHour,
            LocalTime endHour,
            AppointmentStatus status,
            String clientEmail,
            OrderStatus orderStatus,
            BigDecimal minOrderTotal
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(orderJoin.get("createdAt"), startDate.atStartOfDay()));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(orderJoin.get("createdAt"), endDate.atTime(LocalTime.MAX)));
        }
        if (startHour != null) {
            predicates.add(cb.greaterThanOrEqualTo(cb.function("TIME", LocalTime.class, root.get("startTime")), startHour));
        }
        if (endHour != null) {
            predicates.add(cb.lessThanOrEqualTo(cb.function("TIME", LocalTime.class, root.get("endTime")), endHour));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (clientEmail != null && !clientEmail.isEmpty()) {
            predicates.add(cb.equal(clientJoin.get("email"), clientEmail));
        }
        if (orderStatus != null) {
            predicates.add(cb.equal(orderJoin.get("status"), orderStatus));
        }
        if (minOrderTotal != null) {
            predicates.add(cb.greaterThanOrEqualTo(orderJoin.get("totalAmount"), minOrderTotal));
        }

        return predicates;
    }
}

