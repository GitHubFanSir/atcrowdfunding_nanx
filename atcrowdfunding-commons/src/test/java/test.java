public class test {

    static int ticket=30;


    public static void main(String[] args) {
        new Thread(new Seller()).start();
        new Thread(new Seller()).start();
        new Thread(new Seller()).start();

    }

}

class Seller implements Runnable{
    @Override
    public void run() {

        while(test.ticket>0){
            synchronized (Seller.class){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName()+"卖出了第"+test.ticket+"张票");
                test.ticket--;

            }
        }

    }
}
