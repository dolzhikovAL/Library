import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Library {

    private Semaphore maxAmount;
    private Door door;
    int peopleCount;

    public Semaphore getMaxAmount() {
        return maxAmount;
    }


    public int getPeopleCount() {
        return peopleCount;
    }

    public Library(Semaphore maxAmount, int peopleCount) {
        this.door = new Door();
        this.maxAmount = maxAmount;
        this.peopleCount = peopleCount;
    }

    public void startTreads(Library librarySettings) {
        if (librarySettings.getMaxAmount().availablePermits() == 0) {
            System.out.println("Количечтво мест неправильное");
        } else treadsCreater(librarySettings);
    }

    public void treadsCreater(Library settings) {
        for (int i = 0; i < settings.getPeopleCount(); i++) {
            new Thread(new People(i)).start();
            System.out.println();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Thread has been interrupted");
            }
        }
    }

    public class People implements Runnable {

        private boolean outside;
        private int name;

        public People(int name) {
            this.name = name;
            this.outside = true;
        }

        @Override
        public void run() {

            System.out.println("Посетитель " + name + "  подошел к двери с улицы");
            try {
                while (outside) {
                    if (maxAmount.availablePermits() == 0) {
                        System.out.println("Посетитель " + name + " говорит:  В библиотеке нет свободного места стоим перед дверью ждем 2 сек");
                        TimeUnit.SECONDS.sleep(2);
                    } else {
                        maxAmount.acquire();
                        System.out.println("Посетитель " + name + " Говорит В библиотеке есть место готовимся пройти");
                        synchronized (door) {
                            goToTheDoor(outside, name);
                        }
                        outside = false;
                    }
                }
                int read = (int) Math.random() * 4 + 1;
                System.out.println("Посетитель " + name + " Читает книгу");
                TimeUnit.SECONDS.sleep(read);
                System.out.println("Посетитель " + name + " Дочитал подошел к двери");
                synchronized (door) {
                    goToTheDoor(outside, name);
                }
                System.out.println("Посетитель " + name + " Вышел на улицу");
                maxAmount.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToTheDoor(boolean outside, int name) {
        if (door.free) {
            door.free = false;
            if (outside) {
                try {
                    System.out.println("Посетитель " + name + " проходит В библиотеку");
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                door.free = true;
            } else {

                try {
                    System.out.println("Посетитель " + name + " проходит ИЗ библиотеки");
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                door.free = true;
            }
        }
    }

    public class Door {

        public Door() {
        }

        private boolean free;

    }
}




