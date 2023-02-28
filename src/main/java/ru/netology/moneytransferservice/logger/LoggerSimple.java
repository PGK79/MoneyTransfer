package ru.netology.moneytransferservice.logger;

import lombok.Data;
import ru.netology.moneytransferservice.model.Amount;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class LoggerSimple {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;
    private int operationId;

    public LoggerSimple(String cardFromNumber, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardToNumber = cardToNumber;
        this.amount = amount;

        File dirLog = new File("logs");
        dirLog.mkdir();
    }

    public synchronized void logFile(String result) {
        SimpleDateFormat dateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String data = dateNow.format(new Date());
        String log = "[" + data + "] " + "Карта списания: " + cardFromNumber + " Карта зачисления: "
                + cardToNumber + " " + " Cумма: " + amount.getValue() + " " + amount.getCurrency()
                + " Комиссия: " + amount.getValue() / 100 + " " + amount.getCurrency()
                + " Результат операции: " + result + "\n";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logs/logFile.log",
                true))) {
            bw.write(log);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}