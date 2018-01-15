package ru.aovechnikov.voting.web.halresource;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.util.DateTimeUtil;
import ru.aovechnikov.voting.web.controllers.DishController;
import ru.aovechnikov.voting.web.controllers.MenuController;
import ru.aovechnikov.voting.web.controllers.VoteController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * To assemble {@link MenuResource} from {@link Menu} and adds the required links.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@Component
public class MenuResourceAssembler extends IdentifiableResourceAssemblerSupport<Menu, MenuResource> {

    public MenuResourceAssembler() {
        super(MenuController.class, MenuResource.class);
    }

    /**
     * Converts the {@link Menu} into an {@link MenuResource}.
     * if menu has today's date, then adds a link to vote with rel "voting-menu".
     */
    @Override
    public MenuResource toResource(Menu entity) {
        MenuResource resource = createResource(entity);
        resource.add(linkTo(methodOn(MenuController.class).findById(entity.getId())).withRel("menu"));
        resource.add(linkTo(methodOn(MenuController.class).findRestaurantForMenu(entity.getId())).withRel("restaurant"));
        resource.add(linkTo(methodOn(DishController.class).findByMenu(entity.getId(), null, null)).withRel("dishes"));
        resource.add(linkTo(methodOn(MenuController.class).createDishForMenu(null, entity.getId())).withRel("create-dish"));
        if (entity.getDate().equals(DateTimeUtil.getCurrentDate()))
        resource.add(linkTo(VoteController.class).slash(entity.getId()).withRel("voting-menu"));
        return resource;
    }

    /**
     * Converts all {@link Menu} into {@link MenuResource}.
     */
    @Override
    public List<MenuResource> toResources(Iterable<? extends Menu> entities) {
        return super.toResources(entities);
    }

    /**
     * Instantiates the {@link MenuResource}. Used for paging.
     */
    @Override
    protected MenuResource instantiateResource(Menu entity) {
        MenuResource resource = new MenuResource(entity);
        return resource;
    }
}