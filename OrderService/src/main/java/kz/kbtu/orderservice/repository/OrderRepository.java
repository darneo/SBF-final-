package kz.kbtu.orderservice.repository;

import kz.kbtu.orderservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}