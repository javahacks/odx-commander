package com.javahacks.odx.lsp.assertions;

/**
 * Entry point for soft assertions of different data types.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class SoftAssertions extends org.assertj.core.api.SoftAssertions {

  /**
   * Creates a new "soft" instance of <code>{@link com.javahacks.odx.lsp.dtos.DiagnosticElementAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public com.javahacks.odx.lsp.dtos.DiagnosticElementAssert assertThat(com.javahacks.odx.lsp.dtos.DiagnosticElement actual) {
    return proxy(com.javahacks.odx.lsp.dtos.DiagnosticElementAssert.class, com.javahacks.odx.lsp.dtos.DiagnosticElement.class, actual);
  }

}
