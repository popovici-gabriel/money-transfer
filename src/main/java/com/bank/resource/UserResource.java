package com.bank.resource;

import com.bank.domain.User;
import com.bank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/users")
@Produces(APPLICATION_JSON)
public class UserResource {

    static final String USER_ID_PARAM = "userId";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @GET
    public Response getAllUsers() {
        return Response
                .ok(userRepository.getAllUsers())
                .build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response save(User user) {
        final var userId = userRepository.save(user);
        return Response
                .created(URI.create("http://localhost:8080/users/" + userId))
                .build();
    }

    @GET
    @Path("/{" + USER_ID_PARAM + "}")
    public Response getUser(@PathParam(USER_ID_PARAM) Long userId) {
        if (userId == null) {
            return Response.status(BAD_REQUEST).build();
        }
        LOGGER.debug("GET userId [{}]", userId);


        return userRepository
                .findById(userId)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
