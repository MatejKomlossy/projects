package Game;

public interface Mediator {

    <T extends Component> void notify(T sender, String event) throws EventException;
}
