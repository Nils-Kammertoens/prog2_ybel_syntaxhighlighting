package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  public static List<Token> defaultTokens() {
    return List.of(
        // Strings
        Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),
        // Javadoc‑Kommentare
        Token.of(Pattern.compile("/\\*\\*[\\s\\S]*?\\*/"), MiniJavaColours.JAVADOC_COMMENT_COLOUR),
        // Mehrzeilige Kommentare
        Token.of(Pattern.compile("/\\*[\\s\\S]*?\\*/"), MiniJavaColours.BLOCK_COMMENT_COLOUR),
        // Einzeilige Kommentare
        Token.of(Pattern.compile("//.*"), MiniJavaColours.LINE_COMMENT_COLOUR),
        // Characters
        Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),
        // Keywords
        Token.of(
            Pattern.compile(
                "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue"
                    + "|default|do|double|else|enum|extends|final|finally|float|for|goto|if"
                    + "|implements|import|instanceof|int|interface|long|native|new|package"
                    + "|private|protected|public|return|short|static|strictfp|super|switch"
                    + "|synchronized|this|throw|throws|transient|try|void|volatile|while"
                    + "|true|false|null|var|record|sealed|yield)\\b"),
            MiniJavaColours.KEYWORD_COLOUR),
        // Annotations
        Token.of(Pattern.compile("@[A-Za-z-]+"), MiniJavaColours.ANNOTATION_COLOUR));
  }
}
