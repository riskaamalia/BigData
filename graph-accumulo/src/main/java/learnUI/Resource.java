package learnUI;

/**
 * Created by 1224A on 5/20/2016.
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("home")
/*
@Path(�path/{var}�)
*/
public class Resource {
    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello, world!";
    }
/*
    @GET
    @Path("path/{var}")
    @Produces(MediaType.TEXT_PLAIN)
    public String pathMethod(@PathParam("var") String name) {
        return "Hello, " + name;
    }

    @GET
    @Path("index")
    @Produces({MediaType.TEXT_HTML})
    *//*public String test() {
        return "<script>alert('test');</script>";
    }*//*
    public InputStream viewHome()
    {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("index.html");
        return is;
        *//*@POST
            @Path("post")
            @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
            @Produces(MediaType.TEXT_HTML)
            public String postMethod(@FormParam("name") String name) {
              return "<h2>Hello, " + name + "</h2>";
            }*//*
    }*/

    @Path("post")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String viewHome2(@FormParam("name") String name)
    {
        return "<h2>Hello, " + name + "</h2>";
    }

}
