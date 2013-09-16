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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import sandbox.quickstart.model.CandidateBuildingModel;
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
     * @param pId -
     * @return -
     */
    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") final long pId) {
        this.buildingService.deleteById(pId);
        return Response.noContent().build();
    }

    /**
     * @param pId -
     * @return -
     */
    @Path("{id}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response get(@PathParam("id") final long pId) {
        try {
            return Response.ok(new CandidateBuildingModel(this.buildingService.search(pId))).build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pId -
     * @param pRequest -
     * @return -
     */
    @Path("{id}/freeText")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFreeText(@PathParam("id") final long pId, @Context final HttpServletRequest pRequest) {
        checkLogin(pRequest);
        try {
            return Response.ok(this.buildingService.search(pId).getFreeText()).build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pId -
     * @param pName -
     * @param pAddress -
     * @param pLatitude -
     * @param pLongitude -
     * @param pFreeText -
     * @param pIn -
     * @param pRequest -
     * @return -
     * @throws WebApplicationException -
     */
    @Path("")
    @POST
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces(MediaType.APPLICATION_JSON)
    public Response post( //
            @FormDataParam("id") final String pId //
            , @FormDataParam("name") final String pName //
            , @FormDataParam("address") final String pAddress //
            , @FormDataParam("latitude") final double pLatitude //
            , @FormDataParam("longitude") final double pLongitude //
            , @FormDataParam("freeText") final String pFreeText //
            , @FormDataParam("image") final InputStream pIn //
            , @Context final HttpServletRequest pRequest) throws WebApplicationException {

        final LoginUser loginUser = checkLogin(pRequest);

        final ECandidateBuilding cb = new ECandidateBuilding();
        cb.setAddress(pAddress);
        cb.setFreeText(pFreeText);
        cb.setName(pName);
        cb.setPosition(new LatLng(pLatitude, pLongitude));

        if (pId == null || pId.length() == 0) {
            this.buildingService.insert(loginUser, cb, pIn);
            final URI uri = UriBuilder.fromPath("") //$NON-NLS-1$
                    .path(METHOD_GET) //
                    .build(cb.getId());
            return Response.created(uri).entity(new CandidateBuildingModel(cb)).build();
        }
        try {
            this.buildingService.update(loginUser, Long.parseLong(pId), cb, pIn);
            return Response.noContent().build();

        } catch (final NumberFormatException e) {
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
}
