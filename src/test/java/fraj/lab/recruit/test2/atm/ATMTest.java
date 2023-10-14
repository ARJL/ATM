package fraj.lab.recruit.test2.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTest {

    private static AmountSelector amountSelectorMock;
    private static CashManager cashManagerMock;
    private static ATM atm;

    @BeforeAll
    static void setup() {
        amountSelectorMock = mock(AmountSelector.class);
        cashManagerMock = mock(CashManager.class);
        atm = new ATM(amountSelectorMock, cashManagerMock, mock(PaymentProcessor.class));
    }

    @Test
    void testRunCashWithdrawal_WithInvalidAmount_ReturnsATMTechnicalException() {

        int invalidAmount = 0;

        when(amountSelectorMock.selectAmount()).thenReturn(invalidAmount);

        assertThrows(ATMTechnicalException.class, atm::runCashWithdrawal);

    }

    @Test
    void testRunCashWithdrawal_WithUnavailableCash_ReturnsCashNotAvailableStatus() throws ATMTechnicalException {

        int validAmount = 100;

        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(false);

        ATMStatus atmStatus = atm.runCashWithdrawal();

        assertEquals(ATMStatus.CASH_NOT_AVAILABLE, atmStatus);

    }

    @Test
    void testRunCashWithdrawal_WithAvailableCash_ReturnsOperationDoneStatus() throws ATMTechnicalException {

        int validAmount = 80;

        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(true);

        ATMStatus atmStatus = atm.runCashWithdrawal();

        assertEquals(ATMStatus.DONE, atmStatus);

    }

}


