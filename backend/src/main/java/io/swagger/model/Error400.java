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
 * Error400
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class Error400   {
  @JsonProperty("code")

  private Integer code = null;

  @JsonProperty("message")

  private String message = null;


  public Error400 code(Integer code) { 

    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   **/
  
  @Schema(example = "400", required = true, description = "")
  
  @NotNull
  public Integer getCode() {  
    return code;
  }



  public void setCode(Integer code) { 

    this.code = code;
  }

  public Error400 message(String message) { 

    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   **/
  
  @Schema(example = "Bad Request", required = true, description = "")
  
  @NotNull
  public String getMessage() {  
    return message;
  }



  public void setMessage(String message) { 

    this.message = message;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error400 error400 = (Error400) o;
    return Objects.equals(this.code, error400.code) &&
        Objects.equals(this.message, error400.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error400 {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
