package pcd.ass01.v1fix;

public interface Barrier {

    void await();

    boolean isBroken();

    void reset();
}
