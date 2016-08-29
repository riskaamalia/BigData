package kata;

/**
 * Created by 1224A on 8/25/2016.
 */
public class Player {
    public Player(int cakes) {
    }
    // Decide who move first - player or opponent (true if player)
    public boolean firstMove(int cakes) {
        // I wish to move first
        return true;
    }

    // Decide your next move
    public int move(int cakes, int last) {
        // I'm not greedy
        return last == 1 ? 2 : 1;
    }
}
