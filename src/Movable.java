import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Movable extends Animated{

    protected PathingStrategy pathStrat;


    public Movable(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount) {
        super(id, position, images, actionPeriod, animationPeriod, repeatCount);
        pathStrat = new AStarPathingStrategy();
//        pathStrat = new SingleStepPathingStrategy();
    }

    public Movable(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount, PathingStrategy pathStrat) {
        super(id, position, images, actionPeriod, animationPeriod, repeatCount);
        this.pathStrat = pathStrat;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if(_Movehelper(world, target, scheduler)){
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity(this,  nextPos);
            }
            return false;
        }
    }


    protected abstract boolean _Movehelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);


//    public  Point nextPosition(WorldModel world, Point destPos)
//    {
//
//        List<Point> Path = pathStrat.computePath(getPosition(), destPos,
//                point -> world.withinBounds(point) && !world.isOccupied(point),
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS);
//
//
//        return Path.isEmpty() ? getPosition() : Path.get(0);
//
//    }

    public  Point nextPosition(WorldModel world, Point destPos)
    {
//        PathingStrategy pathStrat = new AStarPathingStrategy();


        List<Point> Path = pathStrat.computePath(getPosition(), destPos,
                point -> world.withinBounds(point) && !( _nextPositionHelper(world, point)),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);


        return Path.isEmpty() ? getPosition() : Path.get(0);

    }

    protected abstract  boolean _nextPositionHelper(WorldModel world, Point nextPos);


}
