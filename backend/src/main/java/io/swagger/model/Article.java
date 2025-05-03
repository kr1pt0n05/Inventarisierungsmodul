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
 * Article
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Article   {
  @JsonProperty("articles_order_id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer articlesOrderId = null;

  @JsonProperty("articles_description")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String articlesDescription = null;

  @JsonProperty("articles_serial_number")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String articlesSerialNumber = null;

  @JsonProperty("articles_price")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Float articlesPrice = null;


  public Article articlesOrderId(Integer articlesOrderId) { 

    this.articlesOrderId = articlesOrderId;
    return this;
  }

  /**
   * Get articlesOrderId
   * @return articlesOrderId
   **/
  
  @Schema(example = "6", description = "")
  
  public Integer getArticlesOrderId() {  
    return articlesOrderId;
  }



  public void setArticlesOrderId(Integer articlesOrderId) { 
    this.articlesOrderId = articlesOrderId;
  }

  public Article articlesDescription(String articlesDescription) { 

    this.articlesDescription = articlesDescription;
    return this;
  }

  /**
   * Get articlesDescription
   * @return articlesDescription
   **/
  
  @Schema(example = "Mega-Giga-RAM", description = "")
  
  public String getArticlesDescription() {  
    return articlesDescription;
  }



  public void setArticlesDescription(String articlesDescription) { 
    this.articlesDescription = articlesDescription;
  }

  public Article articlesSerialNumber(String articlesSerialNumber) { 

    this.articlesSerialNumber = articlesSerialNumber;
    return this;
  }

  /**
   * Get articlesSerialNumber
   * @return articlesSerialNumber
   **/
  
  @Schema(example = "234HKL523", description = "")
  
  public String getArticlesSerialNumber() {  
    return articlesSerialNumber;
  }



  public void setArticlesSerialNumber(String articlesSerialNumber) { 
    this.articlesSerialNumber = articlesSerialNumber;
  }

  public Article articlesPrice(Float articlesPrice) { 

    this.articlesPrice = articlesPrice;
    return this;
  }

  /**
   * Get articlesPrice
   * @return articlesPrice
   **/
  
  @Schema(example = "323.65", description = "")
  
  public Float getArticlesPrice() {  
    return articlesPrice;
  }



  public void setArticlesPrice(Float articlesPrice) { 
    this.articlesPrice = articlesPrice;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Article article = (Article) o;
    return Objects.equals(this.articlesOrderId, article.articlesOrderId) &&
        Objects.equals(this.articlesDescription, article.articlesDescription) &&
        Objects.equals(this.articlesSerialNumber, article.articlesSerialNumber) &&
        Objects.equals(this.articlesPrice, article.articlesPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(articlesOrderId, articlesDescription, articlesSerialNumber, articlesPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Article {\n");
    
    sb.append("    articlesOrderId: ").append(toIndentedString(articlesOrderId)).append("\n");
    sb.append("    articlesDescription: ").append(toIndentedString(articlesDescription)).append("\n");
    sb.append("    articlesSerialNumber: ").append(toIndentedString(articlesSerialNumber)).append("\n");
    sb.append("    articlesPrice: ").append(toIndentedString(articlesPrice)).append("\n");
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
