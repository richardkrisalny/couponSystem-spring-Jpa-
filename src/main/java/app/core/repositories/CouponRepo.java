package app.core.repositories;

import app.core.entites.Category;
import app.core.entites.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    boolean existsByTitleAndCompanyID(String title, int id);
    List<Coupon> findByCompanyID(int id);
    List<Coupon> findByCompanyIDAndCategory(int id, Category category);
    List<Coupon> findByCompanyIDAndPriceLessThan(int id,double maxPrice);
    List<Coupon> findByEndDateBetween(LocalDate from, LocalDate to);

}
