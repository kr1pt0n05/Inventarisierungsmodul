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
 * CommentRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-05-03T16:45:10.360954900+02:00[Europe/Berlin]")


public class CommentRequest   {
  @JsonProperty("comments_id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer commentsId = null;

  @JsonProperty("author")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String author = null;

  @JsonProperty("comments_description")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String commentsDescription = null;

  @JsonProperty("comments_created_at")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private BigDecimal commentsCreatedAt = null;


  public CommentRequest commentsId(Integer commentsId) { 

    this.commentsId = commentsId;
    return this;
  }

  /**
   * Get commentsId
   * @return commentsId
   **/
  
  @Schema(example = "23", description = "")
  
  public Integer getCommentsId() {  
    return commentsId;
  }



  public void setCommentsId(Integer commentsId) { 
    this.commentsId = commentsId;
  }

  public CommentRequest author(String author) { 

    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   **/
  
  @Schema(example = "Peter", description = "")
  
  public String getAuthor() {  
    return author;
  }



  public void setAuthor(String author) { 
    this.author = author;
  }

  public CommentRequest commentsDescription(String commentsDescription) { 

    this.commentsDescription = commentsDescription;
    return this;
  }

  /**
   * Get commentsDescription
   * @return commentsDescription
   **/
  
  @Schema(example = "Dieser Gegenstand wurde kommentiert", description = "")
  
  public String getCommentsDescription() {  
    return commentsDescription;
  }



  public void setCommentsDescription(String commentsDescription) { 
    this.commentsDescription = commentsDescription;
  }

  public CommentRequest commentsCreatedAt(BigDecimal commentsCreatedAt) { 

    this.commentsCreatedAt = commentsCreatedAt;
    return this;
  }

  /**
   * Get commentsCreatedAt
   * @return commentsCreatedAt
   **/
  
  @Schema(description = "")
  
@Valid
  public BigDecimal getCommentsCreatedAt() {  
    return commentsCreatedAt;
  }



  public void setCommentsCreatedAt(BigDecimal commentsCreatedAt) { 
    this.commentsCreatedAt = commentsCreatedAt;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentRequest commentRequest = (CommentRequest) o;
    return Objects.equals(this.commentsId, commentRequest.commentsId) &&
        Objects.equals(this.author, commentRequest.author) &&
        Objects.equals(this.commentsDescription, commentRequest.commentsDescription) &&
        Objects.equals(this.commentsCreatedAt, commentRequest.commentsCreatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentsId, author, commentsDescription, commentsCreatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommentRequest {\n");
    
    sb.append("    commentsId: ").append(toIndentedString(commentsId)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    commentsDescription: ").append(toIndentedString(commentsDescription)).append("\n");
    sb.append("    commentsCreatedAt: ").append(toIndentedString(commentsCreatedAt)).append("\n");
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
