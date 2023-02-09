package ru.netology.moneytransferservice.controllers;

import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.OperationIdDto;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.services.TransferService;

@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

   @PostMapping("/transfer")
    public OperationIdDto transferSender(@RequestBody Transferer transferer) {
        return transferService.executeTransfer(transferer);
    }

    @PostMapping("/confirmOperation")
    public OperationIdDto transferSender(@RequestBody Confirmer confirmer) {
       return transferService.confirmOperation(confirmer);
    }
}