package gui;

import java.io.File;

/**
 *
 * @author Jimmys Daskalakis
 */
   /**
     * Inherited FileFilter class to facilitate reuse when multiple file filter
     * selections are required.  For example purposes, I used a static nested class,
     * which is defined as below as a member of our original FileChooserExample class.
     */
   public class ExtensionFileFilter extends javax.swing.filechooser.FileFilter {

        private java.util.List<String> extensions;
        private String description;

        public  ExtensionFileFilter(String[] exts, String desc) {
            if (exts != null) {
                for (int i = 0; i < exts.length; i++) {

                    // Clean array of extensions to remove "." and transform to lowercase.
                    exts[i] = exts[i].replace(".", "").trim().toLowerCase();
                }

                extensions = java.util.Arrays.asList(exts); // Convert array to List<String>
            } // No else necessary as null extensions handled below.

            // Using inline if syntax, use input from desc or use a default value.
            // Wrap with an if statement to default as well as avoid NullPointerException when using trim().
            description = (desc != null) ? desc.trim() : "Custom File List";
        }

        @Override
        public boolean accept(File f) { // Handles which files are allowed by filter.

            // Since this is used during enumeration of existing file system,
            // this should not be necessary, but good practice to test for null.
            if (f != null) {
                if (f.isDirectory()) { // Allow directories to be seen.
                    return true;
                }

                // Get file extension and test if should be allowed.
                String extension = getExtension(f);
                if ((extension != null) && (extensions != null)) {
                    return extensions.contains(extension);
                }
            }

            return false;
        }

        @Override
        public String getDescription() { // 'Files of Type' description
            return description;
        }

        // Takes a java.io.File object, parses file extension, and returns as java.lang.String.
        String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }

            return ext;
        }
    }