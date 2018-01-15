package ru.aovechnikov.voting.web.halresource;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.web.controllers.AdminUserController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * To assemble {@link UserResource} from {@link User} and adds the required links.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@Component
public class UserResourceAssembler extends IdentifiableResourceAssemblerSupport<User, UserResource> {

    public UserResourceAssembler() {
        super(AdminUserController.class, UserResource.class);
    }

    /**
     * Converts the {@link User} into an {@link UserResource}.
     */
    @Override
    public UserResource toResource(User entity) {
        UserResource resource = createResource(entity);
        resource.add(linkTo(methodOn(AdminUserController.class).findById(entity.getId())).withRel("user"));
        resource.add(linkTo(methodOn(AdminUserController.class).enable(entity.getId(), !entity.isEnabled())).withRel("enabled"));
        return resource;
    }

    /**
     * Converts all {@link User} into {@link UserResource}.
     */
    @Override
    public List<UserResource> toResources(Iterable<? extends User> entities) {
        return super.toResources(entities);
    }

    /**
     * Instantiates the {@link UserResource}. Used for paging.
     */
    @Override
    protected UserResource instantiateResource(User entity) {
        UserResource resource = new UserResource(entity);
        return resource;
    }
}