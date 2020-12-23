package Observer;

// wzorzec Observer: obiekt obserwowany, zmianiający pozycję
public interface IPositionChangePublisher {

    void addObserver(IPositionChangeObserver observer);

    void removeObserver(IPositionChangeObserver observer);
}