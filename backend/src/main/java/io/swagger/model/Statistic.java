package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.StatisticNames;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Statistic
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Statistic   {
  @JsonProperty("total_orders")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer totalOrders = null;

  @JsonProperty("total_price")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Float totalPrice = null;

  @JsonProperty("names")
  @Valid
  private List<StatisticNames> names = null;

  public Statistic totalOrders(Integer totalOrders) { 

    this.totalOrders = totalOrders;
    return this;
  }

  /**
   * Get totalOrders
   * @return totalOrders
   **/
  
  @Schema(example = "53", description = "")
  
  public Integer getTotalOrders() {  
    return totalOrders;
  }



  public void setTotalOrders(Integer totalOrders) { 
    this.totalOrders = totalOrders;
  }

  public Statistic totalPrice(Float totalPrice) { 

    this.totalPrice = totalPrice;
    return this;
  }

  /**
   * Get totalPrice
   * @return totalPrice
   **/
  
  @Schema(example = "12345.67", description = "")
  
  public Float getTotalPrice() {  
    return totalPrice;
  }



  public void setTotalPrice(Float totalPrice) { 
    this.totalPrice = totalPrice;
  }

  public Statistic names(List<StatisticNames> names) { 

    this.names = names;
    return this;
  }

  public Statistic addNamesItem(StatisticNames namesItem) {
    if (this.names == null) {
      this.names = new ArrayList<StatisticNames>();
    }
    this.names.add(namesItem);
    return this;
  }

  /**
   * Get names
   * @return names
   **/
  
  @Schema(description = "")
  @Valid
  public List<StatisticNames> getNames() {  
    return names;
  }



  public void setNames(List<StatisticNames> names) { 
    this.names = names;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Statistic statistic = (Statistic) o;
    return Objects.equals(this.totalOrders, statistic.totalOrders) &&
        Objects.equals(this.totalPrice, statistic.totalPrice) &&
        Objects.equals(this.names, statistic.names);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalOrders, totalPrice, names);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Statistic {\n");
    
    sb.append("    totalOrders: ").append(toIndentedString(totalOrders)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    names: ").append(toIndentedString(names)).append("\n");
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
