package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription=new Subscription();
        subscription.setId(subscriptionEntryDto.getUserId());
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        subscriptionRepository.save(subscription);
        int totalamount=subscription.getTotalAmountPaid();

        return totalamount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Subscription subscription=subscriptionRepository.findById(userId).get();
        if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Alreadythe best Subscription");
        }
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(subscription.getTotalAmountPaid()+300);
            subscription.setNoOfScreensSubscribed(250);
            subscriptionRepository.save(subscription);
            return 300;
        }
        if(subscription.getSubscriptionType().equals(SubscriptionType.PRO)){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(subscription.getTotalAmountPaid()+200);
            subscription.setNoOfScreensSubscribed(350);
            subscriptionRepository.save(subscription);
            return 200;
        }

        return null;



    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription>subscriptionList=subscriptionRepository.findAll();
        int revenue=0;
        for(Subscription s:subscriptionList){
            revenue+=s.getTotalAmountPaid();
        }

        return revenue;
    }

}
