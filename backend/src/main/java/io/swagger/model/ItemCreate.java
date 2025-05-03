package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ItemCreate
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class ItemCreate   {
  @JsonProperty("cost_center")

  private String costCenter = null;

  @JsonProperty("inventories_id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer inventoriesId = null;

  @JsonProperty("inventories_description")

  private String inventoriesDescription = null;

  @JsonProperty("company")

  private String company = null;

  @JsonProperty("inventories_price")

  private Float inventoriesPrice = null;

  @JsonProperty("inventories_serial_number")

  private String inventoriesSerialNumber = null;

  @JsonProperty("inventories_location")

  private String inventoriesLocation = null;

  @JsonProperty("orderer")

  private String orderer = null;

  @JsonProperty("tags")
  @Valid
  private List<Integer> tags = new ArrayList<Integer>();

  public ItemCreate costCenter(String costCenter) { 

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

  public ItemCreate inventoriesId(Integer inventoriesId) { 

    this.inventoriesId = inventoriesId;
    return this;
  }

  /**
   * Get inventoriesId
   * @return inventoriesId
   **/
  
  @Schema(example = "1", description = "")
  
  public Integer getInventoriesId() {  
    return inventoriesId;
  }



  public void setInventoriesId(Integer inventoriesId) { 
    this.inventoriesId = inventoriesId;
  }

  public ItemCreate inventoriesDescription(String inventoriesDescription) { 

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

  public ItemCreate company(String company) { 

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

  public ItemCreate inventoriesPrice(Float inventoriesPrice) { 

    this.inventoriesPrice = inventoriesPrice;
    return this;
  }

  /**
   * Get inventoriesPrice
   * @return inventoriesPrice
   **/
  
  @Schema(example = "1299.99", required = true, description = "")
  
  @NotNull
  public Float getInventoriesPrice() {  
    return inventoriesPrice;
  }



  public void setInventoriesPrice(Float inventoriesPrice) { 

    this.inventoriesPrice = inventoriesPrice;
  }

  public ItemCreate inventoriesSerialNumber(String inventoriesSerialNumber) { 

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

  public ItemCreate inventoriesLocation(String inventoriesLocation) { 

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

  public ItemCreate orderer(String orderer) { 

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

  public ItemCreate tags(List<Integer> tags) { 

    this.tags = tags;
    return this;
  }

  public ItemCreate addTagsItem(Integer tagsItem) {
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   **/
  
  @Schema(required = true, description = "")
  
  @NotNull
  public List<Integer> getTags() {  
    return tags;
  }



  public void setTags(List<Integer> tags) { 

    this.tags = tags;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemCreate itemCreate = (ItemCreate) o;
    return Objects.equals(this.costCenter, itemCreate.costCenter) &&
        Objects.equals(this.inventoriesId, itemCreate.inventoriesId) &&
        Objects.equals(this.inventoriesDescription, itemCreate.inventoriesDescription) &&
        Objects.equals(this.company, itemCreate.company) &&
        Objects.equals(this.inventoriesPrice, itemCreate.inventoriesPrice) &&
        Objects.equals(this.inventoriesSerialNumber, itemCreate.inventoriesSerialNumber) &&
        Objects.equals(this.inventoriesLocation, itemCreate.inventoriesLocation) &&
        Objects.equals(this.orderer, itemCreate.orderer) &&
        Objects.equals(this.tags, itemCreate.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costCenter, inventoriesId, inventoriesDescription, company, inventoriesPrice, inventoriesSerialNumber, inventoriesLocation, orderer, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemCreate {\n");
    
    sb.append("    costCenter: ").append(toIndentedString(costCenter)).append("\n");
    sb.append("    inventoriesId: ").append(toIndentedString(inventoriesId)).append("\n");
    sb.append("    inventoriesDescription: ").append(toIndentedString(inventoriesDescription)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
    sb.append("    inventoriesPrice: ").append(toIndentedString(inventoriesPrice)).append("\n");
    sb.append("    inventoriesSerialNumber: ").append(toIndentedString(inventoriesSerialNumber)).append("\n");
    sb.append("    inventoriesLocation: ").append(toIndentedString(inventoriesLocation)).append("\n");
    sb.append("    orderer: ").append(toIndentedString(orderer)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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
