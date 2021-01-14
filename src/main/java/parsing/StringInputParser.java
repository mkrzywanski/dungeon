package parsing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringInputParser implements InputParser {

    private final List<String> lines;

    public StringInputParser(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public List<Entry> parse() throws ParsingException {
        if(lines == null || lines.isEmpty()) {
            throw new ParsingException("input lines is null or empty");
        }
        return parseEntries();
    }

    private List<Entry> parseEntries() throws ParsingException {
        try {
            return lines.stream()
                    .map(s -> s.split(" "))
                    .map(mapper())
                    .collect(Collectors.toList());
        } catch (RuntimeException r) {
            throw new ParsingException(r.getCause());
        }
    }

    private ThrowingFunction<String[], Entry> mapper() {
        return unparsedEntry -> {
            if (unparsedEntry.length <= 1) {
                throw new ParsingException("Not enough fields, expecting minimum two fields in one entry");
            }
            String roomName = unparsedEntry[0];
            String[] unparsedPassages = Arrays.copyOfRange(unparsedEntry, 1, unparsedEntry.length);
            Set<Passage> passages = parsePassages(unparsedPassages);
            return new Entry(roomName, passages);
        };
    }

    private Set<Passage> parsePassages(String[] unparsedDirections) throws ParsingException {
        Set<Passage> passages = new HashSet<>();
        if (unparsedDirections.length != 0) {
            for (String unparsedDirection : unparsedDirections) {
                String[] directionAndDestination = unparsedDirection.split(":");
                if(directionAndDestination.length != 2) {
                    throw new ParsingException("Parsing entry failed. Cannot parse passage");
                }
                passages.add(new Passage(directionAndDestination[0], directionAndDestination[1]));
            }
        }
        return passages;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> extends Function<T, R> {
        @Override
        default R apply(final T elem) {
            try {
                return applyOrThrow(elem);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        R applyOrThrow(T elem) throws Exception;
    }

}
