package InterfaceSearch;

/**
 * Created by 1224A on 5/20/2016.
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("search")
public class ResourceSearch {

    @GET
    @Path("index")
    @Produces({MediaType.TEXT_HTML})

    public InputStream viewHome()
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("search.html");
        return is;
    }

    @POST
    @Path("hasilcari/{param}")
    public String pathMethod(@PathParam("param") String name) {
        return "Hello, " + name;
    }

}
