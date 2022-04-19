public class LongRunningTask implements Runnable{
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Thread.sleep(50000);
            }
        } catch (InterruptedException e) {
            // log error
        }
    }
}
