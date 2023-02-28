package ru.netology.moneytransferservice;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.moneytransferservice.exception.ConfirmationException;
import ru.netology.moneytransferservice.exception.InputDataException;
import ru.netology.moneytransferservice.exception.RepositoryException;
import ru.netology.moneytransferservice.exception.TransferException;
import ru.netology.moneytransferservice.logger.LoggerSimple;
import ru.netology.moneytransferservice.model.Amount;
import ru.netology.moneytransferservice.model.Confirmer;
import ru.netology.moneytransferservice.model.OperationIdDto;
import ru.netology.moneytransferservice.model.Transferer;
import ru.netology.moneytransferservice.repository.TemporaryRepository;
import ru.netology.moneytransferservice.service.TransferService;

public class TransferServiceTest {
    static TransferService sut;
    TemporaryRepository temporaryRepository = Mockito.mock(TemporaryRepository.class);

    @BeforeAll
    public static void startedAll() {
        System.out.println("Начало тестов");
    }

    @BeforeEach
    public void InitAndStart() {
        System.out.println("Старт теста");
        sut = new TransferService(temporaryRepository);
    }

    @AfterAll
    public static void finishAll() {
        System.out.println("Все тесты завершены");
    }

    @AfterEach
    public void finished() {
        System.out.println("Тест завершен");
        sut = null;
    }

    @Test
    public void testExecuteTransfer() {
        // given:
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        long transferAmount = 100L;
        Transferer transferer = new Transferer(cardFromNumber, "1025", "123",
                cardToNumber, new Amount(transferAmount, "RUR"));

        Mockito.when(temporaryRepository.getCardBalance(cardFromNumber))
                .thenReturn(10000000L);
        Mockito.when(temporaryRepository.mapSearch(cardFromNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.mapSearch(cardToNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.saveTransfers("1", "0000"))
                .thenReturn(true);
        Mockito.when(temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount))
                .thenReturn(true);
        Mockito.when(temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount))
                .thenReturn(true);

        final OperationIdDto expected = new OperationIdDto("1");

        // when:
        OperationIdDto actual = sut.executeTransfer(transferer);

        // then:
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testExecuteTransferRepositoryException() {
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        long transferAmount = 100L;
        Transferer transferer = new Transferer(cardFromNumber,"1025","123",
                cardToNumber,new Amount(transferAmount, "RUR"));

        Mockito.when(temporaryRepository.getCardBalance(cardFromNumber))
                .thenReturn(10000000L);
        Mockito.when(temporaryRepository.mapSearch(cardFromNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.mapSearch(cardToNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.saveTransfers("1", "0000"))
                .thenReturn(false);
        Mockito.when(temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount))
                .thenReturn(true);
        Mockito.when(temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount))
                .thenReturn(true);

        Assertions.assertThrows(RepositoryException.class, () -> sut.executeTransfer(transferer));
    }
    @Test
    public void testExecuteTransferTransferException() {
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        long transferAmount = 100L;
        Transferer transferer = new Transferer(cardFromNumber,"1025","123",
                cardToNumber,new Amount(transferAmount, "RUR"));

        Mockito.when(temporaryRepository.getCardBalance(cardFromNumber))
                .thenReturn(10000000L);
        Mockito.when(temporaryRepository.mapSearch(cardFromNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.mapSearch(cardToNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.saveTransfers("1", "0000"))
                .thenReturn(true);
        Mockito.when(temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount))
                .thenReturn(false);
        Mockito.when(temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount))
                .thenReturn(true);

        Assertions.assertThrows(TransferException.class, () -> sut.executeTransfer(transferer));
    }

    @Test
    public void testConfirmOperationException() {
        Confirmer confirmer = new Confirmer("1", "0000");

        Mockito.when(temporaryRepository.checkForTransactionRecord())
                .thenReturn(true);

        Assertions.assertThrows(ConfirmationException.class, () -> sut.confirmOperation(confirmer));
    }

    @Test
    public void testConfirmOperation() {
        // given:
        Confirmer confirmer = new Confirmer("1", "0000");

        Mockito.when(temporaryRepository.checkForTransactionRecord())
                .thenReturn(false);
        Mockito.when(temporaryRepository.getTransferId(confirmer))
                .thenReturn(true);
        Mockito.when(temporaryRepository.getVerificationCode(confirmer))
                .thenReturn(true);

        OperationIdDto expected = new OperationIdDto("1");

        // when:
        OperationIdDto actual = sut.confirmOperation(confirmer);

        // then:
        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void testCardCheckerInvalidCardNumberOne() {
        String cardFromNumber = "5555554444443333";
        String cardToNumber = "2222222222222222";
        Amount amount = new Amount(100L,"RUR");
        LoggerSimple loggerSimple = new LoggerSimple(cardFromNumber, cardToNumber, amount);

        Mockito.when(temporaryRepository.mapSearch(cardFromNumber))
                .thenReturn(false);

        Assertions.assertThrows(InputDataException.class, () -> sut.cardChecker(cardFromNumber,
                cardToNumber, loggerSimple));
    }

    @Test
    public void testCardCheckerInvalidCardNumberTwo() {
        String cardFromNumber = "2222222222222222";
        String cardToNumber = "5555554444443333";
        Amount amount = new Amount(100L,"RUR");
        LoggerSimple loggerSimple = new LoggerSimple(cardFromNumber, cardToNumber, amount);

        Mockito.when(temporaryRepository.mapSearch(cardFromNumber))
                .thenReturn(true);
        Mockito.when(temporaryRepository.mapSearch(cardToNumber))
                .thenReturn(false);

        Assertions.assertThrows(InputDataException.class, () -> sut.cardChecker(cardFromNumber,
                cardToNumber, loggerSimple));
    }

    @Test
    public void testCompareBalanceWithTransferException() throws InputDataException{
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        Amount amount = new Amount(100L,"RUR");
        long transferAmount = 10000L;
        long balance = 100L;
        LoggerSimple loggerSimple = new LoggerSimple(cardFromNumber, cardToNumber, amount);

        Mockito.when(temporaryRepository.getCardBalance(cardFromNumber))
                .thenReturn(10000000L);

        Assertions.assertThrows(InputDataException.class, () -> sut.compareBalanceWithTransfer(balance,
                transferAmount, loggerSimple));
    }

    @Test
    public void testTransfer() {
        // given:
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        long transferAmount = 100L;

        Mockito.when(temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount))
                .thenReturn(true);
        Mockito.when(temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount))
                .thenReturn(true);

        final boolean expected = true;

        // when:
        boolean actual = sut.transfer(cardFromNumber, cardToNumber, transferAmount);

        // then:
        Assertions.assertEquals(expected, actual);
    }
}