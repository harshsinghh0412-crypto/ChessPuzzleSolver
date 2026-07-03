import java.util.HashMap;

public class TranspositionTable {
    private static final HashMap<Long, TTEntry> table = new HashMap<>();
    public static void clear() {
        table.clear();
    }
    public static TTEntry get(long hash) {
        return table.get(hash);
    }
    public static void put(TTEntry entry) {
        table.put(entry.hash, entry);
    }
    public static int size() {
        return table.size();
    }
}