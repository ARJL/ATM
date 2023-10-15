package fraj.lab.recruit.test2.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ATMTest {

    private static AmountSelector amountSelectorMock;
    private static CashManager cashManagerMock;
    private static PaymentProcessor paymentProcessorMock;
    private static ATM atm;

    @BeforeAll
    static void setup() {
        amountSelectorMock = mock(AmountSelector.class);
        cashManagerMock = mock(CashManager.class);
        paymentProcessorMock = mock(PaymentProcessor.class);
        atm = new ATM(amountSelectorMock, cashManagerMock, paymentProcessorMock);
    }

    @Test
    void testRunCashWithdrawal_WithInvalidAmount_ReturnsATMTechnicalException() {

        // Given
        int invalidAmount = 0;
        when(amountSelectorMock.selectAmount()).thenReturn(invalidAmount);

        // When
        Executable executable = () -> atm.runCashWithdrawal();

        // Then
        assertThrows(ATMTechnicalException.class, executable);

    }

    @Test
    void testRunCashWithdrawal_WithUnavailableCash_ReturnsCashNotAvailableStatus() throws ATMTechnicalException {

        // Given
        int validAmount = 100;
        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(false);

        // When
        ATMStatus atmStatus = atm.runCashWithdrawal();

        // Then
        assertEquals(ATMStatus.CASH_NOT_AVAILABLE, atmStatus);

    }

    @Test
    void testRunCashWithdrawal_WithAvailableCashAndFailedPayment_ReturnsPaymentRejectedStatus() throws ATMTechnicalException {

        // Given
        int validAmount = 120;
        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(true);
        when(paymentProcessorMock.pay(validAmount)).thenReturn(PaymentStatus.FAILURE);

        // When
        ATMStatus atmStatus = atm.runCashWithdrawal();

        // Then
        assertEquals(ATMStatus.PAYMENT_REJECTED, atmStatus);

    }

    @Test
    void testRunCashWithdrawal_WithAvailableCashAndNullPayment_ReturnsPaymentRejectedStatus() throws ATMTechnicalException {

        // Given
        int validAmount = 140;
        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(true);
        when(paymentProcessorMock.pay(validAmount)).thenReturn(null);

        // When
        ATMStatus atmStatus = atm.runCashWithdrawal();

        // Then
        assertEquals(ATMStatus.PAYMENT_REJECTED, atmStatus);

    }

    @Test
    void testRunCashWithdrawal_WithAvailableCashAndSuccessPayment_ReturnsOperationDoneStatus() throws ATMTechnicalException {

        // Given
        int validAmount = 80;
        when(amountSelectorMock.selectAmount()).thenReturn(validAmount);
        when(cashManagerMock.canDeliver(validAmount)).thenReturn(true);
        when(paymentProcessorMock.pay(validAmount)).thenReturn(PaymentStatus.SUCCESS);

        // When
        ATMStatus atmStatus = atm.runCashWithdrawal();

        // Then
        assertEquals(ATMStatus.DONE, atmStatus);

    }

}


