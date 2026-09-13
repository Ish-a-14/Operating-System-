import java.util.LinkedList;

public class PC {
    
        LinkedList<Integer> list = new LinkedList<>() ;
        int capacity = 2 ;
        
        synchronized void produce() throws InterruptedException{ 
            for(int val = 0 ; val < 10 ; val++){
                while(list.size()==capacity){ // Waking up doesn't guarantee that the buffer is still available when Producer gets the lock
                                              // Another thread may have changed the list before Producer gets the lock
                    System.out.println("Buffer is full->producer is waiting") ;
                    wait() ;
                }
                list.add(val) ;
                System.out.println("Producer : "+val) ;

                notify();
            }
        }
        synchronized void consume() throws InterruptedException{
           for(int i = 0 ; i < 10 ; i++){
                while(list.isEmpty()){
                    System.out.println("Buffer is empty->consumer is waiting") ;
                    wait() ;
                }
                int val = list.removeFirst() ;
                System.out.println("Consumed : "+val) ;

                notify() ;
            }
        }
    
    public static void main(String[] args){
        PC pc = new PC();
        
        Thread producer = new Thread(() -> {
            try{
                pc.produce() ;
            }
            catch(InterruptedException e){
                System.out.println(e) ;
            }
        });

        Thread consumer = new Thread(() -> {
            try{
                pc.consume() ;
            }
            catch(InterruptedException e){
                System.out.println(e) ;
            }
        });
        producer.start() ;
        consumer.start() ;
    }
}
