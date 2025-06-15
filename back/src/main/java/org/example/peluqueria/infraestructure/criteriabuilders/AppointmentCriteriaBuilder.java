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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentCriteriaBuilder {

    private final EntityManager em;

    public List<Appointment> findAllWithFilters(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
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
                startDateTime, endDateTime, status, clientEmail, orderStatus, minOrderTotal);

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Appointment> query = em.createQuery(cq)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize);

        return query.getResultList();
    }

    public long countWithFilters(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
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
                startDateTime, endDateTime, status, clientEmail, orderStatus, minOrderTotal);

        countQuery.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

        return em.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Appointment> root,
            Join<Appointment, AppUser> clientJoin,
            Join<Appointment, Order> orderJoin,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            AppointmentStatus status,
            String clientEmail,
            OrderStatus orderStatus,
            BigDecimal minOrderTotal
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (startDateTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), startDateTime));
        }
        if (endDateTime != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endTime"), endDateTime));
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


