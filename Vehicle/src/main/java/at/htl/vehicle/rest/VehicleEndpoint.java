package at.htl.vehicle.rest;

import at.htl.vehicle.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("vehicle")
public class VehicleEndpoint {

    @GET
    @Path("{id}")
    public Vehicle find(@PathParam("id") long id){
        return new Vehicle("Opel " + id, "Commodore");
    }

    @GET
    public List<Vehicle> list(){
        List<Vehicle> list = new ArrayList<Vehicle>();
        list.add(find(42));
        list.add(find(43));
        return list;
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id){
        System.out.printf("%d deleted", id);
    }

    @POST
    public void save(Vehicle vehicle){
        System.out.println("Inserted" + vehicle);
    }
}
