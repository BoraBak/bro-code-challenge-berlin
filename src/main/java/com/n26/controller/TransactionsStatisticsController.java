package com.n26.controller;

import com.n26.exception.ApiBadRequestException;
import com.n26.exception.ApiNoContentRequestException;
import com.n26.exception.ApiUnprocessableEntityRequestException;
import com.n26.model.Statistics;
import com.n26.model.dto.TransactionsStatisticsDto;
import com.n26.model.entity.TransactionsStatisticsEntity;
import com.n26.request.EventIdGenerator;
import com.n26.service.TransactionsStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping({"/api/transStat"})
public class TransactionsStatisticsController {

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private TransactionsStatisticsService transactionsStatisticsService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody TransactionsStatisticsDto transStatDto) {
        String eventId = eventIdGenerator.generate();

        log.info("Received request to create a new transaction. EventId: {}", eventId);

        TransactionsStatisticsEntity transactionsStatisticsEntity;
        try {
            transactionsStatisticsEntity = transactionsStatisticsService.convertDtoToEntity(transStatDto, eventId);

            transactionsStatisticsService.createNewTransaction(transactionsStatisticsEntity, eventId);
        } catch (ApiUnprocessableEntityRequestException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }  catch (ApiBadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ApiNoContentRequestException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("Finished to create a new transaction. EventId: {}", eventId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<?> getStatistics() {
        String eventId = eventIdGenerator.generate();

        log.info("Received request to get statistics. EventId: {}", eventId);

        Statistics result = transactionsStatisticsService.getTransactionsAndStatistics();

        log.info("Finished get statistics. EventId: {}", eventId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete() {
        String eventId = eventIdGenerator.generate();

        log.info("Received request to delete all transactions. EventId: {}", eventId);

        transactionsStatisticsService.deleteAllTransactions(eventId);

        log.info("Finished to delete all transactions. EventId: {}", eventId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
