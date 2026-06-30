package com.polymeric.scheduled;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
	

	/**
	 * @category 商户回调重试
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void findKycState() {
		
		
	}
	

}
