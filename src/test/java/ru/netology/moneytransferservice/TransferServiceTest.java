package ru.netology.moneytransferservice;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.loggers.LoggerSimple;
import ru.netology.moneytransferservice.models.Amount;
import ru.netology.moneytransferservice.repositories.TemporaryRepository;
import ru.netology.moneytransferservice.services.TransferService;

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
    public void testCompareBalanceWithTransferException() throws InputDataException{
        String cardFromNumber = "1111111111111111";
        String cardToNumber = "2222222222222222";
        Amount amount = new Amount(100L,"RUR");
        long transferAmount = 10000L;
        long balance = 100L;
        LoggerSimple loggerSimple = new LoggerSimple(cardFromNumber, cardToNumber, amount);
        Mockito.when(temporaryRepository.getCardBalance(cardFromNumber))
                .thenReturn(10000000L);

        Assertions.assertThrows(InputDataException.class, () -> {
            sut.compareBalanceWithTransfer(balance, transferAmount, loggerSimple);
        });
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
