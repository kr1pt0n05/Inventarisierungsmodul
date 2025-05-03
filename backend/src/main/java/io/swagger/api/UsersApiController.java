package io.swagger.api;

import io.swagger.model.Article;
import io.swagger.model.Error400;
import io.swagger.model.Error500;
import io.swagger.model.InlineResponse200;
import io.swagger.model.InlineResponse2001;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createOrder(@Parameter(in = ParameterIn.PATH, description = "User-id des Users bei dem die offene Bestellung angelegt werden soll", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> createOrderItem(@Parameter(in = ParameterIn.PATH, description = "User-id des Users bei dem die offene Bestellung angelegt werden soll", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Article body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteOrder(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteOrderItem(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
,@Parameter(in = ParameterIn.PATH, description = "ID des Artikels", required=true, schema=@Schema()) @PathVariable("item-id") Long itemId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> editOrderItem(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
,@Parameter(in = ParameterIn.PATH, description = "ID des Artikels", required=true, schema=@Schema()) @PathVariable("item-id") Long itemId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> loginUser(@Parameter(in = ParameterIn.QUERY, description = "Der Benutzername für das Login" ,schema=@Schema()) @Valid @RequestParam(value = "username", required = false) String username
,@Parameter(in = ParameterIn.QUERY, description = "Das Password für das Login" ,schema=@Schema()) @Valid @RequestParam(value = "password", required = false) String password
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> logoutUser() {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse200> openOrders(@Parameter(in = ParameterIn.PATH, description = "User-id vom User bei dem man die offenen Bestellungen sehen will", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse200>(objectMapper.readValue("{\r\n  \"orders\" : [ {\r\n    \"orders_description\" : \"order#2345632\",\r\n    \"orders_ordered\" : 0.8008281904610115,\r\n    \"orders_id\" : 5,\r\n    \"orders_price\" : 3556.64,\r\n    \"orders_company\" : \"Gedankenfabrik AG\"\r\n  }, {\r\n    \"orders_description\" : \"order#2345632\",\r\n    \"orders_ordered\" : 0.8008281904610115,\r\n    \"orders_id\" : 5,\r\n    \"orders_price\" : 3556.64,\r\n    \"orders_company\" : \"Gedankenfabrik AG\"\r\n  } ]\r\n}", InlineResponse200.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse200>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse200>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2001> usersUserIdOrdersOrderIdGet(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2001>(objectMapper.readValue("{\r\n  \"order[id]\" : [ {\r\n    \"articles_price\" : 323.65,\r\n    \"articles_description\" : \"Mega-Giga-RAM\",\r\n    \"articles_order_id\" : 6,\r\n    \"articles_serial_number\" : \"234HKL523\"\r\n  }, {\r\n    \"articles_price\" : 323.65,\r\n    \"articles_description\" : \"Mega-Giga-RAM\",\r\n    \"articles_order_id\" : 6,\r\n    \"articles_serial_number\" : \"234HKL523\"\r\n  } ]\r\n}", InlineResponse2001.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2001>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2001>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InlineResponse2001> usersUserIdOrdersOrderIdItemsGet(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse2001>(objectMapper.readValue("{\r\n  \"order[id]\" : [ {\r\n    \"articles_price\" : 323.65,\r\n    \"articles_description\" : \"Mega-Giga-RAM\",\r\n    \"articles_order_id\" : 6,\r\n    \"articles_serial_number\" : \"234HKL523\"\r\n  }, {\r\n    \"articles_price\" : 323.65,\r\n    \"articles_description\" : \"Mega-Giga-RAM\",\r\n    \"articles_order_id\" : 6,\r\n    \"articles_serial_number\" : \"234HKL523\"\r\n  } ]\r\n}", InlineResponse2001.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<InlineResponse2001>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse2001>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Article> usersUserIdOrdersOrderIdItemsItemIdGet(@Parameter(in = ParameterIn.PATH, description = "ID des Users", required=true, schema=@Schema()) @PathVariable("user-id") Long userId
,@Parameter(in = ParameterIn.PATH, description = "ID der Bestellung", required=true, schema=@Schema()) @PathVariable("order-id") Long orderId
,@Parameter(in = ParameterIn.PATH, description = "ID des Artikels", required=true, schema=@Schema()) @PathVariable("item-id") Long itemId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Article>(objectMapper.readValue("{\r\n  \"articles_price\" : 323.65,\r\n  \"articles_description\" : \"Mega-Giga-RAM\",\r\n  \"articles_order_id\" : 6,\r\n  \"articles_serial_number\" : \"234HKL523\"\r\n}", Article.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Article>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Article>(HttpStatus.NOT_IMPLEMENTED);
    }

}
