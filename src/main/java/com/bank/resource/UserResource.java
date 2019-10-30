package com.bank.resource;

import com.bank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/users")
@Produces("application/json")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    static final String USER_ID_PARAM = "userId";

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
