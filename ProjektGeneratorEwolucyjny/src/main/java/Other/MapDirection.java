package Other;

// enum reprezentujący kiedunki świata, czyli orientację zwierzęcia
public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    // funkcja zwracającą kolejny kierunek idąc zgodnie ze wskazówkami zegara
    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    // funkcja zwracającą poprzedni kierunek idąc zgodnie ze wskazówkami zegara
    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHEAST -> NORTH;
            case EAST -> NORTHEAST;
            case SOUTHEAST -> EAST;
            case SOUTH -> SOUTHEAST;
            case SOUTHWEST -> SOUTH;
            case WEST -> SOUTHWEST;
            case NORTHWEST -> WEST;
        };
    }

    // funkcja zwracająca reprezentację wektorową kierunku
    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    @Override
    public String toString(){
        return switch (this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny Wschód";
            case EAST -> "Wschód";
            case SOUTHEAST -> "Południowy Wschód";
            case SOUTH -> "Południe";
            case SOUTHWEST -> "Południowy Zachód";
            case WEST -> "Zachód";
            case NORTHWEST -> "Północny Zachód";
        };
    }
}
