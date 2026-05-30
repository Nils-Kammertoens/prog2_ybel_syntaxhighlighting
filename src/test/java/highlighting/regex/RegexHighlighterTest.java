package highlighting.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.util.List;
import org.junit.jupiter.api.Test;

class RegexHighlighterTest {

  @Test
  void emptyTextShouldProduceNoRegions() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();
    String text = "";

    // when
    List<HighlightRegion> regions = highlighter.computeRegions(text);

    // then
    assertTrue(regions.isEmpty());
  }

  @Test
  void simpleTextShouldProduceKeywordAndStringRegions() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();
    String text = "public String text = \"Hallo\";";

    // when
    List<HighlightRegion> regions = highlighter.computeRegions(text);

    // then
    assertTrue(regions.contains(new HighlightRegion(0, 6, MiniJavaColours.KEYWORD_COLOUR)));
    assertTrue(
        regions.contains(new HighlightRegion(21, 28, MiniJavaColours.STRING_LITERAL_COLOUR)));
  }

  @Test
  void keywordInsideLineCommentShouldBeRemovedByConflictResolution() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();
    String text = "// public class";

    // when
    List<HighlightRegion> regions = highlighter.computeRegions(text);

    // then
    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(0, 15, MiniJavaColours.LINE_COMMENT_COLOUR), regions.get(0));
  }

  @Test
  void commentSymbolsInsideStringShouldStayString() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();
    String text = "String url = \"https://example.com\";";

    // when
    List<HighlightRegion> regions = highlighter.computeRegions(text);

    // then
    assertEquals(1, regions.size());
    assertEquals(
        new HighlightRegion(13, 34, MiniJavaColours.STRING_LITERAL_COLOUR), regions.get(0));
  }

  @Test
  void javadocShouldWinOverBlockComment() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();
    String text = "/** docs */";

    // when
    List<HighlightRegion> regions = highlighter.computeRegions(text);

    // then
    assertEquals(1, regions.size());
    assertEquals(
        new HighlightRegion(0, 11, MiniJavaColours.JAVADOC_COMMENT_COLOUR), regions.get(0));
  }

  @Test
  void adjacentRegionsShouldNotOverlap() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();

    List<HighlightRegion> normalized =
        List.of(
            new HighlightRegion(0, 5, MiniJavaColours.KEYWORD_COLOUR),
            new HighlightRegion(5, 10, MiniJavaColours.STRING_LITERAL_COLOUR));

    // when
    List<HighlightRegion> resolved = highlighter.resolveConflicts(normalized);

    // then
    assertEquals(2, resolved.size());
    assertEquals(normalized, resolved);
  }

  @Test
  void overlappingRegionsShouldKeepFirstRegion() {
    // given
    RegexHighlighter highlighter = new RegexHighlighter();

    HighlightRegion first = new HighlightRegion(0, 10, MiniJavaColours.LINE_COMMENT_COLOUR);
    HighlightRegion second = new HighlightRegion(3, 8, MiniJavaColours.KEYWORD_COLOUR);

    List<HighlightRegion> normalized = List.of(first, second);

    // when
    List<HighlightRegion> resolved = highlighter.resolveConflicts(normalized);

    // then
    assertEquals(1, resolved.size());
    assertEquals(first, resolved.get(0));
  }
}
