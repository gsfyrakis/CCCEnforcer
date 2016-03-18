package uk.ac.ncl.state;

public interface RopState {
    enum RightState implements RopState {
        granted, executing, violated;
    }

    enum ObligationState implements RopState {
        imposed, executing, fulfilled, violated;
    }

    enum ProhibitionState implements RopState {
        imposed, executing, expired, violated, fulfilled;
    }
}
