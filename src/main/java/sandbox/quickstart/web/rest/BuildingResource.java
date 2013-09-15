/**
 * 
 */
package sandbox.quickstart.web.rest;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.entity.LatLng;
import sandbox.quickstart.model.LoginUser;
import sandbox.quickstart.service.IBuildingService;
import sandbox.quickstart.web.LoginUserHolder;

import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("building")
public class BuildingResource {

    private static final Method    METHOD_GET = findMethod("get", long.class); //$NON-NLS-1$

    private final IBuildingService buildingService;

    /**
     * @param pBuildingService -
     */
    @Inject
    public BuildingResource(final IBuildingService pBuildingService) {
        this.buildingService = ArgUtil.checkNull(pBuildingService, "pBuildingService"); //$NON-NLS-1$
    }

    /**
     * @param pName -
     * @param pAddress -
     * @param pLatitude -
     * @param pLongitude -
     * @param pIn -
     * @param pRequest -
     * @return -
     * @throws WebApplicationException -
     */
    @Path("")
    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces(MediaType.APPLICATION_JSON)
    public Response create( //
            @FormDataParam("name") final String pName //
            , @FormDataParam("address") final String pAddress //
            , @FormDataParam("latitude") final double pLatitude //
            , @FormDataParam("longitude") final double pLongitude //
            , @FormDataParam("image") final InputStream pIn //
            , @Context final HttpServletRequest pRequest) throws WebApplicationException {

        final LoginUser loginUser = checkLogin(pRequest);
        final ECandidateBuilding cb = new ECandidateBuilding();
        cb.setAddress(pAddress);
        cb.setName(pName);
        cb.setPosition(new LatLng(pLatitude, pLongitude));
        this.buildingService.insert(loginUser, cb, pIn);

        final URI uri = UriBuilder.fromPath("") //$NON-NLS-1$
                .path(METHOD_GET) //
                .build(cb.getId());
        return Response.created(uri).entity(new CandidateBuilding(cb)).build();
    }

    /**
     * @param pId -
     * @return -
     */
    @Path("{id}")
    @GET
    @Produces({ javax.ws.rs.core.MediaType.APPLICATION_JSON })
    public Response get(@PathParam("id") final long pId) {
        try {
            return Response.ok(new CandidateBuilding(this.buildingService.search(pId))).build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    private static LoginUser checkLogin(final HttpServletRequest pRequest) {
        final HttpSession session = pRequest.getSession();
        if (!LoginUserHolder.isLoggedin(session)) {
            throw new WebApplicationException(Status.NOT_ACCEPTABLE);
        }
        return LoginUserHolder.get(session);
    }

    private static Method findMethod(final String pName, final Class<?>... pArgumentTypes) {
        try {
            return BuildingResource.class.getMethod(pName, pArgumentTypes);
        } catch (final NoSuchMethodException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    static class CandidateBuilding {
        private final ECandidateBuilding candidateBuilding;

        CandidateBuilding() {
            this(new ECandidateBuilding());
        }

        CandidateBuilding(final ECandidateBuilding pCandidateBuilding) {
            this.candidateBuilding = pCandidateBuilding;
        }

        /**
         * @return -
         */
        public String getAddress() {
            return this.candidateBuilding.getAddress();
        }

        /**
         * @return -
         */
        public Date getCreated() {
            return this.candidateBuilding.getCreated();
        }

        /**
         * @return -
         */
        public long getId() {
            return this.candidateBuilding.getId().longValue();
        }

        /**
         * @return -
         */
        public String getName() {
            return this.candidateBuilding.getName();
        }

        /**
         * @return -
         */
        public LatLng getPosition() {
            return this.candidateBuilding.getPosition();
        }

        /**
         * @return -
         */
        public Date getUpdated() {
            return this.candidateBuilding.getUpdated();
        }
    }
}
