package ru.netology.moneytransferservice.loggers;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.moneytransferservice.models.Amount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
public class LoggerSimple {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;

    public synchronized void logFile(LoggerSimple loggerSimple, String result) {
        SimpleDateFormat dateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String data = dateNow.format(new Date());
        String log = "[" + data + "] " + "Карта списания: " + loggerSimple.getCardFromNumber()
                + " Карта зачисления: " + loggerSimple.getCardToNumber() + " " + " Cумма: "
                + loggerSimple.getAmount().getValue() + " " + loggerSimple.getAmount().getCurrency()
                + " Комиссия: " + loggerSimple.getAmount().getValue() / 100 + " "
                + loggerSimple.getAmount().getCurrency() + " Результат операции: " + result + "\n";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logs/logFile.log",
                true))) {
            bw.write(log);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
