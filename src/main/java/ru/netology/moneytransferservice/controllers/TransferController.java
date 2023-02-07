package ru.netology.moneytransferservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.services.TransferService;

import javax.validation.Valid;

@RestController
@RequestMapping("/")

public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("transfer")

    public String transferSender(@RequestBody Transferer transferer) {
        System.out.println(transferer);
        return transferService.executeTransfer(transferer);
    }

    //TODO реализовать
    @PostMapping("confirmOperation")
    public String transferSender(@Valid @RequestBody Confirmer confirmer) {
        System.out.println(confirmer);
        return "Confirm";
    }
}