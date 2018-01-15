package ru.aovechnikov.voting.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.AuthorizedUser;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.service.VoteService;
import ru.aovechnikov.voting.to.ResultTo;
import ru.aovechnikov.voting.to.VoteTo;
import ru.aovechnikov.voting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDateIfNull;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDateTime;

/**
 *  Rest controller provides endpoint for interaction with model {@link Vote}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@RestController
@RequestMapping(VoteController.REST_URL)
public class VoteController {

    public static final String REST_URL = "/rest/voting";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService service;

    /**
     * Find vote for user by date
     *
     * @param date date to search for(default current date)
     * @param authorizedUser user to search for
     * @return {@link Resource} of {@link Vote} with status {@link HttpStatus#OK}.
     */
    @GetMapping
    public ResponseEntity<Resource<Vote>> getVoteForUserByDate(@RequestParam(value = "date", required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @AuthenticationPrincipal AuthorizedUser authorizedUser){
        date = getCurrentDateIfNull(date);
        log.info("get vote for user with id={} by datetime {}", authorizedUser.getId(), date);
        Vote vote = service.getVoteForUserByDate(authorizedUser.getId(), date);
        Resource<Vote> resource = new Resource<>(vote);
        resource.add(linkTo(methodOn(VoteController.class).getVoteForUserByDate(vote.getDate(), authorizedUser)).withSelfRel());
        resource.add(linkTo(methodOn(MenuController.class).findById(vote.getMenu().getId())).withRel("menu"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Created new or updated existing vote for {@code authorizedUser}.
     *
     * @param menuId menu for which the user voted
     * @param authorizedUser voting user
     * @return {@link Resource} of {@link Vote} with status {@link HttpStatus#OK} if updated, {@link HttpStatus#CREATED} if created new.
     */
    @PostMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Vote>> vote(@PathVariable("id") int menuId, @AuthenticationPrincipal AuthorizedUser authorizedUser){
        log.info("voting user with id={} for the menu with id={} by datetime {}", authorizedUser.getId(), menuId, getCurrentDateTime());
        VoteTo voteTo = service.vote(authorizedUser.getId(), menuId);
        Resource<Vote> resource = new Resource<>(voteTo.getVote());
        resource.add(linkTo(methodOn(VoteController.class).getVoteForUserByDate(voteTo.getVote().getDate(), authorizedUser)).withSelfRel());
        resource.add(linkTo(methodOn(MenuController.class).findById(voteTo.getVote().getMenu().getId())).withRel("menu"));
        return new ResponseEntity<>(resource, voteTo.isCreated() ? HttpStatus.CREATED : HttpStatus.OK);
    }

    /**
     * Find all {@link ResultTo} by {@code date}
     *
     * @param date date to search for(default current date)
     * @return {@link List} of {@link Resource(ResultTo)} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/result", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<List<Resource<ResultTo>>> getResultToByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        date = DateTimeUtil.getCurrentDateIfNull(date);
        log.info("getResultToByDate by datetime {}", date);
        List<Resource<ResultTo>> result = service.getAllResultToByDate(date).stream()
                .map(resultTo -> {
                    Resource<ResultTo> resource = new Resource<>(resultTo);
                    resource.add(linkTo(methodOn(MenuController.class).findById(resultTo.getId())).withRel("menu"));
                    resource.add(linkTo(methodOn(MenuController.class).findRestaurantForMenu(resultTo.getId())).withRel("restaurant"));
                    return resource;
                }).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
