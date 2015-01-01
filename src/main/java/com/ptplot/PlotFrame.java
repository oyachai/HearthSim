/* Top-level window containing a plotter.

 Copyright (c) 1998-2010 The Regents of the University of California.
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

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;


// TO DO:
//   - Add a mechanism for combining two plots into one
///////////////////////////////////////////////////////////////////
//// PlotFrame

/**

 PlotFrame is a versatile two-dimensional data plotter that runs as
 part of an application, but in its own window. It can read files
 compatible with the old Ptolemy plot file format (currently only ASCII).
 It is extended with the capability to read PlotML files in PlotMLFrame.
 An application can also interact directly with the contained Plot
 object, which is visible as a public member, by invoking its methods.
 <p>
 An application that uses this class should set up the handling of
 window-closing events.  Presumably, the application will exit when
 all windows have been closed. This is done with code something like:
 <pre>
 plotFrameInstance.addWindowListener(new WindowAdapter() {
 public void windowClosing(WindowEvent e) {
 // Handle the event
 }
 });
 </pre>
 <p>
 PlotFrame contains an instance of PlotBox. PlotBox is the base class for
 classes with drawing capability, e.g. Plot, LogicAnalyzer. If not
 specified in the constructor, the default is to contain a Plot object. This
 field is set once in the constructor and immutable afterwards.

 @see Plot
 @see PlotBox
 @author Christopher Brooks and Edward A. Lee
 @version $Id: PlotFrame.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 0.2
 @Pt.ProposedRating Yellow (cxh)
 @Pt.AcceptedRating Yellow (cxh)
 */
public class PlotFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    /** Construct a plot frame with a default title and by default contains
     *  an instance of Plot. After constructing this, it is necessary
     *  to call setVisible(true) to make the plot appear.
     */
    public PlotFrame() {
        this("Ptolemy Plot Frame");
    }

    /** Construct a plot frame with the specified title and by default
     *  contains an instance of Plot. After constructing this, it is necessary
     *  to call setVisible(true) to make the plot appear.
     *  @param title The title to put on the window.
     */
    public PlotFrame(String title) {
        this(title, null);
    }

    /** Construct a plot frame with the specified title and the specified
     *  instance of PlotBox.  After constructing this, it is necessary
     *  to call setVisible(true) to make the plot appear.
     *  @param title The title to put on the window.
     *  @param plotArg the plot object to put in the frame, or null to create
     *   an instance of Plot.
     */
    public PlotFrame(String title, PlotBox plotArg) {
        super(title);

        // The Java look & feel is pretty lame, so we use the native
        // look and feel of the platform we are running on.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.warn("ignoring..", e);
        }

        if (plotArg == null) {
            plot = new Plot();
        } else {
            plot = plotArg;
        }

        // Background color is a light grey.
        plot.setBackground(new Color(0xe5e5e5));

        _fileMenu.setMnemonic(KeyEvent.VK_F);
        _editMenu.setMnemonic(KeyEvent.VK_E);
        _specialMenu.setMnemonic(KeyEvent.VK_S);

        // File menu
        JMenuItem[] fileMenuItems = { new JMenuItem("Open", KeyEvent.VK_O),
                new JMenuItem("Save", KeyEvent.VK_S),
                new JMenuItem("SaveAs", KeyEvent.VK_A),
                new JMenuItem("Export", KeyEvent.VK_E),
                new JMenuItem("Print", KeyEvent.VK_P),
                new JMenuItem("Close", KeyEvent.VK_C), };

        // Open button = ctrl-o.
        fileMenuItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Event.CTRL_MASK));

        // Save button = ctrl-s.
        fileMenuItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Event.CTRL_MASK));

        // Print button = ctrl-p.
        fileMenuItems[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Event.CTRL_MASK));

        // Close button = ctrl-w.
        fileMenuItems[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Event.CTRL_MASK));

        FileMenuListener fml = new FileMenuListener();

        // Set the action command and listener for each menu item.
        for (int i = 0; i < fileMenuItems.length; i++) {
            fileMenuItems[i].setActionCommand(fileMenuItems[i].getText());
            fileMenuItems[i].addActionListener(fml);
            _fileMenu.add(fileMenuItems[i]);
        }

        _menubar.add(_fileMenu);

        // Edit menu
        JMenuItem format = new JMenuItem("Format", KeyEvent.VK_F);
        FormatListener formatListener = new FormatListener();
        format.addActionListener(formatListener);
        _editMenu.add(format);
        _menubar.add(_editMenu);

        // Special menu
        JMenuItem[] specialMenuItems = { new JMenuItem("About", KeyEvent.VK_A),
                new JMenuItem("Help", KeyEvent.VK_H),
                new JMenuItem("Clear", KeyEvent.VK_C),
                new JMenuItem("Fill", KeyEvent.VK_F),
                new JMenuItem("Reset axes", KeyEvent.VK_R),
                new JMenuItem("Sample plot", KeyEvent.VK_S), };
        SpecialMenuListener sml = new SpecialMenuListener();

        // Set the action command and listener for each menu item.
        for (int i = 0; i < specialMenuItems.length; i++) {
            specialMenuItems[i].setActionCommand(specialMenuItems[i].getText());
            specialMenuItems[i].addActionListener(sml);
            _specialMenu.add(specialMenuItems[i]);
        }

        _menubar.add(_specialMenu);

        setJMenuBar(_menubar);

        getContentPane().add(plot, BorderLayout.CENTER);

        // FIXME: This should not be hardwired in here.
        setSize(500, 300);

        // Center.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Create a sample plot.
     */
    public void samplePlot() {
        _file = null;
        _directory = null;
        plot.samplePlot();
    }

    /** Set the visibility.  As a side effect, this method
     *  sets the background of the menus.
     *  @param visible True if the Frame is to be visible, false
     *  if it is not visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        _editMenu.setBackground(_menubar.getBackground());
        _fileMenu.setBackground(_menubar.getBackground());
        _specialMenu.setBackground(_menubar.getBackground());
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public variables                  ////

    /** @serial The plot object held by this frame. */

    // FIXME: uncomment final when we upgrade to jdk1.2
    public/*final*/PlotBox plot;

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** @serial Menubar for this frame. */
    protected JMenuBar _menubar = new JMenuBar();

    /** @serial Edit menu for this frame. */
    protected JMenu _editMenu = new JMenu("Edit");

    /** @serial File menu for this frame. */
    protected JMenu _fileMenu = new JMenu("File");

    /** @serial Special menu for this frame. */
    protected JMenu _specialMenu = new JMenu("Special");

    /** @serial Directory that contains the input file. */
    protected File _directory = null;

    /** @serial The input file. */
    protected File _file = null;

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////
    protected void _about() {
        JOptionPane
                .showMessageDialog(
                        this,
                        "PlotFrame class\n"
                                + "By: Edward A. Lee "
                                + "and Christopher Brooks\n"
                                + "Version "
                                + PlotBox.PTPLOT_RELEASE
                                + ", Build: $Id: PlotFrame.java 57040 2010-01-27 20:52:32Z cxh $\n\n"
                                + "For more information, see\n"
                                + "http://ptolemy.eecs.berkeley.edu/java/ptplot\n\n"
                                + "Copyright (c) 1997-2010, "
                                + "The Regents of the University of California.",
                        "About Ptolemy Plot", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Close the window.
     */
    protected void _close() {
        dispose();
    }

    /** Interactively edit the file format in a modal dialog.
     */
    protected void _editFormat() {
        PlotFormatter fmt = new PlotFormatter(plot);
        fmt.openModal();
    }

    /** Query the user for a filename and export the plot to that file.
     *  Currently, the only supported format is EPS.
     */
    protected void _export() {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.addChoosableFileFilter(new EPSFileFilter());
        fileDialog.setDialogTitle("Export EPS to...");

        if (_directory != null) {
            fileDialog.setCurrentDirectory(_directory);
        } else {
            // The default on Windows is to open at user.home, which is
            // typically an absurd directory inside the O/S installation.
            // So we use the current directory instead.
            String cwd = StringUtilities.getProperty("user.dir");

            if (cwd != null) {
                fileDialog.setCurrentDirectory(new File(cwd));
            }
        }

        fileDialog.setSelectedFile(new File(fileDialog.getCurrentDirectory(),
                "plot.eps"));

        int returnVal = fileDialog.showDialog(this, "Export");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileDialog.getSelectedFile();

            try {
                FileOutputStream fout = null;

                try {
                    fout = new FileOutputStream(file);
                    plot.export(fout);
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (Throwable throwable) {
                            log.error("Ignoring failure to close stream on {}", file);
                            log.error(throwable.toString());
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting plot: "
                        + ex, "Ptolemy II Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /** Display more detailed information than given by _about().
     */
    protected void _help() {
        JOptionPane.showMessageDialog(this,
                "PlotFrame is a plot in a top-level window.\n"
                        + "  File formats understood: Ptplot ASCII.\n"
                        + "  Left mouse button: Zooming.",
                "About Ptolemy Plot", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Open a new file and plot its data.
     */
    protected void _open() {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Select a plot file");

        // Filter file names.
        fileDialog.addChoosableFileFilter(new PLTOrXMLFileFilter());

        if (_directory != null) {
            fileDialog.setCurrentDirectory(_directory);
        } else {
            // The default on Windows is to open at user.home, which is
            // typically an absurd directory inside the O/S installation.
            // So we use the current directory instead.
            String cwd = StringUtilities.getProperty("user.dir");

            if (cwd != null) {
                fileDialog.setCurrentDirectory(new File(cwd));
            }
        }

        int returnVal = fileDialog.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _file = fileDialog.getSelectedFile();
            setTitle(_file.getName());
            _directory = fileDialog.getCurrentDirectory();

            FileInputStream input = null;
            try {
                plot.clear(true);
                input = new FileInputStream(_file);
                _read(new URL("file", null, _directory.getAbsolutePath()),
                        input);
                plot.repaint();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "File not found:\n"
                        + ex.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading input:\n"
                        + ex.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception ex) {
                        // Ignore this, but print anyway.
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /** Print the plot using the native interface.
     */
    protected void _print() {
        // If you are using $PTII/bin/vergil, under bash, set this property:
        // export JAVAFLAGS=-Dptolemy.ptII.print.platform=CrossPlatform
        // and then run $PTII/bin/vergil
        if (StringUtilities.getProperty("ptolemy.ptII.print.platform").equals(
                "CrossPlatform")) {
            _printCrossPlatform();
        } else {
            _printNative();
        }
    }

    /** Print using the cross platform dialog.
     *  Note that in java 1.6.0_05, the properties button is disabled,
     *  so using _printNative() is preferred.
     */
    protected void _printCrossPlatform() {
        // FIXME: Code duplication with PlotBox and Top.

        // Note that this dialog used to be slow, but this was fixed in:
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6539061
        // Note that this dialog used to appear behind other windows,
        // but this was fixed in:
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775862

        // For more notes, see
        // http://chess.eecs.berkeley.edu/ptolemy/wiki/Ptolemy/PrintingFromJava

        // Build a set of attributes
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        // No need to call pageDialog, printDialog has that same tab
        //PageFormat pageFormat = job.pageDialog(aset);
        //PageFormat pageFormat = job.pageDialog(job.defaultPage());
        //job.setPrintable(plot, pageFormat);
        job.setPrintable(plot);

        if (job.printDialog(aset)) {
            try {
                job.print(aset);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed:\n"
                        + ex.toString(), "Print Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /** If a PDF printer is available print to it.
     *  @exception PrinterException If a printer with the string "PDF"
     * cannot be found or if the job cannot be set to the PDF print
     * service or if there is another problem printing.
     */
    protected void _printPDF() throws PrinterException {
        // Find something that will print to PDF
        boolean foundPDFPrinter = false;

        PrintService pdfPrintService = null;
        PrintService printServices[] = PrinterJob.lookupPrintServices();
        for (int i = 0; i < printServices.length; i++) {
            if (printServices[i].getName().indexOf("PDF") != -1) {
                foundPDFPrinter = true;
                pdfPrintService = printServices[i];
            }
        }

        if (pdfPrintService == null) {
            throw new PrinterException("Could not find a printer with the "
                    + "string \"PDF\" in its name.");
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(pdfPrintService);
        job.setPrintable(plot, job.defaultPage());

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        // This gets ignored, but let's try it anyway
        Destination destination = new Destination(new File("plot.pdf").toURI());
        aset.add(destination);

        job.print(aset);
        if (foundPDFPrinter) {
            System.out
                    .println("Plot printed from command line. "
                            + "Under MacOSX, look for "
                            + "~/Desktop/Java Printing.pdf");
        }
    }

    /** Print using the native dialog.
     */
    protected void _printNative() {
        // FIXME: Code duplication with PlotBox and Top.

        // Native printing used not honor the user's
        // choice of portrait vs. landscape.

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.pageDialog(job.defaultPage());
        job.setPrintable(plot, pageFormat);

        if (job.printDialog()) {
            try {
                // job.print() eventually
                // calls PlotBox.print(Graphics, PageFormat)
                job.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed:\n"
                        + ex.toString(), "Print Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /** Read the specified stream.  Derived classes may override this
     *  to support other file formats.
     *  @param base The base for relative file references, or null if
     *   there are not relative file references.
     *  @param in The input stream.
     *  @exception IOException If the stream cannot be read.
     */
    protected void _read(URL base, InputStream in) throws IOException {
        plot.read(in);
    }

    /** Save the plot to the current file, determined by the
     *  and _file protected variable.
     */
    protected void _save() {
        if (_file != null) {
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(_file);
                plot.write(output);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing file:\n"
                        + ex.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (Exception ex) {
                        // Ignore, but print anyway.
                        ex.printStackTrace();
                    }
                }
            }
        } else {
            _saveAs();
        }
    }

    /** Query the user for a filename and save the plot to that file.
     */
    protected void _saveAs() {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.addChoosableFileFilter(new PLTOrXMLFileFilter());
        fileDialog.setDialogTitle("Save plot as...");

        if (_directory != null) {
            fileDialog.setCurrentDirectory(_directory);
        } else {
            // The default on Windows is to open at user.home, which is
            // typically an absurd directory inside the O/S installation.
            // So we use the current directory instead.
            String cwd = StringUtilities.getProperty("user.dir");

            if (cwd != null) {
                fileDialog.setCurrentDirectory(new File(cwd));
            }
        }

        fileDialog.setSelectedFile(new File(fileDialog.getCurrentDirectory(),
                "plot.xml"));

        int returnVal = fileDialog.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            _file = fileDialog.getSelectedFile();
            setTitle(_file.getName());
            _directory = fileDialog.getCurrentDirectory();
            _save();
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////
    class FileMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem target = (JMenuItem) e.getSource();
            String actionCommand = target.getActionCommand();

            try {
                if (actionCommand.equals("Open")) {
                    _open();
                } else if (actionCommand.equals("Save")) {
                    _save();
                } else if (actionCommand.equals("SaveAs")) {
                    _saveAs();
                } else if (actionCommand.equals("Export")) {
                    _export();
                } else if (actionCommand.equals("Print")) {
                    _print();
                } else if (actionCommand.equals("Close")) {
                    _close();
                }
            } catch (Exception exception) {
                // If we do not catch exceptions here, then they
                // disappear to stdout, which is bad if we launched
                // where there is no stdout visible.
                JOptionPane.showMessageDialog(null, "File Menu Exception:\n"
                        + exception.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);
            }

            // NOTE: The following should not be needed, but there jdk1.3beta
            // appears to have a bug in swing where repainting doesn't
            // properly occur.
            repaint();
        }
    }

    class FormatListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                _editFormat();
            } catch (Exception exception) {
                // If we do not catch exceptions here, then they
                // disappear to stdout, which is bad if we launched
                // where there is no stdout visible.
                log.info("Format Exception: " + exception);
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Format Exception:\n"
                        + exception.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);

            }

            // NOTE: The following should not be needed, but there jdk1.3beta
            // appears to have a bug in swing where repainting doesn't
            // properly occur.
            repaint();
        }
    }

    class SpecialMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem target = (JMenuItem) e.getSource();
            String actionCommand = target.getActionCommand();

            try {
                if (actionCommand.equals("About")) {
                    _about();
                } else if (actionCommand.equals("Help")) {
                    _help();
                } else if (actionCommand.equals("Fill")) {
                    plot.fillPlot();
                } else if (actionCommand.equals("Reset axes")) {
                    plot.resetAxes();
                } else if (actionCommand.equals("Clear")) {
                    plot.clear(false);
                    plot.repaint();
                } else if (actionCommand.equals("Sample plot")) {
                    plot.clear(true);
                    samplePlot();
                }
            } catch (Exception exception) {
                // If we do not catch exceptions here, then they
                // disappear to stdout, which is bad if we launched
                // where there is no stdout visible.
                JOptionPane.showMessageDialog(null, "Special Menu Exception:\n"
                        + exception.toString(), "Ptolemy Plot Error",
                        JOptionPane.WARNING_MESSAGE);
            }

            // NOTE: The following should not be needed, but there jdk1.3beta
            // appears to have a bug in swing where repainting doesn't
            // properly occur.
            repaint();
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////

    /** Display only .eps files */
    static class EPSFileFilter extends FileFilter {
        /** Accept only .eps files.
         *  @param fileOrDirectory The file or directory to be checked.
         *  @return true if the file is a directory, a .eps file
         */
        @Override
        public boolean accept(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory()) {
                return true;
            }

            String fileOrDirectoryName = fileOrDirectory.getName();
            int dotIndex = fileOrDirectoryName.lastIndexOf('.');

            if (dotIndex == -1) {
                return false;
            }

            String extension = fileOrDirectoryName.substring(dotIndex);

            if (extension != null) {
                if (extension.equalsIgnoreCase(".eps")) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        /**  The description of this filter */
        @Override
        public String getDescription() {
            return "Encapsulated PostScript (.eps) files";
        }
    }

    /** Display only .plt and .xml files */
    static class PLTOrXMLFileFilter extends FileFilter {
        /** Accept only .plt or .xml files.
         *  @param fileOrDirectory The file to be checked.
         *  @return true if the file is a directory, a .plot or a .xml file.
         */
        @Override
        public boolean accept(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory()) {
                return true;
            }

            String fileOrDirectoryName = fileOrDirectory.getName();
            int dotIndex = fileOrDirectoryName.lastIndexOf('.');

            if (dotIndex == -1) {
                return false;
            }

            String extension = fileOrDirectoryName.substring(dotIndex);

            if (extension != null) {
                if (extension.equalsIgnoreCase(".plt")
                        || extension.equalsIgnoreCase(".xml")) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        /**  The description of this filter */
        @Override
        public String getDescription() {
            return ".plt and .xml files";
        }
    }
}
