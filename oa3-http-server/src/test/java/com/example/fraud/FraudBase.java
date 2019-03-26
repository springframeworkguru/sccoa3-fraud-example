package com.example.fraud;

import io.restassured.RestAssured;
import org.junit.Before;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.boot.web.server.LocalServerPort;

public class FraudBase {


	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new FraudDetectionController(),
				new FraudStatsController(stubbedStatsProvider()));
	}



	private StatsProvider stubbedStatsProvider() {
		return fraudType -> {
			switch (fraudType) {
			case DRUNKS:
				return 100;
			case ALL:
				return 200;
			}
			return 0;
		};
	}

	public void assertThatRejectionReasonIsNull(Object rejectionReason) {
		assert rejectionReason == null;
	}
}