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
 * History
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class History   {
  @JsonProperty("histories_attribute_changed")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String historiesAttributeChanged = null;

  @JsonProperty("histories_from")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Object historiesFrom = null;

  @JsonProperty("histories_to")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Object historiesTo = null;

  @JsonProperty("histories_date")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private BigDecimal historiesDate = null;

  @JsonProperty("author")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Object author = null;


  public History historiesAttributeChanged(String historiesAttributeChanged) { 

    this.historiesAttributeChanged = historiesAttributeChanged;
    return this;
  }

  /**
   * Get historiesAttributeChanged
   * @return historiesAttributeChanged
   **/
  
  @Schema(example = "location", description = "")
  
  public String getHistoriesAttributeChanged() {  
    return historiesAttributeChanged;
  }



  public void setHistoriesAttributeChanged(String historiesAttributeChanged) { 
    this.historiesAttributeChanged = historiesAttributeChanged;
  }

  public History historiesFrom(Object historiesFrom) { 

    this.historiesFrom = historiesFrom;
    return this;
  }

  /**
   * Get historiesFrom
   * @return historiesFrom
   **/
  
  @Schema(example = "F01.352", description = "")
  
  public Object getHistoriesFrom() {  
    return historiesFrom;
  }



  public void setHistoriesFrom(Object historiesFrom) { 
    this.historiesFrom = historiesFrom;
  }

  public History historiesTo(Object historiesTo) { 

    this.historiesTo = historiesTo;
    return this;
  }

  /**
   * Get historiesTo
   * @return historiesTo
   **/
  
  @Schema(example = "F01.123", description = "")
  
  public Object getHistoriesTo() {  
    return historiesTo;
  }



  public void setHistoriesTo(Object historiesTo) { 
    this.historiesTo = historiesTo;
  }

  public History historiesDate(BigDecimal historiesDate) { 

    this.historiesDate = historiesDate;
    return this;
  }

  /**
   * Get historiesDate
   * @return historiesDate
   **/
  
  @Schema(description = "")
  
@Valid
  public BigDecimal getHistoriesDate() {  
    return historiesDate;
  }



  public void setHistoriesDate(BigDecimal historiesDate) { 
    this.historiesDate = historiesDate;
  }

  public History author(Object author) { 

    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   **/
  
  @Schema(example = "Peter", description = "")
  
  public Object getAuthor() {  
    return author;
  }



  public void setAuthor(Object author) { 
    this.author = author;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    History history = (History) o;
    return Objects.equals(this.historiesAttributeChanged, history.historiesAttributeChanged) &&
        Objects.equals(this.historiesFrom, history.historiesFrom) &&
        Objects.equals(this.historiesTo, history.historiesTo) &&
        Objects.equals(this.historiesDate, history.historiesDate) &&
        Objects.equals(this.author, history.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(historiesAttributeChanged, historiesFrom, historiesTo, historiesDate, author);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class History {\n");
    
    sb.append("    historiesAttributeChanged: ").append(toIndentedString(historiesAttributeChanged)).append("\n");
    sb.append("    historiesFrom: ").append(toIndentedString(historiesFrom)).append("\n");
    sb.append("    historiesTo: ").append(toIndentedString(historiesTo)).append("\n");
    sb.append("    historiesDate: ").append(toIndentedString(historiesDate)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
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
