package ru.netology.moneytransferservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.model.Confirmer;
import ru.netology.moneytransferservice.model.OperationIdDto;
import ru.netology.moneytransferservice.model.Transferer;
import ru.netology.moneytransferservice.service.TransferService;

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