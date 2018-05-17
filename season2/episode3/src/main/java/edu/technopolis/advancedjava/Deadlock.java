package edu.technopolis.advancedjava;

public class Deadlock {
    private static final Object FIRST_LOCK = new Object();
    private static final Object SECOND_LOCK = new Object();
    private static final DeadlockBarrier barrier = new DeadlockBarrier(2);

    public static void main(String[] args) throws Exception {
        Thread ft = new Thread(Deadlock::first);
        Thread st = new Thread(Deadlock::second);
        ft.start();
        st.start();
        ft.join();
        st.join();
        //never going to reach this point
    }

    private static void first() {
        synchronized(FIRST_LOCK) {
            barrier.passBarrier();
            synchronized(SECOND_LOCK) {
                //unreachable point
            }
        }
    }

    private static void second() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }
        //reverse order of monitors
        synchronized(SECOND_LOCK) {
            barrier.passBarrier();
            synchronized(FIRST_LOCK) {
                //unreachable point
            }
        }

    }

}