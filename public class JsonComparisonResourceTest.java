import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class JsonComparisonResourceTest {

    @Test
    public void testJsonComparisonEndpoint() {
        String json1 = "{\"name\": \"John Doe\", \"age\": 30, \"address\": {\"street\": \"123 Main St\", \"city\": \"New York\", \"zipcode\": \"10001\"}, \"demographics\": {\"gender\": \"Male\", \"height\": 180}}";
        String json2 = "{\"name\": \"John Smith\", \"age\": 35, \"address\": {\"street\": \"456 Elm St\", \"city\": \"Boston\", \"zipcode\": \"02101\", \"state\": \"MA\"}, \"demographics\": {\"gender\": \"Male\", \"height\": 180}}";

        given()
                .contentType(ContentType.JSON)
                .body("{\"json1\": " + json1 + ", \"json2\": " + json2 + "}")
                .when()
                .post("/json-comparison")
                .then()
                .statusCode(200)
                .body("changedAttributes", hasSize(2))
                .body("changedAttributes[0]", hasKey("address"))
                .body("changedAttributes[0].address", hasKey("street"))
                .body("changedAttributes[0].address", hasKey("city"))
                .body("changedAttributes[0].address", not(hasKey("zipcode")))
                .body("changedAttributes[0].address", not(hasKey("state")))
                .body("changedAttributes[1]", hasKey("name"));
    }

}
