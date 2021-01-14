package parsing;

import data.DungeonFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StringInputParserTest {

    @Test
    void shouldParseValidStructure() throws ParsingException {
        List<String> lines = DungeonFixture.defaultDungeonFileLines();
        StringInputParser stringInputParser = new StringInputParser(lines);

        List<Entry> parse = stringInputParser.parse();

        assertThat(parse).hasSize(17);
    }

    @Test
    void shouldThrowExceptionWhenListIsNull() {
        StringInputParser stringInputParser = new StringInputParser(null);

        ThrowableAssert.ThrowingCallable callable = stringInputParser::parse;

        assertThatThrownBy(callable).hasMessageContaining("input lines is null or empty");
    }

    @Test
    void shouldThrowExceptionWhenListIsEmpty() {
        StringInputParser stringInputParser = new StringInputParser(new ArrayList<>());

        ThrowableAssert.ThrowingCallable callable = stringInputParser::parse;

        assertThatThrownBy(callable).hasMessageContaining("input lines is null or empty");

    }

    @Test
    void shouldThrowExceptionWhenOneOfTheLinesContainsNotEnoughData() {
        StringInputParser stringInputParser = new StringInputParser(List.of("a1"));

        ThrowableAssert.ThrowingCallable callable = stringInputParser::parse;

        assertThatThrownBy(callable).hasMessageContaining("Not enough fields, expecting minimum two fields in one entry");

    }

    @Test
    void shouldThrowExceptionWhenPassageIsIncorrect() {
        StringInputParser stringInputParser = new StringInputParser(List.of("a1 a2:"));

        ThrowableAssert.ThrowingCallable callable = stringInputParser::parse;

        assertThatThrownBy(callable).hasMessageContaining("Parsing entry failed. Cannot parse passage");

    }
}