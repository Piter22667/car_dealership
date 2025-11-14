package org.example.car_dealership.specification;

import org.example.car_dealership.model.Car;
import org.example.car_dealership.model.config.car.Type;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CarSpecification {
    public Specification<Car> brandLike(String brand){
        return (root, query, cb) -> brand == null ? null
                :
                cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public Specification<Car> typeLike(Type type){
        return (root, query, cb) -> type == null ? null
                :
                cb.equal(root.get("type"), type);
    }

    public Specification<Car> minPrice(BigDecimal minPrice) {
        return (root, query, cb) -> minPrice == null ? null
                :
                cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public Specification<Car> maxPrice(BigDecimal maxPrice){
        return (root, query, cb) -> maxPrice == null ? null
                :
                cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
