package utilz;

public class Timer {
    private int timeLimit;  // in seconds
    private int currentTime;  // in seconds
    private boolean isActive;
    private long lastUpdateTime;

    public Timer(int timeLimit) {
        this.timeLimit = timeLimit;
        this.currentTime = timeLimit;
        this.isActive = false;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void start() {
        isActive = true;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void update() {
        if (isActive) {
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedTime = currentTimeMillis - lastUpdateTime;

            if (elapsedTime >= 1000) {  // 1 second has passed
                currentTime--;
                lastUpdateTime = currentTimeMillis;

                if (currentTime <= 0) {
                    currentTime = 0;
                    isActive = false;  // Timer stops when it reaches zero
                }
            }
        }
    }

    public void reset() {
        currentTime = timeLimit;
        isActive = false;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isActive() {
        return isActive;
    }
}
