package at.htl.vehicle.rest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehicleEndpointIT {
    private Client client;
    private WebTarget target;

    @Before
    public void initClient(){
        this.client = ClientBuilder.newClient();
        this.target = client.target("http://localhost:8080/vehicle/rs/vehicle");
    }

    @Test
    public void t001_fetchVehicle(){
        Response response = this.target.request(MediaType.APPLICATION_XML).get(); //oder .post() oder .put() je nachdem
        assertThat(response.getStatus(), is(200));
        String payload = response.readEntity(String.class);
        System.out.println("Payload = "+payload);
    }
    @Test
    public void t002_fetchVehicleJsonObject(){
        Response response = this.target.request(MediaType.APPLICATION_JSON).get(); //oder .post() oder .put() je nachdem
        assertThat(response.getStatus(), is(200));
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("Payload = "+payload);

        JsonObject vehicle = payload.getJsonObject(0);
        assertThat(vehicle.getString("brand"), is("Opel 42"));
        assertThat(vehicle.getString("type"), is("Commodore"));
    }
    @Test
    public void t003_crud(){
        Response resposne = this.target.request(MediaType.APPLICATION_JSON).get();
        assertThat(resposne.getStatus(), is(200));
        JsonArray payload = resposne.readEntity(JsonArray.class);
        System.out.println("Payload = " + payload);
        assertThat(payload, not(empty()));

        JsonObject vehicle = payload.getJsonObject(0);
        assertThat(vehicle.getString("brand"), equalTo("Opel 42"));
        assertThat(vehicle.getString("type"), startsWith("Commodore"));

        JsonObject dedicatedVehicle = this.target.path("43").request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        assertThat(dedicatedVehicle.getString("brand"), equalTo("Opel 43"));
        assertThat(dedicatedVehicle.getString("type"), startsWith("Commodore"));

        Response deleteResponse = this.target.path("42").request().delete();
        assertThat(deleteResponse.getStatus(), is(204));
    }
}
