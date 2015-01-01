/* Utilities used to manipulate files

 Copyright (c) 2004-2010 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

 PT_COPYRIGHT_VERSION_2
 COPYRIGHTENDKEY

 */
package com.ptplot;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// Avoid importing any packages from ptolemy.* here so that we
// can ship Ptplot.
///////////////////////////////////////////////////////////////////
//// FileUtilities

/**
 A collection of utilities for manipulating files
 These utilities do not depend on any other ptolemy.* packages.

 @author Christopher Brooks
 @version $Id: FileUtilities.java 59167 2010-09-21 17:08:02Z cxh $
 @since Ptolemy II 4.0
 @Pt.ProposedRating Green (cxh)
 @Pt.AcceptedRating Green (cxh)
 */
@SuppressWarnings("PMD")
public class FileUtilities {

    /** Instances of this class cannot be created.
     */
    private FileUtilities() {
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Copy sourceURL to destinationFile without doing any byte conversion.
     *  @param sourceURL The source URL
     *  @param destinationFile The destination File.
     *  @return true if the file was copied, false if the file was not
     *  copied because the sourceURL and the destinationFile refer to the
     *  same file.
     *  @exception IOException If the source file does not exist.
     */
    public static boolean binaryCopyURLToFile(URL sourceURL,
            File destinationFile) throws IOException {
        URL destinationURL = destinationFile.getCanonicalFile().toURI().toURL();

        if (sourceURL.sameFile(destinationURL)) {
            return false;
        }

        // If sourceURL is of the form file:./foo, then we need to try again.
        File sourceFile = new File(sourceURL.getFile());

        // If the sourceURL is not a jar URL, then check to see if we
        // have the same file.
        // FIXME: should we check for !/ and !\ everywhere?
        if ((sourceFile.getPath().indexOf("!/") == -1)
                && (sourceFile.getPath().indexOf("!\\") == -1)) {
            try {
                if (sourceFile.getCanonicalFile().toURI().toURL().sameFile(
                        destinationURL)) {
                    return false;
                }
            } catch (IOException ex) {
                // JNLP Jar urls sometimes throw an exception here.
                // IOException constructor does not take a cause
                IOException ioException = new IOException(
                        "Cannot find canonical file name of '" + sourceFile
                                + "'");
                ioException.initCause(ex);
                throw ioException;
            }
        }

        _binaryCopyStream(sourceURL.openStream(), destinationFile);

        return true;
    }

    /** Read a sourceURL without doing any byte conversion.
     *  @param sourceURL The source URL
     *  @return The array of bytes read from the URL.
     *  @exception IOException If the source URL does not exist.
     */
    public static byte[] binaryReadURLToByteArray(URL sourceURL)
            throws IOException {
        return _binaryReadStream(sourceURL.openStream());
    }

    /** Extract a jar file into a directory.  This is a trivial
     *  implementation of the <code>jar -xf</code> command.
     *  @param jarFileName The name of the jar file to extract
     *  @param directoryName The name of the directory.  If this argument
     *  is null, then the files are extracted in the current directory.
     *  @exception IOException If the jar file cannot be opened, or
     *  if there are problems extracting the contents of the jar file
     */
    public static void extractJarFile(String jarFileName, String directoryName)
            throws IOException {
        JarFile jarFile = new JarFile(jarFileName);
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                File destinationFile = new File(directoryName, jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                        throw new IOException("Warning, failed to create "
                                + "directory for \"" + destinationFile + "\".");
                    }
                } else {
                    _binaryCopyStream(jarFile.getInputStream(jarEntry),
                            destinationFile);
                }
            }
        } finally {
            jarFile.close();
        }
    }

    /** Extract the contents of a jar file.
     *  @param args An array of arguments.  The first argument
     *  names the jar file to be extracted.  The first argument
     *  is required.  The second argument names the directory in
     *  which to extract the files from the jar file.  The second
     *  argument is optional.
     */
    public static void main(String[] args) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileUtilities.class);
        if (args.length < 1 || args.length > 2) {
            log.info("Usage: java -classpath $PTII "
                    + "ptolemy.util.FileUtilities jarFile [directory]\n"
                    + "where jarFile is the name of the jar file\n"
                    + "and directory is the optional directory in which to "
                    + "extract.");
            StringUtilities.exit(2);
        }
        String jarFileName = args[0];
        String directoryName = null;
        if (args.length >= 2) {
            directoryName = args[1];
        }
        try {
            extractJarFile(jarFileName, directoryName);
        } catch (Throwable throwable) {
            log.error("Failed to extract \"" + jarFileName + "\"");
            log.error("{}",throwable);
            StringUtilities.exit(3);
        }
    }

    /** Given a file name or URL, construct a java.io.File object that
     *  refers to the file name or URL.  This method
     *  first attempts to directly use the file name to construct the
     *  File. If the resulting File is a relative pathname, then
     *  it is resolved relative to the specified base URI, if
     *  there is one.  If there is no such base URI, then it simply
     *  returns the relative File object.  See the java.io.File
     *  documentation for a details about relative and absolute pathnames.
     *
     *  <p>If the name begins with
     *  "xxxxxxCLASSPATHxxxxxx" or "$CLASSPATH" then search for the
     *  file relative to the classpath.
     *
     *  <p>Note that "xxxxxxCLASSPATHxxxxxx" is the value of the
     *  globally defined constant $CLASSPATH available in the Ptolemy
     *  II expression language.
     *
     *  <p>If the name begins with $CLASSPATH or "xxxxxxCLASSPATHxxxxxx"
     *  but that name cannot be found in the classpath, the value
     *  of the ptolemy.ptII.dir property is substituted in.
     *  <p>
     *  The file need not exist for this method to succeed.  Thus,
     *  this method can be used to determine whether a file with a given
     *  name exists, prior to calling openForWriting(), for example.
     *
     *  <p>This method is similar to
     *  {@link #nameToURL(String, URI, ClassLoader)}
     *  except that in this method, the file or URL must be readable.
     *  Usually, this method is use for write a file and
     *  {@link #nameToURL(String, URI, ClassLoader)} is used for reading.
     *
     *  @param name The file name or URL.
     *  @param base The base for relative URLs.
     *  @return A File, or null if the filename argument is null or
     *   an empty string.
     *  @see #nameToURL(String, URI, ClassLoader)
     */
    public static File nameToFile(String name, URI base) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileUtilities.class);
        if ((name == null) || name.trim().equals("")) {
            return null;
        }

        if (name.startsWith(_CLASSPATH_VALUE) || name.startsWith("$CLASSPATH")) {
            URL result = null;
            try {
                result = _searchClassPath(name, null);
            } catch (IOException ex) {
                log.warn("error in nameToFile", ex);
                // Ignore.  In nameToFile(), it is ok if we don't find the variable
            }
            if (result != null) {
                return new File(result.getPath());
            } else {
                String ptII = StringUtilities.getProperty("ptolemy.ptII.dir");
                if (ptII != null && ptII.length() > 0) {
                    return new File(ptII, _trimClassPath(name));
                }
            }
        }

        File file = new File(name);

        if (!file.isAbsolute()) {
            // Try to resolve the base directory.
            if (base != null) {
                // Need to replace \ with /, otherwise resolve would fail even
                // if invoked in a windows OS. -- tfeng (02/27/2009)
                URI newURI = base.resolve(StringUtilities.substitute(name, " ",
                        "%20").replace('\\', '/'));

                //file = new File(newURI);
                String urlString = newURI.getPath();
                file = new File(StringUtilities.substitute(urlString, "%20",
                        " "));
            }
        }
        return file;
    }

    /** Given a file or URL name, return as a URL.  If the file name
     *  is relative, then it is interpreted as being relative to the
     *  specified base directory. If the name begins with
     *  "xxxxxxCLASSPATHxxxxxx" or "$CLASSPATH" then search for the
     *  file relative to the classpath.
     *
     *  <p>Note that "xxxxxxCLASSPATHxxxxxx" is the value of the
     *  globally defined constant $CLASSPATH available in the Ptolemy
     *  II expression language.
     *  II expression language.
     *
     *  <p>If no file is found, then throw an exception.
     *
     *  <p>This method is similar to {@link #nameToFile(String, URI)}
     *  except that in this method, the file or URL must be readable.
     *  Usually, this method is use for reading a file and
     *  is used for writing {@link #nameToFile(String, URI)}.
     *
     *  @param name The name of a file or URL.
     *  @param baseDirectory The base directory for relative file names,
     *   or null to specify none.
     *  @param classLoader The class loader to use to locate system
     *   resources, or null to use the system class loader that was used
     *   to load this class.
     *  @return A URL, or null if the name is null or the empty string.
     *  @exception IOException If the file cannot be read, or
     *   if the file cannot be represented as a URL (e.g. System.in), or
     *   the name specification cannot be parsed.
     *  @see #nameToFile(String, URI)
     */
    public static URL nameToURL(String name, URI baseDirectory,
            ClassLoader classLoader) throws IOException {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileUtilities.class);
        if ((name == null) || name.trim().equals("")) {
            return null;
        }

        if (name.startsWith(_CLASSPATH_VALUE) || name.startsWith("$CLASSPATH")) {
            URL result = _searchClassPath(name, classLoader);
            if (result == null) {
                throw new IOException("Cannot find file '"
                        + _trimClassPath(name) + "' in classpath");
            }

            return result;
        }

        File file = new File(name);

        if (file.isAbsolute()) {
            if (!file.canRead()) {
                // FIXME: This is a hack.
                // Expanding the configuration with Ptolemy II installed
                // in a directory with spaces in the name fails on
                // JAIImageReader because PtolemyII.jpg is passed in
                // to this method as C:\Program%20Files\Ptolemy\...
                file = new File(StringUtilities.substitute(name, "%20", " "));

                URL possibleJarURL = null;

                if (!file.canRead()) {
                    // ModelReference and FilePortParameters sometimes
                    // have paths that have !/ in them.
                    possibleJarURL = ClassUtilities.jarURLEntryResource(name);

                    if (possibleJarURL != null) {
                        file = new File(possibleJarURL.getFile());
                    }
                }

                if (!file.canRead()) {
                    throw new IOException("Cannot read file '"
                            + name
                            + "' or '"
                            + StringUtilities.substitute(name, "%20", " ")
                            + "'"
                            + ((possibleJarURL == null) ? "" : (" or '"
                                    + possibleJarURL.getFile() + "")));
                }
            }

            return file.toURI().toURL();
        } else {
            // Try relative to the base directory.
            if (baseDirectory != null) {
                // Try to resolve the URI.
                URI newURI;

                try {
                    newURI = baseDirectory.resolve(name);
                } catch (Exception ex) {
                    // FIXME: Another hack
                    // This time, if we try to open some of the JAI
                    // demos that have actors that have defaults FileParameters
                    // like "$PTII/doc/img/PtolemyII.jpg", then resolve()
                    // bombs.
                    String name2 = StringUtilities.substitute(name, "%20", " ");
                    try {
                        newURI = baseDirectory.resolve(name2);
                        name = name2;
                    } catch (Exception ex2) {
                        IOException io = new IOException(
                                "Problem with URI format in '"
                                        + name
                                        + "'. "
                                        + "and '"
                                        + name2
                                        + "' "
                                        + "This can happen if the file name "
                                        + "is not absolute "
                                        + "and is not present relative to the "
                                        + "directory in which the specified model "
                                        + "was read (which was '"
                                        + baseDirectory + "')");
                        io.initCause(ex2);
                        throw io;
                    }
                }

                String urlString = newURI.toString();

                try {
                    // Adding another '/' for remote execution.
                    if ((newURI.getScheme() != null)
                            && (newURI.getAuthority() == null)) {
                        // Change from Efrat:
                        // "I made these change to allow remote
                        // execution of a workflow from within a web
                        // service."

                        // "The first modification was due to a URI
                        // authentication exception when trying to
                        // create a file object from a URI on the
                        // remote side. The second modification was
                        // due to the file protocol requirements to
                        // use 3 slashes, 'file:///' on the remote
                        // side, although it would be probably be a
                        // good idea to also make sure first that the
                        // url string actually represents the file
                        // protocol."
                        urlString = urlString.substring(0, 6) + "//"
                                + urlString.substring(6);

                        //} else {
                        // urlString = urlString.substring(0, 6) + "/"
                        // + urlString.substring(6);
                    }
                    // Unfortunately, between Java 1.5 and 1.6,
                    // The URL constructor changed.
                    // In 1.5, new URL("file:////foo").toString()
                    // returns "file://foo"
                    // In 1.6, new URL("file:////foo").toString()
                    // return "file:////foo".
                    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6561321
                    return new URL(urlString);
                } catch (Exception ex3) {
                    try {
                        // Under Webstart, opening
                        // hoc/demo/ModelReference/ModelReference.xml
                        // requires this because the URL is relative.
                        return new URL(baseDirectory.toURL(), urlString);
                    } catch (Exception ex4) {
                        try {
                            // Under Webstart, ptalon, EightChannelFFT
                            // requires this.
                            return new URL(baseDirectory.toURL(), newURI
                                    .toString());
                        } catch (Exception ex5) {
                            log.warn("ignoring..", ex5);
                        }

                        IOException io = new IOException(
                                "Problem with URI format in '"
                                        + urlString
                                        + "'. "
                                        + "This can happen if the '"
                                        + urlString
                                        + "' is not absolute"
                                        + " and is not present relative to the directory"
                                        + " in which the specified model was read"
                                        + " (which was '" + baseDirectory
                                        + "')");
                        io.initCause(ex3);
                        throw io;
                    }
                }
            }

            // As a last resort, try an absolute URL.

            URL url = new URL(name);

            // If we call new URL("http", null, /foo);
            // then we get "http:/foo", which should be "http://foo"
            // This change suggested by Dan Higgins and Kevin Kruland
            // See kepler/src/util/URLToLocalFile.java
            try {
                String fixedURLAsString = url.toString().replaceFirst(
                        "(https?:)//?", "$1//");
                url = new URL(fixedURLAsString);
            } catch (Exception e) {
                log.warn("ignoring..",e);
            }
            return url;
        }
    }

    /** Open the specified file for reading. If the specified name is
     *  "System.in", then a reader from standard in is returned. If
     *  the name begins with "$CLASSPATH" or "xxxxxxCLASSPATHxxxxxx",
     *  then the name is passed to {@link #nameToURL(String, URI, ClassLoader)}
     *  If the file name is not absolute, the it is assumed to be relative to
     *  the specified base URI.
     *  @see #nameToURL(String, URI, ClassLoader)
     *  @param name File name.
     *  @param base The base URI for relative references.
     *  @param classLoader The class loader to use to locate system
     *   resources, or null to use the system class loader that was used
     *   to load this class.
     *  @return If the name is null or the empty string,
     *  then null is returned, otherwise a buffered reader is returned.

     *  @exception IOException If the file cannot be opened.
     */
    public static BufferedReader openForReading(String name, URI base,
            ClassLoader classLoader) throws IOException {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileUtilities.class);
        if ((name == null) || name.trim().equals("")) {
            return null;
        }

        if (name.trim().equals("System.in")) {
            if (STD_IN == null) {
                STD_IN = new BufferedReader(new InputStreamReader(System.in));
            }

            return STD_IN;
        }

        // Not standard input. Try URL mechanism.
        URL url = nameToURL(name, base, classLoader);

        if (url == null) {
            throw new IOException("Could not convert \"" + name
                    + "\" with base \"" + base + "\" to a URL.");
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(url.openStream());
        } catch (IOException ex) {
            // Try it as a jar url.
            // WebStart ptalon MapReduce needs this.
            try {
                URL possibleJarURL = ClassUtilities.jarURLEntryResource(url
                        .toString());
                if (possibleJarURL != null) {
                    inputStreamReader = new InputStreamReader(possibleJarURL
                            .openStream());
                }
                // If possibleJarURL is null, this throws an exception,
                // which we ignore and report the first exception (ex)
                return new BufferedReader(inputStreamReader);
            } catch (Exception ex2) {
                try {
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                } catch (IOException ex3) {
                    log.warn("ignoring..", ex3);
                }
                IOException ioException = new IOException("Failed to open \""
                        + url + "\".");
                ioException.initCause(ex);
                throw ioException;
            }
        }

        return new BufferedReader(inputStreamReader);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public members                   ////

    /** Standard in as a reader, which will be non-null
     *  only after a call to openForReading("System.in").
     */
    public static BufferedReader STD_IN = null;

    /** Standard out as a writer, which will be non-null
     *  only after a call to openForWriting("System.out").
     */
    public static PrintWriter STD_OUT = null;

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    /** Copy files safely.  If there are problems, the streams are
     *  close appropriately.
     *  @param inputStream The input stream.
     *  @param destinationFile The destination File.
     *  @exception IOException If the input stream cannot be created
     *  or read, or * if there is a problem writing to the destination
     *  file.
     */
    private static void _binaryCopyStream(InputStream inputStream,
            File destinationFile) throws IOException {
        // Copy the source file.
        BufferedInputStream input = null;

        try {
            input = new BufferedInputStream(inputStream);

            BufferedOutputStream output = null;

            try {
                File parent = destinationFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        throw new IOException("Failed to create directories "
                                + "for \"" + parent + "\".");
                    }
                }

                output = new BufferedOutputStream(new FileOutputStream(
                        destinationFile));

                int c;

                while ((c = input.read()) != -1) {
                    output.write(c);
                }
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                }
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }
    }

    /** Read a stream safely.  If there are problems, the streams are
     *  close appropriately.
     *  @param inputStream The input stream.
     *  @exception IOException If the input stream cannot be read.
     */
    private static byte[] _binaryReadStream(InputStream inputStream)
            throws IOException {
        // Copy the source file.
        BufferedInputStream input = null;

        ByteArrayOutputStream output = null;

        try {
            input = new BufferedInputStream(inputStream);

            try {
                output = new ByteArrayOutputStream();
                // Read the stream in 8k chunks
                final int BUFFERSIZE = 8192;
                byte[] buffer = new byte[BUFFERSIZE];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, BUFFERSIZE)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                if (output != null) {
                    try {
                        // ByteArrayOutputStream.close() has no
                        // effect, but we try it anyway for good form.
                        output.close();
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                }
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }
        if (output != null) {
            return output.toByteArray();
        }
        return null;
    }

    /** Search the classpath.
     *  @param name The name to be searched
     *  @param classLoader The class loader to use to locate system
     *   resources, or null to use the system class loader that was used
     *   to load this class.
     *  @return null if name does not start with "$CLASSPATH"
     *  or _CLASSPATH_VALUE or if name cannot be found.
     */
    private static URL _searchClassPath(String name, ClassLoader classLoader)
            throws IOException {

        URL result = null;

        // If the name begins with "$CLASSPATH", or
        // "xxxxxxCLASSPATHxxxxxx",then attempt to open the file
        // relative to the classpath.
        // NOTE: Use the dummy variable constant set up in the constructor.
        if (name.startsWith(_CLASSPATH_VALUE) || name.startsWith("$CLASSPATH")) {
            // Try relative to classpath.
            String trimmedName = _trimClassPath(name);

            if (classLoader == null) {
                String referenceClassName = "ptolemy.util.FileUtilities";

                try {
                    // WebStart: We might be in the Swing Event thread, so
                    // Thread.currentThread().getContextClassLoader()
                    // .getResource(entry) probably will not work so we
                    // use a marker class.
                    Class referenceClass = Class.forName(referenceClassName);
                    classLoader = referenceClass.getClassLoader();
                } catch (Exception ex) {
                    // IOException constructor does not take a cause
                    IOException ioException = new IOException(
                            "Cannot look up class \"" + referenceClassName
                                    + "\" or get its ClassLoader.");
                    ioException.initCause(ex);
                    throw ioException;
                }
            }

            // Use Thread.currentThread()... for Web Start.
            result = classLoader.getResource(trimmedName);
        }
        return result;
    }

    /** Remove the value of _CLASSPATH_VALUE or "$CLASSPATH".
     */
    private static String _trimClassPath(String name) {
        String classpathKey;

        if (name.startsWith(_CLASSPATH_VALUE)) {
            classpathKey = _CLASSPATH_VALUE;
        } else {
            classpathKey = "$CLASSPATH";
        }

        return name.substring(classpathKey.length() + 1);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private members                   ////

    /** Tag value used by this class and registered as a parser
     *  constant for the identifier "CLASSPATH" to indicate searching
     *  in the classpath.  This is a hack, but it deals with the fact
     *  that Java is not symmetric in how it deals with getting files
     *  from the classpath (using getResource) and getting files from
     *  the file system.
     */
    private static String _CLASSPATH_VALUE = "xxxxxxCLASSPATHxxxxxx";
}
