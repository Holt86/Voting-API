package ru.aovechnikov.voting.web.servlet.halresource;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.web.servlet.controllers.MenuController;
import ru.aovechnikov.voting.web.servlet.controllers.RestaurantController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * To assemble {@link RestaurantResource} from {@link Restaurant} and adds the required links.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@Component
public class RestaurantResourceAssembler extends IdentifiableResourceAssemblerSupport<Restaurant, RestaurantResource> {

    public RestaurantResourceAssembler() {
        super(RestaurantController.class, RestaurantResource.class );
    }

    /**
     * Converts the {@link Restaurant} into an {@link RestaurantResource}.
     */
    @Override
    public RestaurantResource toResource(Restaurant entity) {
        RestaurantResource resource = createResource(entity);
        resource.add(linkTo(methodOn(RestaurantController.class).findById(entity.getId())).withRel("restaurant"));
        resource.add(linkTo(methodOn(MenuController.class).findByRestaurant(entity.getId(), null, null)).withRel("menus"));
        resource.add(linkTo(methodOn(RestaurantController.class).createMenuForRestaurants(null, entity.getId())).withRel("create-menu"));
        return resource;
    }

    /**
     * Converts all {@link Restaurant} into {@link RestaurantResource}.
     */
    @Override
    public List<RestaurantResource> toResources(Iterable<? extends Restaurant> entities) {
        return super.toResources(entities);
    }

    /**
     * Instantiates the {@link RestaurantResource}. Used for paging.
     */
    @Override
    protected RestaurantResource instantiateResource(Restaurant entity) {
        RestaurantResource resource = new RestaurantResource(entity);
        return resource;
    }
}