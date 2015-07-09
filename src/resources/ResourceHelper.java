package resources;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

public class ResourceHelper {

    public static String[] CONTACT_IMAGE_LIST = {
        "contactimages/Actor_Female_Dark.png",
        "contactimages/Actor_Male_Dark.png",
        "contactimages/ade_person_face-128.png",
        "contactimages/ArmyNurse_Female_Dark.png",
        "contactimages/ArmyNurse_Male_Dark.png",
        "contactimages/Barbarian_Female.png",
        "contactimages/Barbarian_Male.png",
        "contactimages/Bartender_Female_Dark.png",
        "contactimages/Bartender_Male_Dark.png",
        "contactimages/Ben_man-128.png",
        "contactimages/carla_girl-128.png",
        "contactimages/Child_Female_Dark.png",
        "contactimages/Child_Male_Dark.png",
        "contactimages/coco_moustache-128.png",
        "contactimages/Cowboy.png",
        "contactimages/Cowgirl.png",
        "contactimages/FilmMaker_Female_Dark.png",
        "contactimages/FilmMaker_Male_Dark.png",
        "contactimages/Firefighter_Female_Dark.png",
        "contactimages/Firefighter_Male_Dark.png",
        "contactimages/Fred_man-128.png",
        "contactimages/Gavroche_face-128.png",
        "contactimages/Group3_MilitaryPersonnel_Dark.png",
        "contactimages/Group3_Rescuers_Dark.png",
        "contactimages/Group4_Meeting_Dark.png",
        "contactimages/Guitarist_Female_Dark.png",
        "contactimages/Guitarist_Male_Dark.png",
        "contactimages/Gus_cawboy-128.png",
        "contactimages/hena_woman_face-128.png",
        "contactimages/Immunologist_Female_Dark.png",
        "contactimages/Immunologist_Male_Dark.png",
        "contactimages/iri_girl_face-128.png",
        "contactimages/Jay_man-128.png",
        "contactimages/kors_man-128.png",
        "contactimages/laly_face_woman-128.png",
        "contactimages/Matthew_school_boy-128.png",
        "contactimages/michela_face_young-128.png",
        "contactimages/mike_boss_man-128.png",
        "contactimages/Musician_Female_Dark.png",
        "contactimages/Musician_Male_Dark.png",
        "contactimages/Nurse_Female_Dark.png",
        "contactimages/Nurse_Male_Dark.png",
        "contactimages/Office-Customer-Female-Light-icon.png",
        "contactimages/Office-Customer-Male-Light-icon.png",
        "contactimages/oscar_boy-128.png",
        "contactimages/Person-Male-Light-icon.png",
        "contactimages/Person_Undefined_Female_Dark.png",
        "contactimages/Person_Undefined_Male_Dark.png",
        "contactimages/Pharmacist_Female_Dark.png",
        "contactimages/Pharmacist_Male_Dark.png",
        "contactimages/PilotMilitary_Female_Dark.png",
        "contactimages/PilotMilitary_Male_Dark.png",
        "contactimages/PilotOldFashioned_Female_Dark.png",
        "contactimages/PilotOldFashioned_Male_Dark.png",
        "contactimages/Pilot_Female_Dark.png",
        "contactimages/Pilot_Male_Dark.png",
        "contactimages/PizzaDeliveryman_Female_Dark.png",
        "contactimages/PizzaDeliveryman_Male_Dark.png",
        "contactimages/profile_boy-128.png",
        "contactimages/SantaClaus_Female.png",
        "contactimages/SantaClaus_Male.png",
        "contactimages/seby_user_face-128.png",
        "contactimages/smith_man-128.png",
        "contactimages/stela_young_prety-128.png",
        "contactimages/TechnicalSupportRepresentative_Female_Dark.png",
        "contactimages/TechnicalSupportRepresentative_Male_Dark.png",
        "contactimages/Viking_Female.png",
        "contactimages/Viking_Male.png",
        "contactimages/Waiter_Male_Dark.png",
        "contactimages/Waitress_Female_Dark.png",
        "contactimages/Wedding_Bridegroom_Dark.png",
        "contactimages/Writer_Female_Dark.png",
        "contactimages/Writer_Male_Dark.png"
    };

    private static TreeMap<String, URL> urlMap = new TreeMap<>();

    private static HashMap<URL, Object> objectMap = new HashMap<>();

    public static void main(String... args) throws MalformedURLException {
        System.out.println(getResourceURL("patch.jpg"));
    }

    public static URL getResourceURL(String resourceName) {
        if (urlMap.containsKey(resourceName)) {
            return urlMap.get(resourceName);
        }
        urlMap.put(resourceName, ResourceHelper.class.getResource(resourceName));
        return urlMap.get(resourceName);
    }

    public static Image getImageObject(URL url) throws IOException {
        try {
            if (objectMap.containsKey(url)) {
                return (Image) objectMap.get(url);
            }
            objectMap.put(url, (Object) javax.imageio.ImageIO.read(url));
            return (Image) objectMap.get(url);
        } catch (Exception ex) {
            System.out.println("ResourceHelper > Error in reading image : " + ex);
        }
        return null;
    }

    public static Image getImageObject(String resourceName) throws IOException {
        URL url = getResourceURL(resourceName);
        return getImageObject(url);
    }
}
