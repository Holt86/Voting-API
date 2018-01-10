package ru.aovechnikov.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.service.VoteService;

import java.time.LocalDate;

@RestController
@RequestMapping(VoteController.REST_URL)
public class VoteController {

    public static final String REST_URL = "/rest/voting";


    @Autowired
    private VoteService service;

    @GetMapping
    public ResponseEntity<Vote> getVoteForUserByDate(@RequestParam(value = "date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        return null;
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@PathVariable("id") int menuId){

        return null;
    }
}
