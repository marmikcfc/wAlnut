package model.experiments.vision.MARK_II;

import model.MARK_II.Region;
import model.MARK_II.connectTypes.AbstractRegionToRegionConnect;
import model.util.Rectangle;

import java.io.File;

/**
 * PROBLEM: When a user wants to create a Neocortex object that is too large for
 * the Java Heap. This is usually when you want to build a Neocortex with 0.5+ million Neurons.
 *
 * SOLUTION: This class provides a easy to use API for creating your Neocortex as separately
 * saved JSON files.
 *
 * @author Q Liu (quinnliu@vt.edu)
 * @date 5/27/2015.
 */
public class BigNeocortex {

    private final int MAX_SIZE_OF_A_REGION_IN_MB;
    private String[] regionParameterListInOrder;
    private AbstractRegionToRegionConnect neocortexRegionToNeocortexRegion;
    private String[] connectionParameterListInOrder;

    private String currentRegionName;
    private Region currentRegion; 
    private String rootRegionName;

    /**
     * @param maxSizeOfARegionInMB
     * @param regionParameterListInOrder
     * @param neocortexRegionToNeocortexRegion
     * @param connectionParameterListInOrder
     * @param pathAndFolderName Must not contain any numbers in folderName
     */
    public BigNeocortex(int maxSizeOfARegionInMB, String[] regionParameterListInOrder,
                        AbstractRegionToRegionConnect neocortexRegionToNeocortexRegion,
                        String[] connectionParameterListInOrder, String pathAndFolderName) {
        this.MAX_SIZE_OF_A_REGION_IN_MB = maxSizeOfARegionInMB;
        this.regionParameterListInOrder = regionParameterListInOrder;
        this.neocortexRegionToNeocortexRegion =
                neocortexRegionToNeocortexRegion;
        this.connectionParameterListInOrder = connectionParameterListInOrder;

        this.currentRegionName = regionParameterListInOrder[0];
        this.rootRegionName = currentRegionName;

        //this.saveConnectedNeocortexInFolder(pathAndFolderName);
    }

    public static void main(String[] args) {
        CharSequence oldNumber = "123";
        CharSequence newNumber = "124";
        String example = "folderName__123";
        System.out.println("example = " + example);

        int indexOf__ = example.indexOf("__");
        System.out.println("indexOf__ = " + indexOf__);
        String folderNumber = example.substring(indexOf__ + 2);
        System.out.println("folderNumber = " + folderNumber);
        int folderNum = Integer.valueOf(folderNumber) + 1;
        System.out.println("folderNum = " + folderNum);
        String strFolderNumber = String.valueOf(folderNum);
        System.out.println("strFolderNumber = " + strFolderNumber);
        example = example.replace(oldNumber, strFolderNumber);

        System.out.println("example = " + example);
    }

    void saveConnectedNeocortexInFolder(String pathAndFolderName) {
        // TODO: fix inifinite loop somewhere in here after i > 1
        System.out.println(">>> starting saveConnectedNeocortexInFolder ...");
        File file = new File(pathAndFolderName);
        File path = new File(extractPath(pathAndFolderName));

        String newFolderName = extractFolderName(pathAndFolderName);

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
                        String folderNumber = newFolderName.substring(indexOfFolderNumber);
                        int folderNumberPlusOne = Integer.valueOf(folderNumber) + 1;

                        String newFolderNumber = String.valueOf(folderNumberPlusOne);
                        newFolderName = newFolderName.replace(folderNumber, newFolderNumber);
                    } else {
                        // this is the 2nd time this folder will be created
                        // with an extra number 0 at the end
                        newFolderName += "__" + String.valueOf(i);
                        foundUniqueName = true;
                    }
                } else {
                    foundUniqueName = true;
                }
            }
            // now newFolderName is a unique name every time the program is run

            // create a new folder to store BigNeocortex object
            File whereToSaveBigNeocortex = new File(path + "/" + newFolderName);
            whereToSaveBigNeocortex.mkdir();
        } else {
            // file.mkdir() worked so there is no need to create a unique folder name
        }
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
        // TODO: implement this method with total used heap size < MAX_SIZE_OF_A_REGION_IN_MB
    }

    public Region getRegion(String regionBiologicalName) {
        // TODO: implement this method with total used heap size < MAX_SIZE_OF_A_REGION_IN_MB
        return null;
    }

    public void addToCurrentRegion(Rectangle rectanglePartOfParentRegionToConnectTo,
                                   Region childRegion,
                                   int numberOfColumnsToOverlapAlongNumberOfRows,
                                   int numberOfColumnsToOverlapAlongNumberOfColumns) {
        // TODO: implement this method with total used heap size < MAX_SIZE_OF_A_REGION_IN_MB
    }

    public Region getCurrentRegion() {
        // TODO: implement this method with total used heap size < MAX_SIZE_OF_A_REGION_IN_MB
        return null;
    }

    String extractFolderName(String pathAndFolderName) {
        // example: String pathAndFolderName = "./src/test/java/model/experiments/vision/MARK_II/FolderName";
        String[] parts = pathAndFolderName.split("/");
        String folderName = parts[parts.length - 1];
        return folderName;
    }

    String extractPath(String pathAndFolderName) {
        int lengthOfFolderName = extractFolderName(pathAndFolderName).length();
        return pathAndFolderName.substring(0, pathAndFolderName.length() - lengthOfFolderName);
    }
}
