package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Extension
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Extension   {
  @JsonProperty("extensions_inventory_id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer extensionsInventoryId = null;

  @JsonProperty("extensions_description")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String extensionsDescription = null;

  @JsonProperty("extensions_serial_number")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String extensionsSerialNumber = null;

  @JsonProperty("extensions_price")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Float extensionsPrice = null;

  @JsonProperty("extensions_created_at")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String extensionsCreatedAt = null;

  @JsonProperty("company")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String company = null;


  public Extension extensionsInventoryId(Integer extensionsInventoryId) { 

    this.extensionsInventoryId = extensionsInventoryId;
    return this;
  }

  /**
   * Get extensionsInventoryId
   * @return extensionsInventoryId
   **/
  
  @Schema(example = "23498", description = "")
  
  public Integer getExtensionsInventoryId() {  
    return extensionsInventoryId;
  }



  public void setExtensionsInventoryId(Integer extensionsInventoryId) { 
    this.extensionsInventoryId = extensionsInventoryId;
  }

  public Extension extensionsDescription(String extensionsDescription) { 

    this.extensionsDescription = extensionsDescription;
    return this;
  }

  /**
   * Get extensionsDescription
   * @return extensionsDescription
   **/
  
  @Schema(example = "G.Skill Aegis DDR4", description = "")
  
  public String getExtensionsDescription() {  
    return extensionsDescription;
  }



  public void setExtensionsDescription(String extensionsDescription) { 
    this.extensionsDescription = extensionsDescription;
  }

  public Extension extensionsSerialNumber(String extensionsSerialNumber) { 

    this.extensionsSerialNumber = extensionsSerialNumber;
    return this;
  }

  /**
   * Get extensionsSerialNumber
   * @return extensionsSerialNumber
   **/
  
  @Schema(example = "344F6534G", description = "")
  
  public String getExtensionsSerialNumber() {  
    return extensionsSerialNumber;
  }



  public void setExtensionsSerialNumber(String extensionsSerialNumber) { 
    this.extensionsSerialNumber = extensionsSerialNumber;
  }

  public Extension extensionsPrice(Float extensionsPrice) { 

    this.extensionsPrice = extensionsPrice;
    return this;
  }

  /**
   * Get extensionsPrice
   * @return extensionsPrice
   **/
  
  @Schema(example = "25.95", description = "")
  
  public Float getExtensionsPrice() {  
    return extensionsPrice;
  }



  public void setExtensionsPrice(Float extensionsPrice) { 
    this.extensionsPrice = extensionsPrice;
  }

  public Extension extensionsCreatedAt(String extensionsCreatedAt) { 

    this.extensionsCreatedAt = extensionsCreatedAt;
    return this;
  }

  /**
   * Get extensionsCreatedAt
   * @return extensionsCreatedAt
   **/
  
  @Schema(example = "11.09.2001", description = "")
  
  public String getExtensionsCreatedAt() {  
    return extensionsCreatedAt;
  }



  public void setExtensionsCreatedAt(String extensionsCreatedAt) { 
    this.extensionsCreatedAt = extensionsCreatedAt;
  }

  public Extension company(String company) { 

    this.company = company;
    return this;
  }

  /**
   * Get company
   * @return company
   **/
  
  @Schema(example = "Gedankenfabrik AG", description = "")
  
  public String getCompany() {  
    return company;
  }



  public void setCompany(String company) { 
    this.company = company;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Extension extension = (Extension) o;
    return Objects.equals(this.extensionsInventoryId, extension.extensionsInventoryId) &&
        Objects.equals(this.extensionsDescription, extension.extensionsDescription) &&
        Objects.equals(this.extensionsSerialNumber, extension.extensionsSerialNumber) &&
        Objects.equals(this.extensionsPrice, extension.extensionsPrice) &&
        Objects.equals(this.extensionsCreatedAt, extension.extensionsCreatedAt) &&
        Objects.equals(this.company, extension.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extensionsInventoryId, extensionsDescription, extensionsSerialNumber, extensionsPrice, extensionsCreatedAt, company);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Extension {\n");
    
    sb.append("    extensionsInventoryId: ").append(toIndentedString(extensionsInventoryId)).append("\n");
    sb.append("    extensionsDescription: ").append(toIndentedString(extensionsDescription)).append("\n");
    sb.append("    extensionsSerialNumber: ").append(toIndentedString(extensionsSerialNumber)).append("\n");
    sb.append("    extensionsPrice: ").append(toIndentedString(extensionsPrice)).append("\n");
    sb.append("    extensionsCreatedAt: ").append(toIndentedString(extensionsCreatedAt)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
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
