package highlighting.presets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

    @Test
    void stringLiteralShouldMatchSimpleString() {
        // given
        Token stringToken =
            Token.of(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING_LITERAL_COLOUR);

        String text = "String name = \"Max\";";

        // when
        List<HighlightRegion> regions = stringToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(14, 19, MiniJavaColours.STRING_LITERAL_COLOUR),
            regions.get(0));
    }

    @Test
    void stringLiteralShouldMatchEmptyString() {
        // given
        Token stringToken =
            Token.of(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING_LITERAL_COLOUR);

        String text = "String empty = \"\";";

        // when
        List<HighlightRegion> regions = stringToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(15, 17, MiniJavaColours.STRING_LITERAL_COLOUR),
            regions.get(0));
    }

    @Test
    void stringLiteralShouldMatchEscapedQuotes() {
        // given
        Token stringToken =
            Token.of(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING_LITERAL_COLOUR);

        String text = "String quote = \"Hallo \\\"Max\\\"\";";

        // when
        List<HighlightRegion> regions = stringToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(15, 30, MiniJavaColours.STRING_LITERAL_COLOUR),
            regions.get(0));
    }

    @Test
    void stringLiteralShouldNotMatchUnclosedString() {
        // given
        Token stringToken =
            Token.of(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                MiniJavaColours.STRING_LITERAL_COLOUR);

        String text = "String broken = \"Hallo;";

        // when
        List<HighlightRegion> regions = stringToken.test(text);

        // then
        assertTrue(regions.isEmpty());
    }

    @Test
    void charLiteralShouldMatchSimpleCharacter() {
        // given
        Token charToken =
            Token.of(
                Pattern.compile("'([^'\\\\]|\\\\.)'"),
                MiniJavaColours.CHAR_LITERAL_COLOUR);

        String text = "char c = 'x';";

        // when
        List<HighlightRegion> regions = charToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(9, 12, MiniJavaColours.CHAR_LITERAL_COLOUR),
            regions.get(0));
    }

    @Test
    void charLiteralShouldMatchEscapedCharacter() {
        // given
        Token charToken =
            Token.of(
                Pattern.compile("'([^'\\\\]|\\\\.)'"),
                MiniJavaColours.CHAR_LITERAL_COLOUR);

        String text = "char lineBreak = '\\n';";

        // when
        List<HighlightRegion> regions = charToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(17, 21, MiniJavaColours.CHAR_LITERAL_COLOUR),
            regions.get(0));
    }

    @Test
    void lineCommentShouldMatchUntilLineEnd() {
        // given
        Token lineCommentToken =
            Token.of(
                Pattern.compile("//.*"),
                MiniJavaColours.LINE_COMMENT_COLOUR);

        String text = "int x; // return class";

        // when
        List<HighlightRegion> regions = lineCommentToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(7, 22, MiniJavaColours.LINE_COMMENT_COLOUR),
            regions.get(0));
    }

    @Test
    void javadocCommentShouldMatchAcrossMultipleLines() {
        // given
        Token javadocToken =
            Token.of(
                Pattern.compile("/\\*\\*[\\s\\S]*?\\*/"),
                MiniJavaColours.JAVADOC_COMMENT_COLOUR);

        String text = "/**\n * docs\n */";

        // when
        List<HighlightRegion> regions = javadocToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(0, 15, MiniJavaColours.JAVADOC_COMMENT_COLOUR),
            regions.get(0));
    }

    @Test
    void blockCommentShouldMatchAcrossMultipleLines() {
        // given
        Token blockCommentToken =
            Token.of(
                Pattern.compile("/\\*[\\s\\S]*?\\*/"),
                MiniJavaColours.BLOCK_COMMENT_COLOUR);

        String text = "a /* hello\nworld */ b";

        // when
        List<HighlightRegion> regions = blockCommentToken.test(text);

        // then
        assertEquals(1, regions.size());
        assertEquals(
            new HighlightRegion(2, 19, MiniJavaColours.BLOCK_COMMENT_COLOUR),
            regions.get(0));
    }

    @Test
    void keywordShouldMatchWholeWordsOnly() {
        // given
        Token keywordToken =
            Token.of(
                Pattern.compile("\\b(package|import|class|public|private|final|return|null|new)\\b"),
                MiniJavaColours.KEYWORD_COLOUR);

        String text = "public class Auto classification";

        // when
        List<HighlightRegion> regions = keywordToken.test(text);

        // then
        assertEquals(2, regions.size());
        assertEquals(
            new HighlightRegion(0, 6, MiniJavaColours.KEYWORD_COLOUR),
            regions.get(0));
        assertEquals(
            new HighlightRegion(7, 12, MiniJavaColours.KEYWORD_COLOUR),
            regions.get(1));
    }

    @Test
    void annotationShouldMatchAtSymbolWithLetters() {
        // given
        Token annotationToken =
            Token.of(
                Pattern.compile("@[A-Za-z-]+"),
                MiniJavaColours.ANNOTATION_COLOUR);

        String text = "@Override\n  @Test";

        // when
        List<HighlightRegion> regions = annotationToken.test(text);

        // then
        assertEquals(2, regions.size());
        assertEquals(
            new HighlightRegion(0, 9, MiniJavaColours.ANNOTATION_COLOUR),
            regions.get(0));
        assertEquals(
            new HighlightRegion(12, 17, MiniJavaColours.ANNOTATION_COLOUR),
            regions.get(1));
    }
    @Test
    void keywordShouldMatchAtStartMiddleAndEnd() {
        // given
        Token keywordToken =
            Token.of(
                Pattern.compile("\\b(package|import|class|public|private|final|return|null|new)\\b"),
                MiniJavaColours.KEYWORD_COLOUR);

        String text = "public Auto return";

        // when
        List<HighlightRegion> regions = keywordToken.test(text);

        // then
        assertEquals(2, regions.size());
        assertEquals(new HighlightRegion(0, 6, MiniJavaColours.KEYWORD_COLOUR), regions.get(0));
        assertEquals(new HighlightRegion(12, 18, MiniJavaColours.KEYWORD_COLOUR), regions.get(1));
    }
}
