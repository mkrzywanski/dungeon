package game;

public enum Directions {
    N,
    S,
    E,
    W;

    public static Directions from(final String input) {
        String upperCase = input.toUpperCase();
        switch (upperCase) {
            case "N" : return Directions.N;
            case "S" : return Directions.S;
            case "E" : return Directions.E;
            case "W" : return Directions.W;
            default: throw new RuntimeException("Cannot map " + input);
        }
    }
}
