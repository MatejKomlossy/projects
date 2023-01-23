package Game;

import java.util.ArrayList;
import java.util.List;

public abstract class Publisher {

    List<Subscriber> subscribers;

    public Publisher() {
        subscribers = new ArrayList<>();
    }

    public void notifySubscribers(String event, int value){
        for (Subscriber subscriber: subscribers) {
            subscriber.update(event, value);
        }
    }

    public void subscribe(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber){
        subscribers.remove(subscriber);
    }
}
