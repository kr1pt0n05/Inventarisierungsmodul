package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Item
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Item   {
  @JsonProperty("cost_center")

  private String costCenter = null;

  @JsonProperty("inventories_id")

  private Integer inventoriesId = null;

  @JsonProperty("inventories_description")

  private String inventoriesDescription = null;

  @JsonProperty("company")

  private String company = null;

  @JsonProperty("inventories_price")

  private Float inventoriesPrice = null;

  @JsonProperty("inventories_created_at")

  private OffsetDateTime inventoriesCreatedAt = null;

  @JsonProperty("inventories_serial_number")

  private String inventoriesSerialNumber = null;

  @JsonProperty("inventories_location")

  private String inventoriesLocation = null;

  @JsonProperty("orderer")

  private String orderer = null;


  public Item costCenter(String costCenter) { 

    this.costCenter = costCenter;
    return this;
  }

  /**
   * Get costCenter
   * @return costCenter
   **/
  
  @Schema(example = "723458320Z", required = true, description = "")
  
  @NotNull
  public String getCostCenter() {  
    return costCenter;
  }



  public void setCostCenter(String costCenter) { 

    this.costCenter = costCenter;
  }

  public Item inventoriesId(Integer inventoriesId) { 

    this.inventoriesId = inventoriesId;
    return this;
  }

  /**
   * Get inventoriesId
   * @return inventoriesId
   **/
  
  @Schema(example = "1", required = true, description = "")
  
  @NotNull
  public Integer getInventoriesId() {  
    return inventoriesId;
  }



  public void setInventoriesId(Integer inventoriesId) { 

    this.inventoriesId = inventoriesId;
  }

  public Item inventoriesDescription(String inventoriesDescription) { 

    this.inventoriesDescription = inventoriesDescription;
    return this;
  }

  /**
   * Get inventoriesDescription
   * @return inventoriesDescription
   **/
  
  @Schema(example = "Asus Gaming Laptop", required = true, description = "")
  
  @NotNull
  public String getInventoriesDescription() {  
    return inventoriesDescription;
  }



  public void setInventoriesDescription(String inventoriesDescription) { 

    this.inventoriesDescription = inventoriesDescription;
  }

  public Item company(String company) { 

    this.company = company;
    return this;
  }

  /**
   * Get company
   * @return company
   **/
  
  @Schema(example = "Gedankenfabrik AG", required = true, description = "")
  
  @NotNull
  public String getCompany() {  
    return company;
  }



  public void setCompany(String company) { 

    this.company = company;
  }

  public Item inventoriesPrice(Float inventoriesPrice) { 

    this.inventoriesPrice = inventoriesPrice;
    return this;
  }

  /**
   * Get inventoriesPrice
   * @return inventoriesPrice
   **/
  
  @Schema(required = true, description = "")
  
  @NotNull
  public Float getInventoriesPrice() {  
    return inventoriesPrice;
  }



  public void setInventoriesPrice(Float inventoriesPrice) { 

    this.inventoriesPrice = inventoriesPrice;
  }

  public Item inventoriesCreatedAt(OffsetDateTime inventoriesCreatedAt) { 

    this.inventoriesCreatedAt = inventoriesCreatedAt;
    return this;
  }

  /**
   * Get inventoriesCreatedAt
   * @return inventoriesCreatedAt
   **/
  
  @Schema(required = true, description = "")
  
@Valid
  @NotNull
  public OffsetDateTime getInventoriesCreatedAt() {  
    return inventoriesCreatedAt;
  }



  public void setInventoriesCreatedAt(OffsetDateTime inventoriesCreatedAt) { 

    this.inventoriesCreatedAt = inventoriesCreatedAt;
  }

  public Item inventoriesSerialNumber(String inventoriesSerialNumber) { 

    this.inventoriesSerialNumber = inventoriesSerialNumber;
    return this;
  }

  /**
   * Get inventoriesSerialNumber
   * @return inventoriesSerialNumber
   **/
  
  @Schema(example = "834Z32T32H", required = true, description = "")
  
  @NotNull
  public String getInventoriesSerialNumber() {  
    return inventoriesSerialNumber;
  }



  public void setInventoriesSerialNumber(String inventoriesSerialNumber) { 

    this.inventoriesSerialNumber = inventoriesSerialNumber;
  }

  public Item inventoriesLocation(String inventoriesLocation) { 

    this.inventoriesLocation = inventoriesLocation;
    return this;
  }

  /**
   * Get inventoriesLocation
   * @return inventoriesLocation
   **/
  
  @Schema(example = "F99.234", required = true, description = "")
  
  @NotNull
  public String getInventoriesLocation() {  
    return inventoriesLocation;
  }



  public void setInventoriesLocation(String inventoriesLocation) { 

    this.inventoriesLocation = inventoriesLocation;
  }

  public Item orderer(String orderer) { 

    this.orderer = orderer;
    return this;
  }

  /**
   * Get orderer
   * @return orderer
   **/
  
  @Schema(example = "Peter", required = true, description = "")
  
  @NotNull
  public String getOrderer() {  
    return orderer;
  }



  public void setOrderer(String orderer) { 

    this.orderer = orderer;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(this.costCenter, item.costCenter) &&
        Objects.equals(this.inventoriesId, item.inventoriesId) &&
        Objects.equals(this.inventoriesDescription, item.inventoriesDescription) &&
        Objects.equals(this.company, item.company) &&
        Objects.equals(this.inventoriesPrice, item.inventoriesPrice) &&
        Objects.equals(this.inventoriesCreatedAt, item.inventoriesCreatedAt) &&
        Objects.equals(this.inventoriesSerialNumber, item.inventoriesSerialNumber) &&
        Objects.equals(this.inventoriesLocation, item.inventoriesLocation) &&
        Objects.equals(this.orderer, item.orderer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costCenter, inventoriesId, inventoriesDescription, company, inventoriesPrice, inventoriesCreatedAt, inventoriesSerialNumber, inventoriesLocation, orderer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Item {\n");
    
    sb.append("    costCenter: ").append(toIndentedString(costCenter)).append("\n");
    sb.append("    inventoriesId: ").append(toIndentedString(inventoriesId)).append("\n");
    sb.append("    inventoriesDescription: ").append(toIndentedString(inventoriesDescription)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
    sb.append("    inventoriesPrice: ").append(toIndentedString(inventoriesPrice)).append("\n");
    sb.append("    inventoriesCreatedAt: ").append(toIndentedString(inventoriesCreatedAt)).append("\n");
    sb.append("    inventoriesSerialNumber: ").append(toIndentedString(inventoriesSerialNumber)).append("\n");
    sb.append("    inventoriesLocation: ").append(toIndentedString(inventoriesLocation)).append("\n");
    sb.append("    orderer: ").append(toIndentedString(orderer)).append("\n");
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
