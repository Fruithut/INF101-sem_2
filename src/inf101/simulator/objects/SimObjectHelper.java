package inf101.simulator.objects;

import inf101.simulator.Habitat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class supports the different SimObjects in finding each other given an object to search from,
 * a habitat to search in and a distanceLimit for the given search. This class was made to avoid duplicate
 * code and to achieve better flexibility.
 */
class SimObjectHelper {

    /**
     * Finds the average angle from the ISimObject to the meteors around it,
     * (limited by: distanceLimit-param in nearbyObjects-method)
     *
     * @return The average angle, converted from radians to degrees
     */
    static double getAverageMeteorAngle(ISimObject from, Habitat hab, double distanceLimit) {
        ArrayList<ISimObject> meteors = new ArrayList<>();
        double cosSum = 0, sinSum = 0;
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof SimMeteor)
                meteors.add(obj);
        }
        for (ISimObject x : meteors) {
            double radian = from.getPosition().directionTo(x.getPosition()).toRadians();
            cosSum += Math.cos(radian);
            sinSum += Math.sin(radian);
        }
        return Math.toDegrees(Math.atan2(sinSum, cosSum));
    }

    /**
     * Finds the average angle from the ISimObject to the SimHunters around it,
     * (limited by: distanceLimit-param in nearbyObjects-method)
     *
     * @return The average angle, converted from radians to degrees
     */
    static double getAverageHunterAngle(ISimObject from, Habitat hab, double distanceLimit) {
        ArrayList<ISimObject> hunters = new ArrayList<>();
        double cosSum = 0, sinSum = 0;
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof SimHunter)
                hunters.add(obj);
        }
        for (ISimObject x : hunters) {
            double radian = from.getPosition().directionTo(x.getPosition()).toRadians();
            cosSum += Math.cos(radian);
            sinSum += Math.sin(radian);
        }
        return Math.toDegrees(Math.atan2(sinSum, cosSum));
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimMeteor'-object
     *
     * @return the closest 'SimMeteor' in regards to a set radius
     */
    static SimMeteor getClosestMeteor(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof SimMeteor)
                return (SimMeteor) obj;
        }
        return null;
    }

    /**
     * Uses the pickupSorter class which implements the Comparator interface to sort
     * the IEdibleObjects from lowest to highest in regards to the item-value
     * of the objects.
     *
     * @return The SimGoldStar with the highest nutritional value
     */
    static IEdibleObject getBestGoldPickup(ISimObject from, Habitat hab, double distanceLimit) {
        ArrayList<IEdibleObject> pickupList = new ArrayList<>();

        class pickupSorter implements Comparator<IEdibleObject> {
            @Override
            public int compare(IEdibleObject o1, IEdibleObject o2) {
                if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) > 0) return 1;
                else if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) == 0) return 0;
                else return -1;
            }
        }

        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof IEdibleObject && obj instanceof SimGoldStar)
                pickupList.add((IEdibleObject) obj);
        }
        Collections.sort(pickupList, new pickupSorter());
        // return object with the largest energy-value
        return pickupList.get(pickupList.size() - 1);
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimGoldStar'-object
     *
     * @return the closest 'SimGoldStar' in regards to a set radius
     */
    static IEdibleObject getClosestGoldPickup(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof IEdibleObject && obj instanceof SimGoldStar)
                return (IEdibleObject) obj;
        }
        return null;
    }

    /**
     * Uses the pickupSorter class which implements the Comparator interface to sort
     * the IEdibleObjects from lowest to highest in regards to the item-value
     * of the objects.
     *
     * @return The SimSilverStar with the highest nutritional value
     */
    static IEdibleObject getBestSilverPickup(ISimObject from, Habitat hab, double distanceLimit) {
        ArrayList<IEdibleObject> pickupList = new ArrayList<>();

        class pickupSorter implements Comparator<IEdibleObject> {
            @Override
            public int compare(IEdibleObject o1, IEdibleObject o2) {
                if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) > 0) return 1;
                else if (Double.compare(o1.getNutritionalValue(), o2.getNutritionalValue()) == 0) return 0;
                else return -1;
            }
        }

        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof IEdibleObject && obj instanceof SimSilverStar)
                pickupList.add((IEdibleObject) obj);
        }
        Collections.sort(pickupList, new pickupSorter());
        // return object with the largest energy-value
        return pickupList.get(pickupList.size() - 1);
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimSilverStar'-object
     *
     * @return the closest 'SimSilverStar' in regards to a set radius
     */
    static IEdibleObject getClosestSilverPickup(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof IEdibleObject && obj instanceof SimSilverStar)
                return (IEdibleObject) obj;
        }
        return null;
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimPrey'-object
     *
     * @return the closest 'SimPrey' in regards to a set radius
     */
    static SimPrey getClosestPrey(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof SimPrey) {
                return (SimPrey) obj;
            }
        }
        return null;
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of a
     * 'SimHunter'-object
     *
     * @return the closest 'SimHunter' in regards to a set radius
     */
    static SimHunter getClosestHunter(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject obj : hab.nearbyObjects(from, from.getRadius() + distanceLimit)) {
            if (obj instanceof SimHunter) {
                return (SimHunter) obj;
            }
        }
        return null;
    }

    /**
     * Searches through all 'ISimObjects' in a list retrieved from 'habitat' in search of both
     * SimHunter-objects and SimPrey-objects, returns whichever it finds first
     *
     * @return the closest 'ISimObject' that is either a SimPrey or SimHunter in regards to a set radius
     */
    static ISimObject getClosestShip(ISimObject from, Habitat hab, double distanceLimit) {
        for (ISimObject object : hab.nearbyObjects(from, from.getRadius() + distanceLimit)){
            if (object instanceof SimHunter || object instanceof SimPrey) {
                return object;
            }
        }
        return null;
    }
}
