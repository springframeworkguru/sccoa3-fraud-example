package com.example.loan;

import com.example.loan.model.Client;
import com.example.loan.model.LoanApplication;
import com.example.loan.model.LoanApplicationResult;
import com.example.loan.model.LoanApplicationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

// tag::autoconfigure_stubrunner[]
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"com.example:oa3-http-server:+:stubs:6569"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
//@DirtiesContext
public class LoanApplicationServiceTests {
// end::autoconfigure_stubrunner[]

    @Autowired
    private LoanApplicationService service;

    @Test
    public void shouldSuccessfullyApplyForLoan() {
        // given:
        LoanApplication application = new LoanApplication(new Client("1234567890"),
                123.123);
        // when:
        LoanApplicationResult loanApplication = service.loanApplication(application);
        // then:
        assertThat(loanApplication.getLoanApplicationStatus())
                .isEqualTo(LoanApplicationStatus.LOAN_APPLIED);
        assertThat(loanApplication.getRejectionReason()).isNull();
    }

    // tag::client_tdd[]
    @Test
    public void shouldBeRejectedDueToAbnormalLoanAmount() {
        // given:
        LoanApplication application = new LoanApplication(new Client("1234567890"),
                99999);
        // when:
        LoanApplicationResult loanApplication = service.loanApplication(application);
        // then:
        assertThat(loanApplication.getLoanApplicationStatus())
                .isEqualTo(LoanApplicationStatus.LOAN_APPLICATION_REJECTED);
        assertThat(loanApplication.getRejectionReason()).isEqualTo("Amount too high");
    }
    // end::client_tdd[]

    @Test
    public void shouldSuccessfullyGetAllFrauds() {
        // when:
        int count = service.countAllFrauds();
        // then:
        assertThat(count).isEqualTo(200);
    }

    @Test
    public void shouldSuccessfullyGetAllDrunks() {
        // when:
        int count = service.countDrunks();
        // then:
        assertThat(count).isEqualTo(100);
    }

}
