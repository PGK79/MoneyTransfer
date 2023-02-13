package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.moneytransferservice.models.Amount;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Transferer;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyTransferServiceApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> backendApp =
            new GenericContainer<>("transferbackend:1.0")
                    .withExposedPorts(5500);

    @Test
    void testAcceptingFormData() {
        //given
        Transferer transferer = new Transferer("1111111111111111", "1025",
                "123", "2222222222222222", new Amount(10, "RUR"));
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/transfer", transferer, String.class);

        String expected = "{\"operationId\":\"2\"}";

        // when:
        String actual = response.getBody();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHttpStatusOkRequestTransfer() {
        //given
        Transferer transferer = new Transferer("1111111111111111", "1025",
                "123", "2222222222222222", new Amount(10, "RUR"));
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/transfer", transferer, String.class);

        HttpStatus expected = HttpStatus.OK;

        // when:
        HttpStatus actual = response.getStatusCode();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHttpStatusBadRequestTransfer() {
        //given
        Transferer transferer = new Transferer("1811411151114114", "1025",
                "123", "2222222222222222", new Amount(10, "RUR"));
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/transfer", transferer, String.class);

        HttpStatus expected = HttpStatus.BAD_REQUEST;

        // when:
        HttpStatus actual = response.getStatusCode();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testAcceptingTransactionIdAndSecretCode() {
        //given
        Confirmer confirmer = new Confirmer("2", "0000");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/confirmOperation", confirmer, String.class);

        String expected = "{\"operationId\":\"2\"}";

        // when:
        String actual = response.getBody();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHttpStatusOkConfirmOperation() {
        //given
        Confirmer confirmer = new Confirmer("2", "0000");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/confirmOperation", confirmer, String.class);

        HttpStatus expected = HttpStatus.OK;

        // when:
        HttpStatus actual = response.getStatusCode();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testConfirmOperationBadRequest() {
        //given
        Confirmer confirmer = new Confirmer("1", "0000");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"
                + backendApp.getMappedPort(5500) + "/confirmOperation", confirmer, String.class);

        HttpStatus expected = HttpStatus.BAD_REQUEST;

        // when:
        HttpStatus actual = response.getStatusCode();

        // then:
        Assertions.assertEquals(expected, actual);
    }
}