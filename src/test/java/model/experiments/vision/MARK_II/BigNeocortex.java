package model.experiments.vision.MARK_II;

import com.google.gson.Gson;
import model.MARK_II.Region;
import model.MARK_II.connectTypes.AbstractRegionToRegionConnect;
import model.util.FileInputOutput;
import model.util.Rectangle;

import java.io.File;
import java.io.IOException;

/**
 * NOTE: Refer to Neocortex.java for summary of how a Neocortex is represented.
 * <p/>
 * PROBLEM: When a user wants to create a Neocortex object that is too large for
 * the Java Heap. This is usually when you want to build a Neocortex with 0
 * .5+ million Neurons for a computer with 4GB of RAM.
 * <p/>
 * SOLUTION: This class provides a easy to use API for creating your
 * Neocortex as separately
 * saved JSON files.
 *
 * @author Q Liu (quinnliu@vt.edu)
 * @date 6/9/2015.
 */
public class BigNeocortex {

    private final int MAX_SIZE_OF_A_REGION_IN_MB;
    private String[] regionParameterListInOrder;
    private AbstractRegionToRegionConnect neocortexRegionToNeocortexRegion;
    private String[] connectionParameterListInOrder;

    private String currentRegionName;
    private Region currentRegion;
    private String rootRegionName;
    private String pathAndFolderName; // BigNeocortex is saved as JSON file

    private Gson gson;

    /**
     * @param maxSizeOfARegionInMB
     * @param regionParameterListInOrder       Every 6 elements in list
     *                                         represent a
     *                                         list of parameters for 1
     *                                         Region. The
     *                                         parameters for the root region
     *                                         must
     *                                         be the first 6 elements in the
     *                                         list.
     * @param neocortexRegionToNeocortexRegion
     * @param connectionParameterListInOrder   Every 7 elements in list
     *                                         represent a list of parameters
     *                                         for
     *                                         1 directed connection between the
     *                                         currentRegion and the region
     *                                         provided in the list.
     * @param pathAndFolderName
     */
    public BigNeocortex(int maxSizeOfARegionInMB, String[]
            regionParameterListInOrder,
                        AbstractRegionToRegionConnect
                                neocortexRegionToNeocortexRegion,
                        String[] connectionParameterListInOrder, String
                                pathAndFolderName) throws IOException {
        this.MAX_SIZE_OF_A_REGION_IN_MB = maxSizeOfARegionInMB;
        this.regionParameterListInOrder = regionParameterListInOrder;
        this.neocortexRegionToNeocortexRegion =
                neocortexRegionToNeocortexRegion;
        this.connectionParameterListInOrder = connectionParameterListInOrder;

        this.currentRegionName = regionParameterListInOrder[0];
        this.rootRegionName = currentRegionName;
        this.pathAndFolderName = this
                .createUniqueFolderToSaveBigNeocortex(pathAndFolderName);

        this.gson = new Gson();
        this.instantiateAndSaveAllUnconnectedRegions();
    }

    void instantiateAndSaveAllUnconnectedRegions() throws IOException {
        // TODO: instantiate and save all regions
        // NOTE: new connection pattern every 7 elements

        for (int i = 0; i < this.regionParameterListInOrder.length; i = i + 6) {
            // NOTE: new region every 6 elements

            // convert String parameters into correct type
            String biologicalName = this.regionParameterListInOrder[i];
            int numberOfColumnsAlongRowsDimension = Integer.valueOf(this
                    .regionParameterListInOrder[i + 1]);
            int numberOfColumnsAlongColumnsDimension = Integer.valueOf(this
                    .regionParameterListInOrder[i + 2]);
            int cellsPerColumn = Integer.valueOf(this
                    .regionParameterListInOrder[i + 3]);
            double percentMinimumOverlapScore = Double.valueOf(this
                    .regionParameterListInOrder[i + 4]);
            int desiredLocalActivity = Integer.valueOf(this
                    .regionParameterListInOrder[i + 5]);

            Region region = new Region(biologicalName,
                    numberOfColumnsAlongRowsDimension,
                    numberOfColumnsAlongColumnsDimension,
                    cellsPerColumn, percentMinimumOverlapScore,
                    desiredLocalActivity);

            // save Region as JSON file
            String regionAsJSON = this.gson.toJson(region);
            String finalPathAndFile = this.pathAndFolderName + "/" + biologicalName + ".json";
            FileInputOutput.saveObjectToTextFile(regionAsJSON, finalPathAndFile);
        }
    }

    /**
     * @param pathAndFolderName
     * @return The old path and new folder name the BigNeocortex object will
     * be saved in.
     */
    String createUniqueFolderToSaveBigNeocortex(String pathAndFolderName) {
        File file = new File(pathAndFolderName);
        File path = new File(extractPath(pathAndFolderName));

        String newFolderName = extractFolderName(pathAndFolderName);

        String pathAndNewFolderName = pathAndFolderName;
        if (file.mkdir() == false) {
            // if there is already a folder/file with the same name add
            // a number to the folder name to be created
            boolean foundUniqueName = false;
            int i = 0;
            while (foundUniqueName == false) {
                if (isFolderInList(newFolderName, path.listFiles())) {
                    // we need to change newFolderName to something unique
                    CharSequence twoUnderscores = "__";
                    if (newFolderName.contains(twoUnderscores)) {
                        // this is not the first time this folder has been
                        // created so increment number after 2 underscores
                        int indexOf2Underscores = newFolderName.indexOf("__");
                        int indexOfFolderNumber = indexOf2Underscores + 2;
                        String folderNumber = newFolderName.substring
                                (indexOfFolderNumber);
                        int folderNumberPlusOne = Integer.valueOf
                                (folderNumber) + 1;

                        String newFolderNumber = String.valueOf
                                (folderNumberPlusOne);
                        newFolderName = newFolderName.replace(folderNumber,
                                newFolderNumber);
                    } else {
                        // this is the 2nd time this folder will be created
                        // with an extra number 0 at the end
                        newFolderName += "__" + String.valueOf(i);
                    }
                } else {
                    foundUniqueName = true;
                }
            }
            // now newFolderName is a unique name every time the program is run

            // create a new folder to store BigNeocortex object
            pathAndNewFolderName = path + "/" + newFolderName;
            File whereToSaveBigNeocortex = new File(pathAndNewFolderName);
            whereToSaveBigNeocortex.mkdir();
        } else {
            // file.mkdir() worked so there is no need to create a unique
            // folder name
        }
        return pathAndNewFolderName;
    }

    boolean isFolderInList(String folderName, File[] listOfFilesAndFolders) {
        for (int i = 0; i < listOfFilesAndFolders.length; i++) {

            if (listOfFilesAndFolders[i].isDirectory() &&
                    folderName.equals(listOfFilesAndFolders[i].getName())) {
                return true;
            }
        }
        return false;
    }

    public void changeCurrentRegionTo(String newCurrentRegionBiologicalName) {
        // TODO: implement this method with total used heap size <
        // MAX_SIZE_OF_A_REGION_IN_MB
    }

    public Region getRegion(String regionBiologicalName) {
        // TODO: implement this method with total used heap size <
        // MAX_SIZE_OF_A_REGION_IN_MB
        return null;
    }

    public void addToCurrentRegion(Rectangle
                                           rectanglePartOfParentRegionToConnectTo,
                                   Region childRegion,
                                   int numberOfColumnsToOverlapAlongNumberOfRows,
                                   int numberOfColumnsToOverlapAlongNumberOfColumns) {
        // TODO: implement this method with total used heap size <
        // MAX_SIZE_OF_A_REGION_IN_MB
    }

    public Region getCurrentRegion() {
        // TODO: implement this method with total used heap size <
        // MAX_SIZE_OF_A_REGION_IN_MB
        return null;
    }

    String extractFolderName(String pathAndFolderName) {
        // example: String pathAndFolderName = "
        // ./src/test/java/model/experiments/vision/MARK_II/FolderName";
        String[] parts = pathAndFolderName.split("/");
        String folderName = parts[parts.length - 1];
        return folderName;
    }

    String extractPath(String pathAndFolderName) {
        int lengthOfFolderName = extractFolderName(pathAndFolderName).length();
        return pathAndFolderName.substring(0, pathAndFolderName.length() -
                lengthOfFolderName);
    }
}
