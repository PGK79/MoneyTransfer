package ru.netology.moneytransferservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.services.TransferService;

@RestController
@Slf4j
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

   @PostMapping("/transfer")
    public Confirmer transferSender(@RequestBody Transferer transferer) {
        return transferService.executeTransfer(transferer);
    }

    @PostMapping("/confirmOperation")
    public Confirmer transferSender(@RequestBody Confirmer confirmer) {
       System.out.println(confirmer);
       return transferService.confirmOperation(confirmer);
    }
}