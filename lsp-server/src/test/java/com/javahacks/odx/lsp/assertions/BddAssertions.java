package com.javahacks.odx.lsp.assertions;

/**
 * Entry point for BDD assertions of different data types.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class BddAssertions {

  /**
   * Creates a new instance of <code>{@link com.javahacks.odx.lsp.dtos.DiagnosticElementAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static com.javahacks.odx.lsp.dtos.DiagnosticElementAssert then(com.javahacks.odx.lsp.dtos.DiagnosticElement actual) {
    return new com.javahacks.odx.lsp.dtos.DiagnosticElementAssert(actual);
  }

  /**
   * Creates a new <code>{@link BddAssertions}</code>.
   */
  protected BddAssertions() {
    // empty
  }
}
