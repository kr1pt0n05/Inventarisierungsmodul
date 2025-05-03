package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Order
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Order   {
  @JsonProperty("orders_id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer ordersId = null;

  @JsonProperty("orders_description")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String ordersDescription = null;

  @JsonProperty("orders_price")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Float ordersPrice = null;

  @JsonProperty("orders_company")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String ordersCompany = null;

  @JsonProperty("orders_ordered")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private BigDecimal ordersOrdered = null;


  public Order ordersId(Integer ordersId) { 

    this.ordersId = ordersId;
    return this;
  }

  /**
   * Get ordersId
   * @return ordersId
   **/
  
  @Schema(example = "5", description = "")
  
  public Integer getOrdersId() {  
    return ordersId;
  }



  public void setOrdersId(Integer ordersId) { 
    this.ordersId = ordersId;
  }

  public Order ordersDescription(String ordersDescription) { 

    this.ordersDescription = ordersDescription;
    return this;
  }

  /**
   * Get ordersDescription
   * @return ordersDescription
   **/
  
  @Schema(example = "order#2345632", description = "")
  
  public String getOrdersDescription() {  
    return ordersDescription;
  }



  public void setOrdersDescription(String ordersDescription) { 
    this.ordersDescription = ordersDescription;
  }

  public Order ordersPrice(Float ordersPrice) { 

    this.ordersPrice = ordersPrice;
    return this;
  }

  /**
   * Get ordersPrice
   * @return ordersPrice
   **/
  
  @Schema(example = "3556.64", description = "")
  
  public Float getOrdersPrice() {  
    return ordersPrice;
  }



  public void setOrdersPrice(Float ordersPrice) { 
    this.ordersPrice = ordersPrice;
  }

  public Order ordersCompany(String ordersCompany) { 

    this.ordersCompany = ordersCompany;
    return this;
  }

  /**
   * Get ordersCompany
   * @return ordersCompany
   **/
  
  @Schema(example = "Gedankenfabrik AG", description = "")
  
  public String getOrdersCompany() {  
    return ordersCompany;
  }



  public void setOrdersCompany(String ordersCompany) { 
    this.ordersCompany = ordersCompany;
  }

  public Order ordersOrdered(BigDecimal ordersOrdered) { 

    this.ordersOrdered = ordersOrdered;
    return this;
  }

  /**
   * Get ordersOrdered
   * @return ordersOrdered
   **/
  
  @Schema(description = "")
  
@Valid
  public BigDecimal getOrdersOrdered() {  
    return ordersOrdered;
  }



  public void setOrdersOrdered(BigDecimal ordersOrdered) { 
    this.ordersOrdered = ordersOrdered;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(this.ordersId, order.ordersId) &&
        Objects.equals(this.ordersDescription, order.ordersDescription) &&
        Objects.equals(this.ordersPrice, order.ordersPrice) &&
        Objects.equals(this.ordersCompany, order.ordersCompany) &&
        Objects.equals(this.ordersOrdered, order.ordersOrdered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ordersId, ordersDescription, ordersPrice, ordersCompany, ordersOrdered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Order {\n");
    
    sb.append("    ordersId: ").append(toIndentedString(ordersId)).append("\n");
    sb.append("    ordersDescription: ").append(toIndentedString(ordersDescription)).append("\n");
    sb.append("    ordersPrice: ").append(toIndentedString(ordersPrice)).append("\n");
    sb.append("    ordersCompany: ").append(toIndentedString(ordersCompany)).append("\n");
    sb.append("    ordersOrdered: ").append(toIndentedString(ordersOrdered)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
