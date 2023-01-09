package app.core.services;

import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Scope("prototype")
public class CompanyService extends ClientService{
    private int companyID;

    /**
     * check if the company exist in database
     * @param email the company email
     * @param password the company password
     * @return true if company exist and false if not
     */
    public boolean login(String email, String password) {
      if(companyRepo.findByEmailAndPassword(email,password)!=null) {
          companyID = companyRepo.findByEmailAndPassword(email, password).getId();
          return true;
      }
      else{
          return false;
      }

    }

    /**
     * add coupon to database and check if the company have same coupon
     * @param coupon the coupon to add
     */
    public Coupon addCoupon(Coupon coupon) throws ClientServiceException {
        if(!couponRepo.existsByTitleAndCompanyID(coupon.getTitle(),companyID)){
            coupon.setCompanyID(companyID);
            getCompanyDetails().getCoupons().add(coupon);
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the company: "+companyID+" have coupon with same title");
        }
    }


    /**
     * update coupon
     * @param coupon the coupon to update
     */
    public Coupon updateCoupon(Coupon coupon) throws ClientServiceException {
       Coupon coupon1= couponRepo.findById(coupon.getId()).orElseThrow(()->new ClientServiceException("the coupon not exist"));
        if(coupon1.getCompanyID()==companyID){
            coupon.setCompanyID(companyID);
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the company id is not the same or coupon not exist");
        }
    }

    /**
     * delete coupon from database
     * @param couponID coupon id to delete
     */
    public void deleteCoupon(int couponID) throws ClientServiceException {
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon not exist"));
        if(coupon.getCompanyID()==companyID)
            couponRepo.delete(coupon);
        else
            throw new ClientServiceException("the coupon is not for tis company");
    }

    /**
     * get all company coupons
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(){
        return couponRepo.findByCompanyID(companyID);
    }
    /**
     * get all company coupons with specific category
     * @param category the category
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(Category category){
        return couponRepo.findByCompanyIDAndCategory(companyID,category);
    }
    /**
     * get all company coupons under specific price
     * @param maxPrice the maximum price
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(double maxPrice){
        return couponRepo.findByCompanyIDAndPriceLessThan(companyID,maxPrice);
    }
    /**
     * get the company details
     * @return company
     */
    public Company getCompanyDetails(){
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company not exist"));
    }
}
