package Observer;

// wzorzec Observer: informowanie o kliknięciu pola w celu śledzenia statystyk
public interface IClickedPublisher {

    void addObserver(IClickedObserver observer);

    void removeObserver(IClickedObserver observer);
}
