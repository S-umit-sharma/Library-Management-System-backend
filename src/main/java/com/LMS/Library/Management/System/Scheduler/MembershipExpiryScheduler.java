package com.LMS.Library.Management.System.Scheduler;



// scheduler/MembershipExpiryScheduler.java
import com.LMS.Library.Management.System.services.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MembershipExpiryScheduler {

    private final MembershipService membershipService;

    /**
     * Runs every day at midnight.
     * Finds all ACTIVE memberships whose endDate < today and marks them EXPIRED.
     */
//    @Scheduled(cron = "0 0 0 * * *")
//    public void expireMemberships() {
//        log.info("Running membership expiry job...");
//        membershipService.expireOverdueMemberships();
//        log.info("Membership expiry job complete.");
//    }
}



