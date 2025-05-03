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
 * ExtensionsPatchField
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class ExtensionsPatchField   {
  @JsonProperty("field")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String field = null;

  @JsonProperty("value")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Object value = null;


  public ExtensionsPatchField field(String field) { 

    this.field = field;
    return this;
  }

  /**
   * Name des Felds, das geändert werden soll (z.B extensions_description, extensions_price)
   * @return field
   **/
  
  @Schema(example = "extensions_serial_number", description = "Name des Felds, das geändert werden soll (z.B extensions_description, extensions_price)")
  
  public String getField() {  
    return field;
  }



  public void setField(String field) { 
    this.field = field;
  }

  public ExtensionsPatchField value(Object value) { 

    this.value = value;
    return this;
  }

  /**
   * Neuer Wert für Attribut
   * @return value
   **/
  
  @Schema(example = "234HKF402", description = "Neuer Wert für Attribut")
  
  public Object getValue() {  
    return value;
  }



  public void setValue(Object value) { 
    this.value = value;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtensionsPatchField extensionsPatchField = (ExtensionsPatchField) o;
    return Objects.equals(this.field, extensionsPatchField.field) &&
        Objects.equals(this.value, extensionsPatchField.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtensionsPatchField {\n");
    
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
