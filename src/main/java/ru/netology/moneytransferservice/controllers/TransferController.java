package ru.netology.moneytransferservice.controllers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.services.TransferService;

import javax.validation.Valid;

@RestController
@Validated
public class TransferController {
TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public String transferSender(@Valid Transferer transferer){
        return transferService.executeTransfer(transferer);
    }


}
