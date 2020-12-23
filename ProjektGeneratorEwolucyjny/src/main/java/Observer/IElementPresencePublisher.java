package Observer;

// wzorzec Observer: obiekt informujący o pojawieniu się lub usunięciu elementu z mapy
public interface IElementPresencePublisher {

    void addObserver(IElementPresenceObserver observer);

    void removeObserver(IElementPresenceObserver observer);
}
