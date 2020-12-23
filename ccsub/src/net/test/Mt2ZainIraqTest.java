package net.test;

import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.RedisCacheService;

public class Mt2ZainIraqTest {

	@Autowired
	private RedisCacheService redisCacheService;
	
	public static void main( String [] args) {
		Mt2ZainIraqTest test= new Mt2ZainIraqTest();
		test.redisCacheService.putObjectCacheValueByEvictionDay("MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX9647809668908", 74007683, 1);
	}
	
}
