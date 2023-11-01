package com.javahacks.odx.lsp.assertions;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class Assertions {

  /**
   * Creates a new instance of <code>{@link com.javahacks.odx.lsp.dtos.DiagnosticElementAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static com.javahacks.odx.lsp.dtos.DiagnosticElementAssert assertThat(com.javahacks.odx.lsp.dtos.DiagnosticElement actual) {
    return new com.javahacks.odx.lsp.dtos.DiagnosticElementAssert(actual);
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }
}
