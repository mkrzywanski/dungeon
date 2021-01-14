package parsing;

import java.util.List;

public interface InputParser {
    List<Entry> parse() throws ParsingException;
}
