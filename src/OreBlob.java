import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OreBlob extends  Movable{


    private final String QUAKE_KEY = "quake";
//    public static final String DYNAMITE_KEY = "dynamite";



    public OreBlob(

            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {

        super(id, position, images, actionPeriod, animationPeriod, 0);
    }



    public  boolean  _Movehelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else return false;
    }


    public void executeActivity(WorldModel world,
                                       ImageStore imageStore,
                                       EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(Vein.class, getPosition());
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition() ;

            if (moveTo( world, blobTarget.get(), scheduler)) {
                Quake quake = EntityFactory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

//                Dynamite quake = EntityFactory.createDynamite(tgtPos,
//                        imageStore.getImageList(DYNAMITE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                EntityFactory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }



    public  boolean _nextPositionHelper(WorldModel world, Point nextPos){
        Optional<Entity> occupant = world.getOccupant(nextPos);

        return  (((occupant.isPresent() && !(occupant.get() instanceof Ore))));

    }



}
