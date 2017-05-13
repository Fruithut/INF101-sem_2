package inf101.simulator.objects;

import inf101.simulator.SimMain;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;

/**
 * A primitive implementation for adding extra sound effects to the different objects
 * Number index used because of the small set of files needed for this particular program.
 * Derived from BDSounds in my last term-test (sem1)
 */
public class SimSounds {
    private static ArrayList<AudioClip> soundList;

    public SimSounds() {
        SimMain.setSound(true);
        soundList = new ArrayList<>();
        try {
            AudioClip type0Laser = new AudioClip(getClass().getResource("../sounds/laser1.wav").toString());
            AudioClip type1Laser = new AudioClip(getClass().getResource("../sounds/laser2.wav").toString());
            AudioClip type0Meteor = new AudioClip(getClass().getResource("../sounds/meteorHit1.wav").toString());
            AudioClip type1Meteor = new AudioClip(getClass().getResource("../sounds/meteorHit2.wav").toString());
            AudioClip meteorExplode = new AudioClip(getClass().getResource("../sounds/meteorExplode.wav").toString());
            AudioClip type0LaserFailure = new AudioClip(getClass().getResource("../sounds/laser1Failure.mp3").toString());
            AudioClip preyCrash = new AudioClip(getClass().getResource("../sounds/preyCrash.wav").toString());
            AudioClip hunterCrash = new AudioClip(getClass().getResource("../sounds/hunterCrash.wav").toString());
            AudioClip meteorHitShip = new AudioClip(getClass().getResource("../sounds/meteorHitShip.wav").toString());
            AudioClip ufoPassby = new AudioClip(getClass().getResource("../sounds/ufoPassing.mp3").toString());
            
            //Adjust volume
            type0Laser.setVolume(0.25);
            type1Laser.setVolume(0.45);
            type0Meteor.setVolume(0.3);
            type1Meteor.setVolume(0.3);
            meteorExplode.setVolume(0.4);
            preyCrash.setVolume(0.1);
            hunterCrash.setVolume(0.2);
            meteorHitShip.setVolume(0.8);

            soundList.add(type0Laser);
            soundList.add(type1Laser);
            soundList.add(type0Meteor);
            soundList.add(type1Meteor);
            soundList.add(meteorExplode);
            soundList.add(type0LaserFailure);
            soundList.add(preyCrash);
            soundList.add(hunterCrash);
            soundList.add(meteorHitShip);
            soundList.add(ufoPassby);
        } catch (Exception e) {
            System.out.println("An sfx-file is missing or has been altered!");
            System.out.println("Cancel out sfx from 'Setup' by commenting out SimSounds");
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * @param fileNumber index of the sound-file
     * @returns An audioclip from the arraylist
     */
    static AudioClip getSound(int fileNumber) {
        //Size limitation on get method
        if (fileNumber > soundList.size()-1) {
            fileNumber = soundList.size()-1;
        } else if (fileNumber < 0) {
            fileNumber = 0;
        }
        return soundList.get(fileNumber);
    }
}
