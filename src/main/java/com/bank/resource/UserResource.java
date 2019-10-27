package com.bank.resource;

import com.bank.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces("application/json")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    public static final String USER_ID_PARAM = "userId";

    @GET
    @Path("/{" + USER_ID_PARAM + "}")
    public Response getCourse(@PathParam(USER_ID_PARAM) Long userId) {
        LOGGER.debug("GET userId [{}]", userId);
        final var user = User
                .builder()
                .emailAddress("popovici.gabriel@gmail.com")
                .userId(12_345)
                .userName("gabe")
                .build();

        return Response.ok(user).build();
    }
}
