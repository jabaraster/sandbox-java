package sandbox.quickstart.web.rest;

import jabara.general.Sort;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.entity.EUser_;
import sandbox.quickstart.service.IUserService;

/**
 *
 */
@Path("registrationUser")
public class UserResource {

    private final IUserService employeeService;

    /**
     * @param pEmployeeService
     */
    @Inject
    public UserResource(final IUserService pEmployeeService) {
        this.employeeService = pEmployeeService;
    }

    /**
     * @return ユーザ情報全件.
     */
    @Path("all")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<EUser> getAll() {
        return this.employeeService.getAll(Sort.asc(EUser_.userId.getName()));
    }
}
