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
 * AddTag
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class AddTag   {
  @JsonProperty("tags_id")
  @Valid
  private List<Integer> tagsId = null;

  public AddTag tagsId(List<Integer> tagsId) { 

    this.tagsId = tagsId;
    return this;
  }

  public AddTag addTagsIdItem(Integer tagsIdItem) {
    if (this.tagsId == null) {
      this.tagsId = new ArrayList<Integer>();
    }
    this.tagsId.add(tagsIdItem);
    return this;
  }

  /**
   * Get tagsId
   * @return tagsId
   **/
  
  @Schema(description = "")
  
  public List<Integer> getTagsId() {  
    return tagsId;
  }



  public void setTagsId(List<Integer> tagsId) { 
    this.tagsId = tagsId;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddTag addTag = (AddTag) o;
    return Objects.equals(this.tagsId, addTag.tagsId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagsId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddTag {\n");
    
    sb.append("    tagsId: ").append(toIndentedString(tagsId)).append("\n");
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
