package ru.aovechnikov.voting.web.halresource;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.web.controllers.DishController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * To assemble {@link DishResource} from {@link Dish} and adds the required links.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@Component
public class DishResourceAssembler extends IdentifiableResourceAssemblerSupport<Dish, DishResource> {

    public DishResourceAssembler() {
        super(DishController.class, DishResource.class);
    }

    /**
     * Converts the {@link Dish} into an {@link DishResource}.
     */
    @Override
    public DishResource toResource(Dish entity) {
        DishResource resource = createResource(entity);
        resource.add(linkTo(methodOn(DishController.class).findById(entity.getId())).withRel("dish"));
        resource.add(linkTo(methodOn(DishController.class).findMenuForDish(entity.getId())).withRel("menu"));
        return resource;
    }

    /**
     * Converts all {@link Dish} into {@link DishResource}.
     */
    @Override
    public List<DishResource> toResources(Iterable<? extends Dish> entities) {
        return super.toResources(entities);
    }

    /**
     * Instantiates the {@link DishResource}. Used for paging.
     */
    @Override
    protected DishResource instantiateResource(Dish entity) {
        DishResource resource = new DishResource(entity);
        return resource;
    }
}
